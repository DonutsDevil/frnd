package com.swapnil.frnd.model

import com.google.gson.annotations.SerializedName

data class Task(
    @SerializedName("task_id")
    val taskId: Int,
    @SerializedName("task_detail")
    val taskDetail: TaskDetails
)
