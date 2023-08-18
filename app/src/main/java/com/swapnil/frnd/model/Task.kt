package com.swapnil.frnd.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @SerializedName("task_id")
    val taskId: Int?= null,
    @SerializedName("task_detail")
    @Embedded
    val taskDetail: TaskDetails? = null
)
