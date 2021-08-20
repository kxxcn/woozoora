package dev.kxxcn.woozoora.data.source.entity

import com.google.gson.annotations.SerializedName

data class CodeEntity(
    @SerializedName("code")
    val code: String?,
    @SerializedName("transfer")
    val transfer: Boolean,
)
