package dev.kxxcn.woozoora.data.source.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.util.*

@Entity(tableName = "notification")
data class NotificationEntity(
    @PrimaryKey
    @SerializedName("id")
    @ColumnInfo
    val id: String = UUID.randomUUID().toString(),

    @SerializedName("userName")
    @ColumnInfo
    val userName: String?,

    @SerializedName("userProfile")
    @ColumnInfo
    val userProfile: String?,

    @SerializedName("transactionId")
    @ColumnInfo
    val transactionId: String?,

    @SerializedName("transactionName")
    @ColumnInfo
    val transactionName: String?,

    @SerializedName("transactionDate")
    @ColumnInfo
    val transactionDate: Long?,

    @SerializedName("transactionPrice")
    @ColumnInfo
    val transactionPrice: Int?,

    @SerializedName("transactionType")
    @ColumnInfo
    val transactionType: Int?,

    @SerializedName("date")
    @ColumnInfo
    val date: Long?,

    @SerializedName("isChecked")
    @ColumnInfo
    val isChecked: Int,
)
