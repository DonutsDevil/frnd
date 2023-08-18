package com.swapnil.frnd.model

import com.google.gson.annotations.SerializedName

data class TaskDetails(
    @SerializedName("title")
    val title: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("date")
    val date: String
)