package dev.kxxcn.woozoora.data.source.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "user")
data class UserEntity(
    @PrimaryKey
    @SerializedName("id")
    @ColumnInfo
    val id: String,

    @SerializedName("code")
    @ColumnInfo
    val code: String? = null,

    @SerializedName("token")
    @ColumnInfo
    var token: String? = null,

    @SerializedName("name")
    @ColumnInfo
    val name: String? = null,

    @SerializedName("profile")
    @ColumnInfo
    val profile: String? = null,

    @SerializedName("email")
    @ColumnInfo
    val email: String? = null,

    @SerializedName("budget")
    @ColumnInfo
    val budget: Long = 0,

    @SerializedName("date")
    @ColumnInfo
    val createdDate: Long = 0,

    @SerializedName("year")
    @ColumnInfo
    val year: Int = 0,

    @SerializedName("type")
    @ColumnInfo
    val type: Int = -1,
)
