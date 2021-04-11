package dev.kxxcn.woozoora.util

import android.graphics.Canvas
import android.graphics.RectF
import com.github.mikephil.charting.animation.ChartAnimator
import com.github.mikephil.charting.interfaces.dataprovider.BarDataProvider
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.renderer.BarChartRenderer
import com.github.mikephil.charting.utils.ViewPortHandler

class RoundedBarChartRenderer(
    chart: BarDataProvider?,
    animator: ChartAnimator?,
    viewPortHandler: ViewPortHandler?
) : BarChartRenderer(chart, animator, viewPortHandler) {

    var radius = 5f

    override fun drawDataSet(c: Canvas, dataSet: IBarDataSet, index: Int) {
        mShadowPaint.color = dataSet.barShadowColor

        val trans = mChart.getTransformer(dataSet.axisDependency)
        val phaseX = mAnimator.phaseX
        val phaseY = mAnimator.phaseY

        val buffer = mBarBuffers[index]
        buffer.setPhases(phaseX, phaseY)
        buffer.setDataSet(index)
        buffer.setBarWidth(mChart.barData.barWidth)
        buffer.setInverted(mChart.isInverted(dataSet.axisDependency))
        buffer.feed(dataSet)
        trans.pointValuesToPixel(buffer.buffer)

        if (dataSet.colors.size > 1) {
            var j = 0
            while (j < buffer.size()) {
                if (!mViewPortHandler.isInBoundsLeft(buffer.buffer[j + 2])) {
                    j += 4
                    continue
                }
                if (!mViewPortHandler.isInBoundsRight(buffer.buffer[j])) break
                if (mChart.isDrawBarShadowEnabled) {
                    if (radius > 0) c.drawRoundRect(
                        RectF(
                            buffer.buffer[j], mViewPortHandler.contentTop(),
                            buffer.buffer[j + 2],
                            mViewPortHandler.contentBottom()
                        ), radius, radius, mShadowPaint
                    ) else c.drawRect(
                        buffer.buffer[j], mViewPortHandler.contentTop(),
                        buffer.buffer[j + 2],
                        mViewPortHandler.contentBottom(), mShadowPaint
                    )
                }

                mRenderPaint.color = dataSet.getColor(j / 4)
                if (radius > 0) c.drawRoundRect(
                    RectF(
                        buffer.buffer[j], buffer.buffer[j + 1], buffer.buffer[j + 2],
                        buffer.buffer[j + 3]
                    ), radius, radius, mRenderPaint
                ) else c.drawRect(
                    buffer.buffer[j], buffer.buffer[j + 1], buffer.buffer[j + 2],
                    buffer.buffer[j + 3], mRenderPaint
                )
                j += 4
            }
        } else {
            mRenderPaint.color = dataSet.color
            var j = 0
            while (j < buffer.size()) {
                if (!mViewPortHandler.isInBoundsLeft(buffer.buffer[j + 2])) {
                    j += 4
                    continue
                }
                if (!mViewPortHandler.isInBoundsRight(buffer.buffer[j])) break
                if (mChart.isDrawBarShadowEnabled) {
                    if (radius > 0) c.drawRoundRect(
                        RectF(
                            buffer.buffer[j], mViewPortHandler.contentTop(),
                            buffer.buffer[j + 2],
                            mViewPortHandler.contentBottom()
                        ), radius, radius, mShadowPaint
                    ) else c.drawRect(
                        buffer.buffer[j], buffer.buffer[j + 1], buffer.buffer[j + 2],
                        buffer.buffer[j + 3], mRenderPaint
                    )
                }
                if (radius > 0) c.drawRoundRect(
                    RectF(
                        buffer.buffer[j], buffer.buffer[j + 1], buffer.buffer[j + 2],
                        buffer.buffer[j + 3]
                    ), radius, radius, mRenderPaint
                ) else c.drawRect(
                    buffer.buffer[j], buffer.buffer[j + 1], buffer.buffer[j + 2],
                    buffer.buffer[j + 3], mRenderPaint
                )
                j += 4
            }
        }
    }
}
