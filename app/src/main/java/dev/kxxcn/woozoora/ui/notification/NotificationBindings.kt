package dev.kxxcn.woozoora.ui.notification

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import dev.kxxcn.woozoora.R
import dev.kxxcn.woozoora.common.extension.color
import dev.kxxcn.woozoora.common.extension.dpToPx
import dev.kxxcn.woozoora.common.extension.weekday
import dev.kxxcn.woozoora.domain.model.StatisticData
import dev.kxxcn.woozoora.ui.notification.item.NotificationItem
import dev.kxxcn.woozoora.util.Converter
import dev.kxxcn.woozoora.util.RoundedBarChartRenderer
import java.util.*

@BindingAdapter("app:notifications")
fun setNotificationList(view: RecyclerView, notifications: List<NotificationItem>?) {
    notifications?.let {
        val list =
            if (it.isEmpty()) listOf(NotificationItem(NotificationAdapter.TYPE_EMPTY)) else it
        (view.adapter as? NotificationAdapter)?.submitList(list)
    }
}

@BindingAdapter("app:statisticChart")
fun setStatisticChart(view: BarChart, statistic: StatisticData) {
    val context = view.context
    val transactions = statistic.transactions
    val weekdayRepo = transactions.groupBy { it.date.weekday }
    val startDay = statistic.startDate.weekday
    val sunday = 8
    val start = if (startDay == Calendar.SUNDAY) sunday else startDay

    val values = (start..sunday).map { day ->
        val index = if (day == sunday) Calendar.SUNDAY else day
        val totalPrice = weekdayRepo[index]?.sumBy { it.price } ?: 0
        BarEntry(day.toFloat(), totalPrice.toFloat())
    }

    val colorBlue = context.color(R.color.primaryBlue)

    BarDataSet(values, "")
        .apply {
            color = colorBlue
            setDrawValues(false)
        }.run {
            BarData(this).apply { barWidth = 0.3f }
        }.also {
            view.description.isEnabled = false
            view.legend.isEnabled = false
            view.isDragEnabled = false

            view.axisLeft.spaceTop = 0f
            view.axisRight.spaceTop = 0f
            view.axisLeft.isEnabled = false
            view.axisRight.isEnabled = false

            view.xAxis.apply {
                granularity = 1f
                labelCount = values.size
                textColor = colorBlue
                position = XAxis.XAxisPosition.BOTTOM
                valueFormatter = object : IndexAxisValueFormatter() {
                    override fun getFormattedValue(value: Float): String {
                        val resource = Converter.weekdayToResource(value)
                        return context.getString(resource)
                    }
                }
                setDrawAxisLine(false)
                setDrawGridLines(false)
            }
            view.renderer = RoundedBarChartRenderer(
                view,
                view.animator,
                view.viewPortHandler
            ).apply { radius = 3.dpToPx.toFloat() }
            view.data = it

            view.setPinchZoom(false)
            view.setTouchEnabled(false)
        }
}
