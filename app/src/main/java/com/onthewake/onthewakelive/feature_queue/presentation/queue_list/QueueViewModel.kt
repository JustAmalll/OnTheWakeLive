package com.onthewake.onthewakelive.feature_queue.presentation.queue_list

import android.app.Application
import android.content.SharedPreferences
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.accompanist.pager.ExperimentalPagerApi
import com.onthewake.onthewakelive.R
import com.onthewake.onthewakelive.core.util.Constants.ADMIN_IDS
import com.onthewake.onthewakelive.core.util.Constants.PREFS_FIRST_NAME
import com.onthewake.onthewakelive.core.util.Constants.PREFS_USER_ID
import com.onthewake.onthewakelive.core.util.Resource
import com.onthewake.onthewakelive.feature_queue.data.remote.QueueService
import com.onthewake.onthewakelive.feature_queue.data.remote.QueueSocketService
import com.onthewake.onthewakelive.feature_queue.domain.module.QueueItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalPagerApi
@HiltViewModel
class QueueViewModel @Inject constructor(
    private val queueService: QueueService,
    private val queueSocketService: QueueSocketService,
    private val context: Application,
    prefs: SharedPreferences,
) : AndroidViewModel(context) {

    private val _state = mutableStateOf(QueueState())
    val state: State<QueueState> = _state

    private val _snackBarEvent = MutableSharedFlow<String>()
    val snackBarEvent = _snackBarEvent.asSharedFlow()

    private val _snackBarWithActionEvent = MutableSharedFlow<QueueItem>()
    val snackBarWithActionEvent = _snackBarWithActionEvent.asSharedFlow()

    var showDialog by mutableStateOf(false)

    val firstName = prefs.getString(PREFS_FIRST_NAME, null)
    val userId = prefs.getString(PREFS_USER_ID, null)

    fun connectToQueue() {
        getQueue()

        viewModelScope.launch {
            when (val result = queueSocketService.initSession(firstName!!)) {
                is Resource.Success -> {
                    observeQueue()
                }
                is Resource.Error -> {
                    _snackBarEvent.emit(
                        result.message ?: context.getString(R.string.unknown_error)
                    )
                }
            }
        }
    }

    private fun observeQueue() {
        queueSocketService.observeQueue()
            .onEach { queueItem ->
                val newList = state.value.queueItem
                    .toMutableList()
                    .apply {
                        if (queueItem.isDeleteAction) removeIf { it.id == queueItem.queueItem.id }
                        else add(0, queueItem.queueItem)
                    }
                    .sortedWith(compareByDescending { it.timestamp })
                _state.value = state.value.copy(queueItem = newList)
            }.launchIn(viewModelScope)
    }

    private fun getQueue() {
        viewModelScope.launch {
            _state.value = state.value.copy(isQueueLoading = true)
            val result = queueService.getQueue()
            _state.value = state.value.copy(queueItem = result, isQueueLoading = false)
        }
    }

    fun addToQueue(isLeftQueue: Boolean, firstName: String, timestamp: Long) {
        viewModelScope.launch {
            _state.value = state.value.copy(isQueueLoading = true)

            val allQueue = _state.value.queueItem.sortedWith(compareBy { it.timestamp })
            val leftQueueItems = allQueue.filter { it.isLeftQueue }
            val rightQueueItems = allQueue.filter { !it.isLeftQueue }

            val isUserAlreadyInQueue = allQueue.find { it.userId == userId }
            val isUserAlreadyInLeftQueue = leftQueueItems.find { it.userId == userId }
            val isUserAlreadyInRightQueue = rightQueueItems.find { it.userId == userId }

            val userPositionInLeftQueue = leftQueueItems.indexOf(isUserAlreadyInLeftQueue)
            val userPositionInRightQueue = rightQueueItems.indexOf(isUserAlreadyInRightQueue)

            if (userId in ADMIN_IDS) {
                queueSocketService.addToQueue(isLeftQueue, firstName, timestamp)
            } else {
                if (isUserAlreadyInQueue == null) {
                    queueSocketService.addToQueue(isLeftQueue, firstName, timestamp)
                } else if (isLeftQueue && isUserAlreadyInLeftQueue != null) {
                    _snackBarEvent.emit(context.getString(R.string.already_in_queue_error))
                } else if (!isLeftQueue && isUserAlreadyInRightQueue != null) {
                    _snackBarEvent.emit(context.getString(R.string.already_in_queue_error))
                } else if (isLeftQueue && isUserAlreadyInRightQueue != null) {
                    if (leftQueueItems.size - userPositionInRightQueue >= 4) {
                        queueSocketService.addToQueue(isLeftQueue, firstName, timestamp)
                    } else if (userPositionInRightQueue - leftQueueItems.size >= 4) {
                        queueSocketService.addToQueue(isLeftQueue, firstName, timestamp)
                    } else {
                        _snackBarEvent.emit(context.getString(R.string.interval_error))
                    }
                } else if (!isLeftQueue && isUserAlreadyInLeftQueue != null) {
                    if (rightQueueItems.size - userPositionInLeftQueue >= 4) {
                        queueSocketService.addToQueue(isLeftQueue, firstName, timestamp)
                    } else if (userPositionInLeftQueue - rightQueueItems.size >= 4) {
                        queueSocketService.addToQueue(isLeftQueue, firstName, timestamp)
                    } else {
                        _snackBarEvent.emit(context.getString(R.string.interval_error))
                    }
                }
            }

            _state.value = state.value.copy(isQueueLoading = false)
        }
    }

    fun deleteQueueItem(queueItemId: String) {
        viewModelScope.launch {
            _state.value = state.value.copy(isQueueLoading = true)
            when (val result = queueService.deleteQueueItem(queueItemId)) {
                is Resource.Success -> {
                    result.data?.let { queueItem ->
                        _snackBarWithActionEvent.emit(
                            QueueItem(
                                id = queueItem.id,
                                userId = queueItem.userId,
                                firstName = queueItem.firstName,
                                lastName = queueItem.lastName,
                                profilePictureUri = queueItem.profilePictureUri,
                                isLeftQueue = queueItem.isLeftQueue,
                                timestamp = queueItem.timestamp
                            )
                        )
                    }
                }
                is Resource.Error -> {
                    _snackBarEvent.emit(result.message ?: "Unknown Error")
                }
            }
            _state.value = state.value.copy(isQueueLoading = false)
        }
    }


    fun disconnect() {
        viewModelScope.launch {
            queueSocketService.closeSession()
        }
    }
}