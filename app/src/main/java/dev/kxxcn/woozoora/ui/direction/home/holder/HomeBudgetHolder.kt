package dev.kxxcn.woozoora.ui.direction.home.holder

import android.view.LayoutInflater
import android.view.ViewGroup
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import dev.kxxcn.woozoora.R
import dev.kxxcn.woozoora.common.extension.dpToPx
import dev.kxxcn.woozoora.common.extension.setWeeklyChart
import dev.kxxcn.woozoora.common.extension.updateValues
import dev.kxxcn.woozoora.databinding.HomeBudgetItemBinding
import dev.kxxcn.woozoora.domain.model.OverviewData
import dev.kxxcn.woozoora.ui.base.AnimatableHolder
import dev.kxxcn.woozoora.ui.custom.WeeklyMarkerView
import dev.kxxcn.woozoora.ui.direction.home.HomeFilterType
import dev.kxxcn.woozoora.ui.direction.home.HomeViewModel
import dev.kxxcn.woozoora.util.Calculator
import dev.kxxcn.woozoora.util.RoundedHorizontalBarChartRenderer

class HomeBudgetHolder(
    private val binding: HomeBudgetItemBinding,
) : AnimatableHolder(binding) {

    private val dataRenderer by lazy {
        with(binding.budgetChart) {
            RoundedHorizontalBarChartRenderer(
                this,
                animator,
                viewPortHandler
            ).apply {
                radius = 15.dpToPx.toFloat()
            }
        }
    }

    override fun onDetach() {
        binding.overview?.let {
            val usageRate = it.getUsageRateOfBudget(it.id, HomeFilterType.MONTHLY)
            setProgress(usageRate.toInt(), false)
        }
        super.onDetach()
    }

    override fun animate() {
        binding.overview?.let {
            animateProgress(it)
            animateChart(it)
        }
    }

    override fun clearAnimation() {
        clearMoneyText()
        clearMarker()
        super.clearAnimation()
    }

    fun bind(overview: OverviewData, viewModel: HomeViewModel) {
        setupBinding(overview, viewModel)
        setupBarChart()
    }

    private fun setupBinding(overview: OverviewData, viewModel: HomeViewModel) {
        with(binding) {
            this.lifecycleOwner = this@HomeBudgetHolder
            this.overview = overview
            this.viewModel = viewModel
            this.executePendingBindings()
        }
    }

    private fun setupBarChart() {
        binding.budgetChart.setWeeklyChart(
            R.color.budgetActive,
            R.color.budgetInactive,
            R.color.budgetActive,
            maximum = 100f,
            dataRenderer = dataRenderer,
            marker = WeeklyMarkerView(
                context,
                layoutResource = R.layout.weekly_marker_view
            ).apply { isHorizontalBarChart = true }
        )
    }

    private fun animateProgress(overview: OverviewData) {
        val usageRate = overview.getUsageRateOfBudget(overview.id, HomeFilterType.MONTHLY)
        setProgress(usageRate.toInt())
    }

    private fun setProgress(progress: Int, animate: Boolean = true) {
        binding.budgetProgress.setProgress(progress, animate)
    }

    private fun animateChart(overview: OverviewData) {
        val values = Calculator.calculateRangeByWeek().mapIndexed { index, range ->
            val value = overview.getUsageRateOfBudget(overview.id, range = range)
            BarEntry((index + 1).toFloat(), value, overview.budget)
        }
        binding.budgetChart.updateValues<BarDataSet> { this.values = values }
    }

    private fun clearMoneyText() {
        binding.budgetProgress.setProgress(0, false)
    }

    private fun clearMarker() {
        binding.budgetChart.highlightValue(null)
    }

    companion object {

        fun from(parent: ViewGroup): HomeBudgetHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = HomeBudgetItemBinding.inflate(inflater, parent, false)
            return HomeBudgetHolder(binding)
        }
    }
}
