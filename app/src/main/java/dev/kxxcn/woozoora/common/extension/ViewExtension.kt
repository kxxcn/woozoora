package dev.kxxcn.woozoora.common.extension

import android.animation.ValueAnimator
import android.view.View
import android.widget.TextView
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.animation.addListener
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.Chart
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.DataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.renderer.DataRenderer
import dev.kxxcn.woozoora.R
import dev.kxxcn.woozoora.common.DURATION_ONE_SECONDS
import dev.kxxcn.woozoora.util.Calculator

inline fun MotionLayout.doOnTransition(
    crossinline onTransitionTrigger: (
        p0: MotionLayout?,
        p1: Int,
        p2: Boolean,
        p3: Float,
    ) -> Unit = { _, _, _, _ -> },
    crossinline onTransitionStarted: (
        p0: MotionLayout?,
        p1: Int,
        p2: Int,
    ) -> Unit = { _, _, _ -> },
    crossinline onTransitionChange: (
        p0: MotionLayout?,
        p1: Int,
        p2: Int,
        p3: Float,
    ) -> Unit = { _, _, _, _ -> },
    crossinline onTransitionCompleted: (
        p0: MotionLayout?,
        p1: Int,
    ) -> Unit = { _, _ -> },
): MotionLayout.TransitionListener {
    val listener = object : MotionLayout.TransitionListener {
        override fun onTransitionTrigger(
            motionLayout: MotionLayout?,
            triggerId: Int,
            positive: Boolean,
            progress: Float,
        ) {
            onTransitionTrigger.invoke(motionLayout, triggerId, positive, progress)
        }

        override fun onTransitionStarted(motionLayout: MotionLayout?, startId: Int, endId: Int) {
            onTransitionStarted.invoke(motionLayout, startId, endId)
        }

        override fun onTransitionChange(
            motionLayout: MotionLayout?,
            startId: Int,
            endId: Int,
            progress: Float,
        ) {
            onTransitionChange.invoke(motionLayout, startId, endId, progress)
        }

        override fun onTransitionCompleted(motionLayout: MotionLayout?, currentId: Int) {
            onTransitionCompleted.invoke(motionLayout, currentId)
        }
    }
    setTransitionListener(listener)
    return listener
}

fun MotionLayout.isTransitionEnabled(beginId: Int, endId: Int): Boolean {
    return startState != beginId && endState != endId && (currentState == -1 || currentState == beginId)
}

fun TextView.countAnimation(
    from: Int = 0,
    to: Int,
    transform: (Int) -> String? = { it.toString() },
) {
    ValueAnimator.ofInt(from, to).apply {
        addUpdateListener { animator ->
            val value = animator.animatedValue as Int
            text = transform.invoke(value)
        }
        addListener(onEnd = { removeAllUpdateListeners() })
        duration = DURATION_ONE_SECONDS
    }.also {
        it.start()
    }
}

fun TextView.countAnimation(
    from: Float = 0f,
    to: Float,
    transform: (Float) -> String? = { it.toString() },
) {
    ValueAnimator.ofFloat(from, to).apply {
        addUpdateListener { animator ->
            val value = animator.animatedValue as Float
            text = transform.invoke(value)
        }
        addListener(onEnd = { removeAllUpdateListeners() })
        duration = DURATION_ONE_SECONDS
    }.also {
        it.start()
    }
}

@Suppress("UNCHECKED_CAST")
fun <T : DataSet<*>> Chart<*>.updateValues(
    easing: Easing.EasingFunction = Easing.EaseInOutQuad,
    durationMillis: Int = DURATION_ONE_SECONDS.toInt(),
    block: T.() -> Unit,
) {
    (data?.getDataSetByIndex(0) as? T)?.let {
        block(it)
        data.notifyDataChanged()
        notifyDataSetChanged()
        animateY(durationMillis, easing)
    }
}

fun BarChart.setWeeklyChart(
    barColorRes: Int,
    shadowColorRes: Int,
    highlightColorRes: Int? = null,
    maximum: Float? = null,
    isDrawGridLines: Boolean = false,
    dataRenderer: DataRenderer? = null,
    marker: MarkerView? = null,
) {
    val values = Calculator.calculateRangeByWeek().mapIndexed { index, _ ->
        BarEntry((index + 1).toFloat(), floatArrayOf(0f))
    }

    val barColor = context.color(barColorRes)
    val shadowColor = context.color(shadowColorRes)
    val highlightColor = highlightColorRes?.let { context.color(it) }

    BarDataSet(values, "")
        .apply {
            color = barColor
            barShadowColor = shadowColor
            highlightColor?.let { highLightColor = it }
            setDrawValues(false)
        }
        .run {
            BarData(this).apply { barWidth = 0.15f }
        }
        .also {
            with(this@setWeeklyChart) {
                this.axisLeft.apply {
                    spaceTop = 0f
                    axisMinimum = 0f
                    maximum?.let { axisMaximum = it }
                    isEnabled = false
                }

                this.xAxis.apply {
                    textColor = barColor
                    position = XAxis.XAxisPosition.BOTTOM
                    labelCount = values.size
                    granularity = 1f
                    valueFormatter = object : IndexAxisValueFormatter() {
                        override fun getFormattedValue(value: Float): String {
                            return context.getString(R.string.format_week, value.toInt())
                        }
                    }
                    setDrawGridLines(false)
                    setDrawAxisLine(false)
                }

                this.axisRight.apply {
                    spaceTop = 0f
                    axisMinimum = 0f
                    maximum?.let { axisMaximum = it }
                    gridColor = context.color(R.color.grey02)
                    setLabelCount(values.size, true)
                    setDrawLabels(false)
                    setDrawAxisLine(false)
                    setDrawGridLines(isDrawGridLines)
                }

                this.description.isEnabled = false
                this.legend.isEnabled = false
                this.isDoubleTapToZoomEnabled = false
                this.isDragEnabled = false
                this.marker = marker
                this.data = it
                if (this.renderer != dataRenderer) {
                    this.renderer = dataRenderer
                }

                this.setDrawMarkers(marker != null)
                this.setDrawBarShadow(true)
            }
        }
}

fun RecyclerView.smoothScrollToCenter(position: Int) {
    val smoothScroller = object : LinearSmoothScroller(context) {
        override fun calculateDtToFit(
            viewStart: Int,
            viewEnd: Int,
            boxStart: Int,
            boxEnd: Int,
            snapPreference: Int,
        ): Int {
            return (boxEnd / 2) - viewStart - 40.dpToPx
        }
    }
    smoothScroller.targetPosition = position
    layoutManager?.startSmoothScroll(smoothScroller)
}

fun RecyclerView.smoothScrollToTop() {
    val smoothScroller = object : LinearSmoothScroller(context) {
        override fun getVerticalSnapPreference(): Int {
            return SNAP_TO_START
        }
    }
    smoothScroller.targetPosition = 0
    layoutManager?.startSmoothScroll(smoothScroller)
}

fun RecyclerView.smoothScrollForceToPosition(position: Int) {
    val smoothScroller = object : LinearSmoothScroller(context) {
        override fun getVerticalSnapPreference(): Int {
            return SNAP_TO_START
        }
    }
    smoothScroller.targetPosition = position
    layoutManager?.startSmoothScroll(smoothScroller)
}

fun RecyclerView.setItemDecoration(decor: RecyclerView.ItemDecoration) {
    if (itemDecorationCount == 0) {
        addItemDecoration(decor)
    }
}

fun View.asTextView() = this as TextView
