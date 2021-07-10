package dev.kxxcn.woozoora.data.source.entity

import androidx.room.ColumnInfo
import com.google.gson.annotations.SerializedName

data class AskEntity(
    @SerializedName("user_id")
    @ColumnInfo
    val userId: String,

    @SerializedName("message")
    @ColumnInfo
    val message: String,

    @SerializedName("version")
    @ColumnInfo
    val version: String,

    @SerializedName("device")
    @ColumnInfo
    val device: String,

    @SerializedName("os")
    @ColumnInfo
    val os: Int,

    @SerializedName("date")
    @ColumnInfo
    val date: Long,

    @SerializedName("reply")
    @ColumnInfo
    val reply: ReplyEntity?,
)
