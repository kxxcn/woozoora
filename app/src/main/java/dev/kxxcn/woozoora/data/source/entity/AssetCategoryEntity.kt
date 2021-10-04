package dev.kxxcn.woozoora.data.source.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.util.*

@Entity(tableName = "acategory")
data class AssetCategoryEntity(
    @PrimaryKey
    @SerializedName("id")
    @ColumnInfo
    val id: String = UUID.randomUUID().toString(),

    @SerializedName("category")
    @ColumnInfo
    val category: String,

    @SerializedName("priority")
    @ColumnInfo
    val priority: Int,
)
