package com.onthewake.onthewakelive.feature_queue.presentation.queue_list

import com.onthewake.onthewakelive.feature_queue.domain.module.Queue

data class QueueState(
    val queue: List<Queue> = emptyList(),
    var isQueueLoading: Boolean = false
)