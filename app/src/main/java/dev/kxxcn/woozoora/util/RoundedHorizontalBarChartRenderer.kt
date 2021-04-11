package dev.kxxcn.woozoora.util

import android.graphics.Canvas
import android.graphics.RectF
import com.github.mikephil.charting.animation.ChartAnimator
import com.github.mikephil.charting.interfaces.dataprovider.BarDataProvider
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.renderer.HorizontalBarChartRenderer
import com.github.mikephil.charting.utils.ViewPortHandler
import dev.kxxcn.woozoora.common.extension.dpToPx

class RoundedHorizontalBarChartRenderer(
    chart: BarDataProvider?,
    animator: ChartAnimator?,
    viewPortHandler: ViewPortHandler?,
) : HorizontalBarChartRenderer(chart, animator, viewPortHandler) {

    var radius = 5.dpToPx.toFloat()

    override fun drawDataSet(c: Canvas, dataSet: IBarDataSet, index: Int) {
        val margin = if (mChart.barData.entryCount == 4) 15.dpToPx else 10.dpToPx

        mShadowPaint.color = dataSet.barShadowColor

        val trans = mChart.getTransformer(dataSet.axisDependency)
        val phaseX = mAnimator.phaseX
        val phaseY = mAnimator.phaseY

        val buffer = mBarBuffers[index]
        buffer.setPhases(phaseX, phaseY)
        buffer.setDataSet(index)
        buffer.setInverted(mChart.isInverted(dataSet.axisDependency))
        buffer.feed(dataSet)

        trans.pointValuesToPixel(buffer.buffer)

        var j = 0
        while (j < buffer.size()) {
            if (!mViewPortHandler.isInBoundsTop(buffer.buffer[j + 3])) break
            if (!mViewPortHandler.isInBoundsBottom(buffer.buffer[j + 1])) {
                j += 4
                continue
            }

            val color = dataSet.getColor(j / 4)
            mRenderPaint.color = color

            if (mChart.isDrawBarShadowEnabled) {
                c.drawRoundRect(
                    RectF(
                        mViewPortHandler.contentLeft(),
                        buffer.buffer[j + 1] + margin,
                        mViewPortHandler.contentRight(),
                        buffer.buffer[j + 3] - margin
                    ), radius, radius, mShadowPaint
                )
            }

            c.drawRoundRect(
                RectF(
                    buffer.buffer[j],
                    buffer.buffer[j + 1] + margin,
                    buffer.buffer[j + 2],
                    buffer.buffer[j + 3] - margin
                ), radius,
                radius,
                mRenderPaint
            )

            j += 4
        }
    }
}
