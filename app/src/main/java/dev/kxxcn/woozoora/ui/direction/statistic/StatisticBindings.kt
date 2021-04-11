package dev.kxxcn.woozoora.ui.direction.statistic

import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.faltenreich.skeletonlayout.Skeleton
import com.robinhood.spark.SparkAdapter
import com.robinhood.spark.SparkView
import dev.kxxcn.woozoora.R
import dev.kxxcn.woozoora.common.DURATION_QUARTER_SECONDS
import dev.kxxcn.woozoora.common.extension.setItemDecoration
import dev.kxxcn.woozoora.domain.model.OverviewData
import dev.kxxcn.woozoora.ui.direction.statistic.category.ChildCategoryAdapter
import dev.kxxcn.woozoora.ui.direction.statistic.item.CategoryItem

@BindingAdapter("app:overview", "app:userId", "app:month")
fun setStatisticList(
    view: RecyclerView,
    overview: OverviewData?,
    userId: String?,
    month: Int,
) {
    if (overview == null || userId == null) return
    with(view) {
        val skeleton = view.getTag(R.id.tag_skeleton_statistic_list) as? Skeleton
        if (skeleton != null) {
            skeleton.showOriginal()
            view.setTag(R.id.tag_skeleton_statistic_list, null)
        }
        val adapter = adapter as? StatisticAdapter
        val newList = StatisticAdapter.create(overview, userId, month)
        setItemDecoration(StatisticSpacingDecoration())
        adapter?.submitListAndScroll(newList)
    }
}

@BindingAdapter("app:category")
fun setCategoryList(view: RecyclerView, categoryItems: List<CategoryItem>?) {
    categoryItems
        ?.sortedByDescending { it.totalPricing }
        ?.let { (view.adapter as? ChildCategoryAdapter)?.submitList(it) }
}

@BindingAdapter("app:spark")
fun setSparkView(view: SparkView, data: ArrayList<Int>) {
    with(view) {
        isInvisible = true
        adapter = object : SparkAdapter() {
            override fun getCount(): Int {
                return data.size
            }

            override fun getItem(index: Int): Any {
                return data[index]
            }

            override fun getY(index: Int): Float {
                return data[index].toFloat()
            }
        }
        postDelayed({
            adapter.notifyDataSetChanged()
            isVisible = true
        }, DURATION_QUARTER_SECONDS)
    }
}
