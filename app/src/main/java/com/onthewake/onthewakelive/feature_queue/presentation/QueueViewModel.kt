package com.onthewake.onthewakelive.feature_queue.presentation

import android.app.Application
import android.content.SharedPreferences
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.onthewake.onthewakelive.R
import com.onthewake.onthewakelive.feature_queue.data.remote.QueueService
import com.onthewake.onthewakelive.feature_queue.data.remote.QueueSocketService
import com.onthewake.onthewakelive.feature_queue.domain.module.Queue
import com.onthewake.onthewakelive.util.Constants.FIRST_ADMIN_USER_ID
import com.onthewake.onthewakelive.util.Constants.PREFS_FIRST_NAME
import com.onthewake.onthewakelive.util.Constants.PREFS_USER_ID
import com.onthewake.onthewakelive.util.Constants.SECOND_ADMIN_USER_ID
import com.onthewake.onthewakelive.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

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

    val firstName = prefs.getString(PREFS_FIRST_NAME, null)
    val userId = prefs.getString(PREFS_USER_ID, null)

    fun connectToQueue() {
        getQueue()

        viewModelScope.launch {
            when (val result = queueSocketService.initSession(firstName!!)) {
                is Resource.Success -> {
                    queueSocketService.observeQueue()
                        .onEach { queueItem ->

                            val newList = state.value.queue
                                .toMutableList().apply {
                                    if (queueItem.isDeleteAction) remove(queueItem.queue)
                                    else add(0, queueItem.queue)
                                }
                                .sortedWith(compareByDescending { it.timestamp })

                            _state.value = state.value.copy(queue = newList)

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
        viewModelScope.launch {
            _state.value = state.value.copy(isQueueLoading = true)
            val result = queueService.getQueue()
            _state.value = state.value.copy(
                queue = result, isQueueLoading = false
            )
        }
    }

    fun addToQueue(leftQueue: String, firstName: String, timestamp: Long) {
        viewModelScope.launch {
            _state.value = state.value.copy(isQueueLoading = true)

            val allQueue = _state.value.queue

            val leftQueueItems = allQueue.filter { it.leftQueue == "true" }
            val rightQueueItems = allQueue.filter { it.leftQueue == "false" }

            val userItemInLeftQueue = leftQueueItems.find { it.userId == userId }
            val userItemInRightQueue = rightQueueItems.find { it.userId == userId }

            val userPositionInLeftQueue = leftQueueItems.indexOf(userItemInLeftQueue)
            val userPositionInRightQueue = rightQueueItems.indexOf(userItemInRightQueue)

            val ifUserAlreadyInQueue = allQueue.find { it.userId == userId }

            if (userId == FIRST_ADMIN_USER_ID || userId == SECOND_ADMIN_USER_ID) {
                queueSocketService.addToQueue(leftQueue, firstName, timestamp)
            } else {
                if (leftQueue == "true" && ifUserAlreadyInQueue == null ||
                    leftQueue == "false" && ifUserAlreadyInQueue == null
                ) {
                    queueSocketService.addToQueue(leftQueue, firstName, timestamp)
                } else if (leftQueue == "true" && userItemInLeftQueue != null) {
                    _snackBarEvent.emit(context.getString(R.string.already_in_queue_error))
                } else if (leftQueue == "false" && userItemInRightQueue != null) {
                    _snackBarEvent.emit(context.getString(R.string.already_in_queue_error))
                } else if (leftQueue == "true" && userItemInRightQueue != null) {
                    if (leftQueueItems.size - userPositionInRightQueue >= 3) {
                        queueSocketService.addToQueue(leftQueue, firstName, timestamp)
                    } else {
                        _snackBarEvent.emit(context.getString(R.string.interval_error))
                    }
                } else if (leftQueue == "false" && userItemInLeftQueue != null) {
                    if (rightQueueItems.size - userPositionInLeftQueue >= 3) {
                        queueSocketService.addToQueue(leftQueue, firstName, timestamp)
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
            val deletedItem = queueService.deleteQueueItem(queueItemId)
            deletedItem?.let {
                _snackBarWithActionEvent.emit(
                    Queue(
                        id = it.id,
                        userId = it.userId,
                        firstName = it.firstName,
                        leftQueue = it.leftQueue,
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