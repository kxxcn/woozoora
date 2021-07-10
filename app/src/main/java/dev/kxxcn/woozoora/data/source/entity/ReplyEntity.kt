package dev.kxxcn.woozoora.data.source.entity

import androidx.room.ColumnInfo
import com.google.gson.annotations.SerializedName

data class ReplyEntity(
    @SerializedName("message")
    @ColumnInfo
    val message: String,

    @SerializedName("date")
    @ColumnInfo
    val date: Long,
)
