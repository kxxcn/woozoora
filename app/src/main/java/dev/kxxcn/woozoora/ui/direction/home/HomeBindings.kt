package dev.kxxcn.woozoora.ui.direction.home

import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.GridLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.core.text.buildSpannedString
import androidx.core.text.scale
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.vaibhavlakhera.circularprogressview.CircularProgressView
import dev.kxxcn.woozoora.R
import dev.kxxcn.woozoora.common.DURATION_ONE_SECONDS
import dev.kxxcn.woozoora.common.FORMAT_DATE_MONTH_DOT_DAY
import dev.kxxcn.woozoora.common.Payment
import dev.kxxcn.woozoora.common.extension.color
import dev.kxxcn.woozoora.common.extension.countAnimation
import dev.kxxcn.woozoora.common.extension.dpToPx
import dev.kxxcn.woozoora.common.extension.font
import dev.kxxcn.woozoora.domain.model.OverviewData
import dev.kxxcn.woozoora.ui.custom.CategoryPriceView
import dev.kxxcn.woozoora.ui.custom.DailyMarkerView
import dev.kxxcn.woozoora.ui.custom.PaymentView
import dev.kxxcn.woozoora.ui.custom.ProfileView
import dev.kxxcn.woozoora.util.Calculator
import dev.kxxcn.woozoora.util.ColorGenerator
import dev.kxxcn.woozoora.util.Converter
import dev.kxxcn.woozoora.util.RoundedBarChartRenderer
import java.util.*
import kotlin.math.roundToInt

@BindingAdapter("app:overview", "app:filterType")
fun setCountText(
    view: TextView,
    overview: OverviewData?,
    filterType: HomeFilterType,
) {
    overview?.let { data ->
        val money = data.getTotalSpendingByType(data.id, filterType)
        view.countAnimation(to = money) { Converter.moneyFormat(it.toString()) }
    }
}

@BindingAdapter("app:dateRangeText")
fun setDateRangeText(view: TextView, filterType: HomeFilterType) {
    val (startDate, endDate) = Converter.rangeOfHomeFilterType(filterType)
    view.text = view.context.getString(
        R.string.format_date,
        Converter.dateFormat(FORMAT_DATE_MONTH_DOT_DAY, startDate),
        Converter.dateFormat(FORMAT_DATE_MONTH_DOT_DAY, endDate)
    )
}

@BindingAdapter("app:mostSpentText")
fun setMostSpentText(view: TextView, filterType: HomeFilterType) {
    val context = view.context
    val (startDate, endDate) = Converter.rangeOfHomeFilterType(filterType)
    val range = context.getString(
        R.string.format_date,
        Converter.dateFormat(FORMAT_DATE_MONTH_DOT_DAY, startDate),
        Converter.dateFormat(FORMAT_DATE_MONTH_DOT_DAY, endDate)
    )
    when (filterType) {
        HomeFilterType.WEEKLY -> R.string.most_spent_hash_tag_of_week
        else -> R.string.most_spent_hash_tag_of_month
    }.also {
        view.text = context.getString(it, range)
    }
}

@BindingAdapter("app:averageMonth")
fun setAverageMonth(view: TextView, avg: Int) {
    val context = view.context
    val typeface = ResourcesCompat.getFont(context, R.font.font_nexon_regular)
    val args = Converter.moneyFormat(avg.toString())
    val (perWeek, money, using) = context
        .getString(R.string.format_average_month, args)
        .split("\n")
    view.text = buildSpannedString {
        appendLine(perWeek)
        font(typeface) { scale(1.5f) { append(money.substring(0, money.length - 1)) } }
        appendLine(money.substring(money.length - 1))
        append(using)
    }
}

@BindingAdapter("app:countOfMostSpent")
fun setCountOfMostSpentWeekday(view: TextView, overview: OverviewData) {
    val count = overview.getCountOfMostSpentCategory()
    view.text = view.context.getString(
        R.string.format_count_02,
        count,
        overview.totalCountOfTransactions
    )
}

@BindingAdapter("app:mostSpentCategory")
fun setMostSpentCategory(view: TextView, category: String?) {
    category?.let { view.text = it }
}

@BindingAdapter("app:mostSpentPayment")
fun setMostSpentPayment(view: TextView, payment: Payment?) {
    payment?.let { view.text = view.context.getString(it.nameRes) }
}

@BindingAdapter("app:totalTransaction")
fun setTotalTransaction(view: TextView, count: Int) {
    view.text = view.context.getString(R.string.format_total_transaction, count)
}

@BindingAdapter("app:overview")
fun setOverview(view: RecyclerView, overview: OverviewData?) {
    overview?.let {
        val overviews = HomeOverviewAdapter.create(it)
        (view.adapter as? HomeOverviewAdapter)?.submitListAndScroll(overviews)
    }
}

@BindingAdapter("app:overview")
fun setGroup(view: LinearLayout, overview: OverviewData) {
    view.removeAllViews()
    val colors = ColorGenerator.generate().map { view.context.color(it) }
    val users = overview.allUsers
    users.mapIndexed { index, user ->
        val position = index.takeIf { it <= colors.size - 1 } ?: 0
        val color = colors[position]
        val progress = overview.getUsageRateOfBudgetByUser(user.id, HomeFilterType.MONTHLY)
        ProfileView(view.context)
            .apply { bind(user, progress, color) }
            .apply {
                val marginEnd = if (index == users.size - 1) 0 else 20.dpToPx
                layoutParams = LinearLayout.LayoutParams(
                    WRAP_CONTENT,
                    WRAP_CONTENT
                ).apply { setMargins(0, 0, marginEnd, 0) }
            }
    }.forEach {
        view.addView(it)
    }
}

@BindingAdapter("app:ratios")
fun setPayment(view: LinearLayout, ratios: List<Pair<Payment, Float>>) {
    view.removeAllViews()
    Payment.values().map { payment ->
        val ratio = ratios.find { it.first == payment }?.second ?: 0f
        PaymentView(view.context).apply { bind(payment, ratio) }
    }.forEach {
        view.addView(it)
    }
}

@BindingAdapter("app:overview", "app:colors")
fun setCategory(view: GridLayout, overview: OverviewData, colors: List<Int>) {
    view.removeAllViews()
    overview.groupByCategory(overview.id, HomeFilterType.MONTHLY)
        .takeIf { it.isNotEmpty() }
        ?.toList()
        ?.sortedByDescending { group -> group.second.sumBy { it.price } }
        ?.mapIndexed { index, (category, transactions) ->
            CategoryPriceView(view.context)
                .apply {
                    bind(
                        category,
                        colors[index],
                        transactions.sumBy { it.price }
                    )
                }.apply {
                    layoutParams = GridLayout.LayoutParams().apply {
                        columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
                        setMargins(20.dpToPx, 10.dpToPx, 0, 0)
                    }
                }
        }
        ?.forEach { view.addView(it) }
        ?: run { view.isVisible = false }
}

@BindingAdapter("app:chart", "app:filterType")
fun setTransactionChart(
    view: LineChart,
    overview: OverviewData?,
    filterType: HomeFilterType,
) {
    overview?.let {
        val (startDay, endDay) = Calculator.calculateRangeForDays(filterType)

        val values = IntRange(startDay, endDay).map { day ->
            Entry(
                day.toFloat(),
                it.getTotalSpendingForDaily(it.id, day).toFloat(),
                it.getResourceOfCategory(it.id, day)
            )
        }

        val colorOrange = view.context.color(R.color.primaryOrange)
        val colorGrey = view.context.color(R.color.grey02)

        LineDataSet(values, "")
            .apply {
                mode = LineDataSet.Mode.CUBIC_BEZIER
                cubicIntensity = 0.3f
                setDrawFilled(true)
                setDrawCircles(false)
                lineWidth = 1.5f
                color = colorOrange
                fillColor = colorOrange
            }
            .run {
                LineData(this).apply { setDrawValues(false) }
            }
            .also { lineData ->
                view.axisLeft.spaceTop = 0f
                view.axisLeft.isEnabled = false

                view.xAxis.apply {
                    textColor = colorGrey
                    granularity = 1f
                    position = XAxis.XAxisPosition.BOTTOM
                    valueFormatter = object : IndexAxisValueFormatter() {
                        override fun getFormattedValue(value: Float): String {
                            return view.context.getString(R.string.format_day, value.toInt())
                        }
                    }
                    setDrawGridLines(false)
                    setDrawAxisLine(false)
                }

                view.axisRight.apply {
                    spaceTop = 0f
                    gridColor = colorGrey
                    setLabelCount(5, true)
                    setDrawLabels(false)
                    setDrawAxisLine(false)
                }

                view.description.isEnabled = false
                view.legend.isEnabled = false
                view.isDragEnabled = true
                view.data = lineData
                view.marker = DailyMarkerView(
                    view.context,
                    layoutResource = R.layout.daily_marker_view
                ).apply { chartView = view }
                view.setTouchEnabled(true)
                view.setScaleEnabled(false)
                view.setDrawGridBackground(false)
                view.animateY(DURATION_ONE_SECONDS.toInt())
                view.invalidate()
            }

    }
}

@BindingAdapter("app:categoryChart")
fun setCategoryChart(view: PieChart, colorRes: Int) {
    val values = listOf(PieEntry(100f, "", R.color.grey01))

    val (startDate, endDate) = Converter.rangeOfHomeFilterType(HomeFilterType.MONTHLY)
    val centerText = view.context.getString(
        R.string.format_date_with_new_line,
        Converter.dateFormat(FORMAT_DATE_MONTH_DOT_DAY, startDate),
        Converter.dateFormat(FORMAT_DATE_MONTH_DOT_DAY, endDate)
    )
    val typeFace = ResourcesCompat.getFont(view.context, R.font.font_swagger)

    PieDataSet(values, "")
        .apply {
            sliceSpace = 0f
            selectionShift = 0f
            setDrawIcons(false)
            color = colorRes
        }.run {
            PieData(this).apply { setDrawValues(false) }
        }.also {
            view.description.isEnabled = false
            view.legend.isEnabled = false
            view.isRotationEnabled = false
            view.holeRadius = 65f
            view.centerText = centerText
            view.data = it

            view.setHoleColor(view.context.color(R.color.yellow01))
            view.setTransparentCircleAlpha(0)
            view.setCenterTextTypeface(typeFace)
            view.setCenterTextSize(30f)
            view.setCenterTextColor(view.context.color(R.color.categoryCardText))
            view.setDrawEntryLabels(false)
            view.setUsePercentValues(false)
            view.setDrawRoundedSlices(true)
            view.setDrawSlicesUnderHole(false)
        }
}

@BindingAdapter("app:paymentChart")
fun setPaymentChart(view: PieChart, colorRes: Int) {
    val values = listOf(PieEntry(100f, ""))

    PieDataSet(values, "")
        .apply {
            sliceSpace = 0f
            selectionShift = 0f
            setDrawIcons(false)
            color = colorRes
        }.run {
            PieData(this).apply { setDrawValues(false) }
        }.also {
            view.description.isEnabled = false
            view.legend.isEnabled = false
            view.isRotationEnabled = false
            view.isDrawHoleEnabled = false
            view.data = it

            view.setTransparentCircleAlpha(0)

            view.setDrawEntryLabels(false)
            view.setUsePercentValues(false)
        }
}

@BindingAdapter("app:weekdayChart")
fun setWeekdayChart(view: BarChart, overview: OverviewData) {
    val context = view.context
    val weekdayRepo = overview.groupByWeekday(filterType = HomeFilterType.MONTHLY)
    val values = (Calendar.SUNDAY..Calendar.SATURDAY).map { day ->
        val totalPrice = weekdayRepo[day]?.sumBy { it.price } ?: 0
        BarEntry(day.toFloat(), totalPrice.toFloat())
    }

    val colorWhite = context.color(R.color.white01)

    BarDataSet(values, "")
        .apply {
            color = colorWhite
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
                textColor = colorWhite
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

@BindingAdapter("app:overview", "app:paymentProgress")
fun setCircularProgress(
    view: CircularProgressView,
    overview: OverviewData,
    payment: Payment,
) {
    overview.getRatioByPayment(filterType = HomeFilterType.MONTHLY)
        .firstOrNull { it.first == payment }
        ?.let { (_, ratio) ->
            val progress = (ratio * 100).roundToInt()
            view.setProgress(progress, false)
        }
}
