package dev.kxxcn.woozoora.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserData(
    var id: String,
    var code: String? = null,
    var token: String? = null,
    var name: String? = null,
    var profile: String? = null,
    var email: String? = null,
    var budget: Long = 0,
    var createdDate: Long = 0,
    val year: Int = 0,
    var type: Int = -1,
    val unique: Long = System.nanoTime(),
) : Parcelable {

    val hasEmail: Boolean
        get() = email?.isNotEmpty() == true

    override fun equals(other: Any?): Boolean {
        return if (other is UserData) {
            id == other.id
                    && code == other.code
                    && token == other.token
                    && name == other.name
                    && profile == other.profile
                    && email == other.email
                    && budget == other.budget
                    && createdDate == other.createdDate
                    && year == other.year
                    && type == other.type
        } else {
            false
        }
    }

    override fun hashCode(): Int {
        return id.hashCode() * 31 +
                code.hashCode() * 31 +
                token.hashCode() * 31 +
                name.hashCode() * 31 +
                profile.hashCode() * 31 +
                email.hashCode() * 31 +
                budget.hashCode() * 31 +
                createdDate.hashCode() * 31 +
                year.hashCode() * 31 +
                type.hashCode() * 31
    }
}
