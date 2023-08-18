package com.swapnil.frnd.model.network.request

import com.google.gson.annotations.SerializedName
import com.swapnil.frnd.model.TaskDetails

data class TaskPostRequest(

    @SerializedName("user_id")
    val userId: Int,

    @SerializedName("task")
    val taskDetails: TaskDetails

)