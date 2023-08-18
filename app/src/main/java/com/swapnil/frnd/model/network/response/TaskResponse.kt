package com.swapnil.frnd.model.network.response

import com.google.gson.annotations.SerializedName

data class TaskResponse(
    @SerializedName("status")
    val status: String
)
