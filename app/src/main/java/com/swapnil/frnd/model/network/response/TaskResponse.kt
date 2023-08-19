package com.swapnil.frnd.model.network.response

import com.google.gson.annotations.SerializedName
import com.swapnil.frnd.model.Task

data class TaskResponse(
    @SerializedName("tasks")
    val taskList: List<Task>
)