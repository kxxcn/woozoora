package dev.kxxcn.woozoora.ui.direction.home.holder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.forEach
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import dev.kxxcn.woozoora.common.base.Animatable
import dev.kxxcn.woozoora.common.extension.color
import dev.kxxcn.woozoora.common.extension.updateValues
import dev.kxxcn.woozoora.databinding.HomePaymentItemBinding
import dev.kxxcn.woozoora.domain.model.OverviewData
import dev.kxxcn.woozoora.ui.base.AnimatableHolder
import dev.kxxcn.woozoora.ui.direction.home.HomeFilterType
import dev.kxxcn.woozoora.ui.direction.home.HomeViewModel

class HomePaymentHolder(
    private val binding: HomePaymentItemBinding
) : AnimatableHolder(binding) {

    override fun animate() {
        binding.overview?.let {
            animateChart(it)
            animatePaymentView()
        }
    }

    fun bind(overview: OverviewData, viewModel: HomeViewModel) {
        with(binding) {
            this.lifecycleOwner = this@HomePaymentHolder
            this.overview = overview
            this.viewModel = viewModel
            this.executePendingBindings()
        }
    }

    private fun animateChart(overview: OverviewData) {
        overview.getRatioByPayment(overview.id, HomeFilterType.MONTHLY)
            .map { (payment, ratio) ->
                PieEntry(
                    ratio,
                    context.getString(payment.nameRes),
                    payment.colorRes
                )
            }.takeIf { it.isNotEmpty() }
            ?.let {
                binding.paymentChart.updateValues<PieDataSet>(Easing.EaseInOutCirc) {
                    this.values = it
                    this.colors = it.map { context.color(it.data as Int) }
                }
            }
    }

    private fun animatePaymentView() {
        binding.paymentRatioParent.forEach { (it as? Animatable)?.playAnimation() }
    }

    companion object {

        fun from(parent: ViewGroup): HomePaymentHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = HomePaymentItemBinding.inflate(inflater, parent, false)
            return HomePaymentHolder(binding)
        }
    }
}
