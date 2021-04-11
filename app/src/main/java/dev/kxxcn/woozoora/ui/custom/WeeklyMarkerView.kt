package dev.kxxcn.woozoora.ui.custom

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import dev.kxxcn.woozoora.R
import dev.kxxcn.woozoora.common.extension.displayWidth
import dev.kxxcn.woozoora.common.extension.dpToPx
import dev.kxxcn.woozoora.util.Converter

class WeeklyMarkerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    layoutResource: Int,
) : MarkerView(context, layoutResource) {

    private val markerRoot: ConstraintLayout = findViewById(R.id.marker_root)
    private val moneyText: TextView = findViewById(R.id.money_text)

    var isHorizontalBarChart = false

    override fun draw(canvas: Canvas, posx: Float, posy: Float) {
        val dx = if (isLocatedOnTheLeftSide(posx)) {
            posx
        } else {
            posx - width.toFloat()
        }

        val dy = if (isLocatedOnTheTopSide(posy, canvas.height)) {
            posy
        } else {
            posy - height
        }

        canvas.translate(dx, dy)
        draw(canvas)
    }

    override fun refreshContent(e: Entry?, highlight: Highlight?) {
        val index = highlight?.stackIndex
            ?.takeIf { it > -1 }
            ?: 0
        val entry = e as? BarEntry ?: return
        val values = entry.yVals ?: floatArrayOf(entry.y)
        val value = values[index]

        val moneyValue = (entry.data as? Long)
            ?.let { (it * value / 100).toInt() }
            ?: value.toInt()

        markerRoot.isVisible = value != 0f
        val money = Converter.moneyFormat(moneyValue.toString())
        moneyText.text = context.getString(R.string.format_price, money)
        super.refreshContent(e, highlight)
    }

    private fun isLocatedOnTheLeftSide(x: Float): Boolean {
        return if (isHorizontalBarChart) {
            x + width < (context.displayWidth - 40.dpToPx) * 0.6
        } else {
            x < (context.displayWidth - 40.dpToPx) / 2
        }
    }

    private fun isLocatedOnTheTopSide(y: Float, height: Int): Boolean {
        return y < height / 2
    }
}
