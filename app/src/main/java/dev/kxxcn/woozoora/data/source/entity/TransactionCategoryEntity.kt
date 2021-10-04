package dev.kxxcn.woozoora.data.source.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "tcategory")
data class TransactionCategoryEntity(
    @PrimaryKey
    @SerializedName("id")
    @ColumnInfo
    val id: Int,

    @SerializedName("category")
    @ColumnInfo
    val category: String,

    @SerializedName("priority")
    @ColumnInfo
    val priority: Int,
)
