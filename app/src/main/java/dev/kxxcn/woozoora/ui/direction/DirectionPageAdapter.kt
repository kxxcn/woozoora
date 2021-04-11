package dev.kxxcn.woozoora.ui.direction

import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import dev.kxxcn.woozoora.common.InvalidPositionException
import dev.kxxcn.woozoora.common.KEY_DEFAULT_DATE
import dev.kxxcn.woozoora.ui.direction.history.HistoryFragment
import dev.kxxcn.woozoora.ui.direction.home.HomeFragment
import dev.kxxcn.woozoora.ui.direction.more.MoreFragment
import dev.kxxcn.woozoora.ui.direction.statistic.StatisticFragment

class DirectionPageAdapter(
    fragment: Fragment,
    private val defaultMs: Long?,
) : FragmentStateAdapter(fragment.childFragmentManager, fragment.viewLifecycleOwner.lifecycle) {

    override fun getItemCount(): Int {
        return PAGE_COUNT
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            PAGE_HOME -> HomeFragment()
            PAGE_HISTORY -> HistoryFragment().apply {
                arguments = bundleOf(KEY_DEFAULT_DATE to defaultMs)
            }
            PAGE_STATISTIC -> StatisticFragment()
            PAGE_MORE -> MoreFragment()
            else -> throw InvalidPositionException()
        }
    }

    companion object {

        private const val PAGE_COUNT = 4

        const val PAGE_HOME = 0
        const val PAGE_HISTORY = 1
        const val PAGE_STATISTIC = 2
        const val PAGE_MORE = 3
    }
}
