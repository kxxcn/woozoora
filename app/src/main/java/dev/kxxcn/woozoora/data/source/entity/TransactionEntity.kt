package dev.kxxcn.woozoora.data.source.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "transaction")
data class TransactionEntity(
    @SerializedName("id")
    @PrimaryKey
    @ColumnInfo
    val id: Int,

    @SerializedName("user_id")
    @ColumnInfo
    val userId: String,

    @SerializedName("code")
    @ColumnInfo
    val code: String?,

    @SerializedName("description")
    @ColumnInfo
    val description: String?,

    @SerializedName("name")
    @ColumnInfo
    val name: String?,

    @SerializedName("category")
    @ColumnInfo
    val category: Int,

    @SerializedName("payment")
    @ColumnInfo
    val payment: Int,

    @SerializedName("price")
    @ColumnInfo
    val price: Int,

    @SerializedName("date")
    @ColumnInfo
    val date: Long
)
