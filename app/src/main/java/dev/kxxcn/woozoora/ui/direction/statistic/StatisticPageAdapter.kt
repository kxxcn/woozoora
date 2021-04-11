package dev.kxxcn.woozoora.ui.direction.statistic

import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import dev.kxxcn.woozoora.common.*
import dev.kxxcn.woozoora.common.extension.month
import dev.kxxcn.woozoora.common.extension.year
import dev.kxxcn.woozoora.domain.model.OverviewData
import dev.kxxcn.woozoora.domain.model.UserData
import dev.kxxcn.woozoora.ui.base.BaseViewModel
import dev.kxxcn.woozoora.ui.direction.home.HomeFilterType

class StatisticPageAdapter(
    fragment: Fragment,
    private val viewModel: BaseViewModel,
) : FragmentStateAdapter(fragment.childFragmentManager, fragment.viewLifecycleOwner.lifecycle) {

    private var overview: OverviewData? = null
        set(value) {
            if (field != value) {
                field = value
                notifyDataSetChanged()
            }
        }

    private val userIds: List<Long>
        get() = overview?.allUsers?.map { it.unique } ?: emptyList()

    override fun getItemCount(): Int {
        return overview?.allUsers?.size ?: 0
    }

    override fun createFragment(position: Int): Fragment {
        val user = getItem(position)
        return StatisticUserFragment().apply {
            arguments = bundleOf(
                KEY_USER_ID to user?.id,
                KEY_USER_NAME to user?.name,
                KEY_USER_PROFILE to user?.profile,
                KEY_USER_PRICE to overview?.getTotalSpendingById(user?.id),
                KEY_USER_TRANSACTION_COUNT to overview?.getCountOfTransactionsById(user?.id),
                KEY_USER_PAYMENT_CASH to overview?.getTotalSpendingByPayment(
                    user?.id,
                    Payment.CASH
                ),
                KEY_USER_PAYMENT_CARD to overview?.getTotalSpendingByPayment(
                    user?.id,
                    Payment.CARD
                ),
                KEY_USER_BUDGET_USAGE_RATE to overview?.getUsageRateOfBudget(
                    user?.id,
                    HomeFilterType.MONTHLY,
                    year = viewModel.dateTimeMs.value?.year,
                    month = viewModel.dateTimeMs.value?.month,
                )?.toInt()
            )
        }
    }

    override fun getItemId(position: Int): Long {
        return userIds[position]
    }

    override fun containsItem(itemId: Long): Boolean {
        return userIds.contains(itemId)
    }

    private fun getItem(position: Int): UserData? {
        return overview?.allUsers?.get(position)
    }

    fun update(newOverview: OverviewData) {
        overview = newOverview
    }
}
