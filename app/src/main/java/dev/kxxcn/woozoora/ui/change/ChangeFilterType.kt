package dev.kxxcn.woozoora.ui.change

import dev.kxxcn.woozoora.R

enum class ChangeFilterType(val nameRes: Int) {

    YEAR(R.string.birth_year),

    BUDGET(R.string.budget);

    companion object {

        fun find(ordinal: Int?): ChangeFilterType? {
            return ordinal?.let { values()[it] }
        }
    }
}
