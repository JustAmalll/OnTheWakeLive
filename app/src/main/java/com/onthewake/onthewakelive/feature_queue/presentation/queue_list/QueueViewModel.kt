package com.onthewake.onthewakelive.feature_queue.presentation.queue_list

import android.app.Application
import android.content.SharedPreferences
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.accompanist.pager.ExperimentalPagerApi
import com.onthewake.onthewakelive.R
import com.onthewake.onthewakelive.feature_queue.data.remote.QueueService
import com.onthewake.onthewakelive.feature_queue.data.remote.QueueSocketService
import com.onthewake.onthewakelive.feature_queue.domain.module.Queue
import com.onthewake.onthewakelive.util.Constants.ADMIN_IDS
import com.onthewake.onthewakelive.util.Constants.PREFS_FIRST_NAME
import com.onthewake.onthewakelive.util.Constants.PREFS_USER_ID
import com.onthewake.onthewakelive.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalPagerApi
@HiltViewModel
class QueueViewModel @Inject constructor(
    private val queueService: QueueService,
    private val queueSocketService: QueueSocketService,
    private val context: Application,
    prefs: SharedPreferences
) : AndroidViewModel(context) {

    private val _state = mutableStateOf(QueueState())
    val state: State<QueueState> = _state

    private val _snackBarEvent = MutableSharedFlow<String>()
    val snackBarEvent = _snackBarEvent.asSharedFlow()

    private val _snackBarWithActionEvent = MutableSharedFlow<Queue>()
    val snackBarWithActionEvent = _snackBarWithActionEvent.asSharedFlow()

    val isAdding = mutableStateOf(false)
    val showDialog = mutableStateOf(false)

    val firstName = prefs.getString(PREFS_FIRST_NAME, null)
    val userId = prefs.getString(PREFS_USER_ID, null)

    fun connectToQueue() {
        getQueue()

        viewModelScope.launch {
            when (val result = queueSocketService.initSession(firstName ?: "Guest")) {
                is Resource.Success -> {
                    queueSocketService.observeQueue()
                        .onEach { queueItem ->
                            val newList = state.value.queue
                                .toMutableList().apply {
                                    if (queueItem.isDeleteAction) removeIf {
                                        it.id == queueItem.queue.id
                                    } else add(0, queueItem.queue)
                                }
                                .sortedWith(compareByDescending { it.timestamp })
                            _state.value = state.value.copy(queue = newList)

                            println("observed $newList")
                        }.launchIn(viewModelScope)
                }
                is Resource.Error -> {
                    _snackBarEvent.emit(
                        result.message ?: context.getString(R.string.unknown_error)
                    )
                }
            }
        }
    }

    private fun getQueue() {
        viewModelScope.launch(Dispatchers.IO) {
            _state.value = state.value.copy(isQueueLoading = true)
            val result = queueService.getQueue()
            _state.value = state.value.copy(queue = result, isQueueLoading = false)
        }
    }

    fun addToQueue(isLeftQueue: Boolean, firstName: String, timestamp: Long) {
        viewModelScope.launch {
            isAdding.value = true

            val allQueue = _state.value.queue.sortedWith(compareBy { it.timestamp })
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

            isAdding.value = false
        }
    }

    fun deleteQueueItem(queueItemId: String) {
        viewModelScope.launch {
            _state.value = state.value.copy(isQueueLoading = true)
            val deletedItem = queueService.deleteQueueItem(queueItemId)
            deletedItem?.let {
                _snackBarWithActionEvent.emit(
                    Queue(
                        id = it.id,
                        userId = it.userId,
                        firstName = it.firstName,
                        lastName = it.lastName,
                        profilePictureUri = it.profilePictureUri,
                        isLeftQueue = it.isLeftQueue,
                        timestamp = it.timestamp
                    )
                )
            }
            _state.value = state.value.copy(isQueueLoading = false)
        }
    }


    fun disconnect() {
        viewModelScope.launch {
            queueSocketService.closeSession()
        }
    }

    override fun onCleared() {
        super.onCleared()
        disconnect()
    }
}