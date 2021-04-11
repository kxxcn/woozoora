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
import dev.kxxcn.woozoora.databinding.HomeCategoryItemBinding
import dev.kxxcn.woozoora.domain.model.OverviewData
import dev.kxxcn.woozoora.ui.base.AnimatableHolder
import dev.kxxcn.woozoora.ui.direction.home.HomeFilterType
import dev.kxxcn.woozoora.ui.direction.home.HomeViewModel

class HomeCategoryHolder(
    private val binding: HomeCategoryItemBinding
) : AnimatableHolder(binding) {

    override fun animate() {
        binding.overview?.let {
            animateChart(it)
            animateCategoryView()
        }
    }

    fun bind(overview: OverviewData, viewModel: HomeViewModel) {
        with(binding) {
            this.lifecycleOwner = this@HomeCategoryHolder
            this.overview = overview
            this.viewModel = viewModel
            this.executePendingBindings()
        }
    }

    private fun animateChart(overview: OverviewData) {
        overview.groupByCategory(overview.id, HomeFilterType.MONTHLY)
            .map { (category, transactions) ->
                PieEntry(
                    transactions.sumBy { it.price }.toFloat(),
                    context.getString(category.nameRes),
                    category.colorRes
                )
            }
            .takeIf { it.isNotEmpty() }
            ?.let {
                binding.categoryChart.updateValues<PieDataSet>(Easing.EaseInOutCirc) {
                    this.values = it
                    this.colors = it.map { context.color(it.data as Int) }
                }
            }
    }

    private fun animateCategoryView() {
        binding.categoryNameParent.forEach { (it as? Animatable)?.playAnimation() }
    }

    companion object {

        fun from(parent: ViewGroup): HomeCategoryHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = HomeCategoryItemBinding.inflate(inflater, parent, false)
            return HomeCategoryHolder(binding)
        }
    }
}
