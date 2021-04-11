package dev.kxxcn.woozoora.ui.direction.home.holder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.forEach
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import dev.kxxcn.woozoora.R
import dev.kxxcn.woozoora.common.Payment
import dev.kxxcn.woozoora.common.base.Animatable
import dev.kxxcn.woozoora.common.extension.*
import dev.kxxcn.woozoora.databinding.HomeGroupItemBinding
import dev.kxxcn.woozoora.domain.model.OverviewData
import dev.kxxcn.woozoora.ui.base.AnimatableHolder
import dev.kxxcn.woozoora.ui.custom.WeeklyMarkerView
import dev.kxxcn.woozoora.ui.direction.home.HomeFilterType
import dev.kxxcn.woozoora.ui.direction.home.HomeViewModel
import dev.kxxcn.woozoora.util.Calculator
import dev.kxxcn.woozoora.util.ColorGenerator
import dev.kxxcn.woozoora.util.Converter

class HomeGroupHolder(
    private val binding: HomeGroupItemBinding,
) : AnimatableHolder(binding) {

    override fun animate() {
        binding.overview?.let {
            animateChart(it)
            animateProfile()
            animateTotalSpending(it)
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
            this.lifecycleOwner = this@HomeGroupHolder
            this.overview = overview
            this.viewModel = viewModel
            this.executePendingBindings()
        }
    }

    private fun setupBarChart() {
        with(binding.groupChart) {
            setWeeklyChart(
                R.color.white01,
                R.color.black_alpha_20,
                isDrawGridLines = true,
                marker = WeeklyMarkerView(context, layoutResource = R.layout.weekly_marker_view)
            )
        }
    }

    private fun animateChart(overview: OverviewData) {
        binding.groupChart.updateValues<BarDataSet> {
            val shadowColor = barShadowColor
            val users = overview.allUsers
            Calculator.calculateRangeByWeek().mapIndexed { index, pair ->
                val values = users.map { user ->
                    overview.getTotalSpendingByRange(
                        user.id,
                        range = pair
                    ).toFloat()
                }.toFloatArray()
                BarEntry((index + 1).toFloat(), values)
            }.also { values ->
                BarDataSet(values, "").apply {
                    colors = ColorGenerator.generate(users.size).map { context.color(it) }
                    barShadowColor = shadowColor
                    setDrawValues(false)
                }.run {
                    BarData(this).apply { barWidth = 0.15f }
                }.also {
                    binding.groupChart.data = it
                }
            }
        }
    }

    private fun animateProfile() {
        binding.profileParent.forEach { (it as? Animatable)?.playAnimation() }
    }

    private fun animateTotalSpending(overview: OverviewData) {
        val to = overview.getTotalSpendingByType(filterType = HomeFilterType.MONTHLY)
        binding.moneyText.countAnimation(to = to) {
            Converter.moneyFormat(it.toString())
        }

        val cash = overview.getTotalSpendingByPayment(payment = Payment.CASH)
        binding.cashText.countAnimation(to = cash) {
            Converter.moneyToText(it.toLong())
        }

        val card = overview.getTotalSpendingByPayment(payment = Payment.CARD)
        binding.cardText.countAnimation(to = card) {
            Converter.moneyToText(it.toLong())
        }
    }

    private fun clearMoneyText() {
        binding.moneyText.text = context.getString(R.string.zero)
        binding.cashText.text = context.getString(R.string.format_price, "0")
        binding.cardText.text = context.getString(R.string.format_price, "0")
    }

    private fun clearMarker() {
        binding.groupChart.highlightValue(null)
    }

    companion object {

        fun from(parent: ViewGroup): HomeGroupHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = HomeGroupItemBinding.inflate(inflater, parent, false)
            return HomeGroupHolder(binding)
        }
    }
}
