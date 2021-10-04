package dev.kxxcn.woozoora.ui.edit.item

import java.util.*

data class EditCategory(
    val id: Int = Random().nextInt(Int.MAX_VALUE),
    val name: String = "",
    val ordinal: Int = 11,
    var isSelected: Boolean = false,
) {

    fun toggle(): EditCategory {
        isSelected = !isSelected
        return this
    }

    fun release(): EditCategory {
        isSelected = false
        return this
    }
}
