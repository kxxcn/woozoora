package dev.kxxcn.woozoora.util

import dev.kxxcn.woozoora.domain.model.OptionData
import dev.kxxcn.woozoora.ui.direction.DirectionPageAdapter

object Director {

    fun findPage(option: OptionData): Int {
        return when (option) {
            OptionData.DEFAULT -> DirectionPageAdapter.PAGE_HOME
            OptionData.REGISTRATION -> DirectionPageAdapter.PAGE_HISTORY
            OptionData.DAILY -> DirectionPageAdapter.PAGE_HOME
            OptionData.WEEKLY -> DirectionPageAdapter.PAGE_STATISTIC
            OptionData.NOTICE -> DirectionPageAdapter.PAGE_MORE
        }
    }
}
