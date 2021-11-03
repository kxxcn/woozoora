package dev.kxxcn.woozoora.data.source.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.util.*

@Entity(tableName = "statistic")
data class StatisticEntity(
    @SerializedName("startDate")
    @ColumnInfo
    val startDate: Long,

    @SerializedName("endDate")
    @ColumnInfo
    val endDate: Long,

    @SerializedName("date")
    @ColumnInfo
    val date: Long,

    @SerializedName("isChecked")
    @ColumnInfo
    val isChecked: Int,

    @PrimaryKey
    @SerializedName("id")
    @ColumnInfo
    val id: String = UUID.randomUUID().toString(),
)
