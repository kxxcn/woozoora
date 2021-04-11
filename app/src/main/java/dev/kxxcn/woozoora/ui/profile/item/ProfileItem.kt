package dev.kxxcn.woozoora.ui.profile.item

import dev.kxxcn.woozoora.ui.change.ChangeFilterType

data class ProfileItem(
    val res: Int,
    val content: String?,
    val filterType: ChangeFilterType? = null,
) {

    val isEditable: Boolean
        get() = filterType != null
}
