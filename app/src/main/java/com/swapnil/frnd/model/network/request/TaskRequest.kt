package com.swapnil.frnd.model.network.request

import com.google.gson.annotations.SerializedName

data class TaskRequest(
    @SerializedName("user_id")
    val userId: Int
)