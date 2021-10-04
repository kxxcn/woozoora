package dev.kxxcn.woozoora.ui.custom

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.widget.TextView
import androidx.core.view.isVisible
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import dev.kxxcn.woozoora.R
import dev.kxxcn.woozoora.common.extension.displayWidth
import dev.kxxcn.woozoora.common.extension.dpToPx
import dev.kxxcn.woozoora.util.Converter

class DailyMarkerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    layoutResource: Int,
) : MarkerView(context, layoutResource) {

    private val dayText: TextView = findViewById(R.id.day_text)
    private val moneyText: TextView = findViewById(R.id.money_text)
    private val categoryText: TextView = findViewById(R.id.category_text)

    override fun draw(canvas: Canvas, posx: Float, posy: Float) {
        val dx = when {
            isDisplayedOffTheRightSideOfTheScreen(posx) -> posx - width + 15.dpToPx
            isDisplayedOffTheLeftSideOfTheScreen(posx) -> 0f
            else -> posx - (width / 2).toFloat()
        }
        val dy = when {
            isDisplayedOffTheTopSideOfTheScreen(posy) -> posy
            else -> posy - height - 5.dpToPx
        }
        canvas.translate(dx, dy)
        draw(canvas)
    }

    override fun refreshContent(e: Entry?, highlight: Highlight?) {
        dayText.text = context.getString(R.string.format_day, e?.x?.toInt())
        moneyText.text =
            context.getString(
                R.string.format_price,
                Converter.moneyFormat(e?.y?.toInt().toString())
            )

        val category = e?.data
            ?.let { convertCategoryText(it as List<*>) }
            .also { categoryText.text = it }
        categoryText.isVisible = category != null

        super.refreshContent(e, highlight)
    }

    private fun isDisplayedOffTheRightSideOfTheScreen(x: Float): Boolean {
        val chartWidth = context.displayWidth - 40.dpToPx
        return chartWidth - x - width < width
    }

    private fun isDisplayedOffTheLeftSideOfTheScreen(x: Float): Boolean {
        return x < width
    }

    private fun isDisplayedOffTheTopSideOfTheScreen(y: Float): Boolean {
        return y < height
    }

    private fun convertCategoryText(resources: List<*>): String? {
        return if (resources.isEmpty()) {
            null
        } else {
            val size = resources.size
            val name = resources.first() as String
            if (size == 1) {
                name
            } else {
                context.getString(
                    R.string.format_marker_category,
                    name,
                    size - 1
                )
            }
        }
    }
}
