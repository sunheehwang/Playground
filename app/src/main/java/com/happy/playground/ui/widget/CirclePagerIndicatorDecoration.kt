package com.happy.playground.ui.widget

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.happy.playground.util.extensions.dp

class CirclePagerIndicatorDecoration(private val colorActive: Int = Color.BLACK,
                                     private val colorInactive: Int = Color.GRAY,
                                     mIndicatorTopPaddingDp: Int = 20,
                                     mIndicatorItemRadiusDp: Float = 2.5f,
                                     mIndicatorItemPaddingDp: Float = mIndicatorItemRadiusDp * 2
) : RecyclerView.ItemDecoration() {




    /**
     * Height of the space the indicator takes up at the bottom of the view.
     */
    private val mIndicatorTopPadding: Int = mIndicatorTopPaddingDp.dp

    private val mIndicatorItemRadius: Float = mIndicatorItemRadiusDp.dp

    private val mIndicatorItemPadding: Float = mIndicatorItemPaddingDp.dp
    /**
     * Indicator width.
     */
    private val mIndicatorItemLength: Float

    /**
     * Some more natural animation interpolation
     */
    private val mInterpolator = AccelerateDecelerateInterpolator()

    private val mPaint = Paint()

    init {
        this.mIndicatorItemLength = mIndicatorItemRadius * 2
        with(mPaint) {
            style = Paint.Style.FILL
            isAntiAlias = true
        }
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDrawOver(c, parent, state)

        val itemCount = parent.adapter?.itemCount?:0

        // center horizontally, calculate width and subtract half from center
        val totalLength = mIndicatorItemLength * itemCount
        val paddingBetweenItems = Math.max(0, itemCount - 1) * mIndicatorItemPadding
        val indicatorTotalWidth = totalLength + paddingBetweenItems
        val indicatorStartX = (parent.width - indicatorTotalWidth) / 2f

        // center vertically in the allotted space
        val indicatorPosY: Float = parent.height - (mIndicatorItemRadius)

        drawInactiveIndicators(c, indicatorStartX, indicatorPosY, itemCount)


        // find active page (which should be highlighted)
        val layoutManager = parent.layoutManager as LinearLayoutManager
        val activePosition = layoutManager.findFirstVisibleItemPosition()
        if (activePosition == RecyclerView.NO_POSITION) {
            return
        }

        // find offset of active page (if the user is scrolling)
        val activeChild = layoutManager.findViewByPosition(activePosition)
        val left = activeChild?.left?:0
        val width = activeChild?.width?:0

        // on swipe the active item will be positioned from [-width, 0]
        // interpolate offset for smooth animation
        val progress = mInterpolator.getInterpolation(left * -1 / width.toFloat())

        drawHighlights(c, indicatorStartX, indicatorPosY, activePosition, progress, itemCount)
    }

    private fun drawInactiveIndicators(c: Canvas, indicatorStartX: Float, indicatorPosY: Float, itemCount: Int) {
        mPaint.color = colorInactive

        // width of item indicator including padding
        val itemWidth = mIndicatorItemLength + mIndicatorItemPadding

        var start = indicatorStartX
        for (i in 0 until itemCount) {
            c.drawCircle(start, indicatorPosY, mIndicatorItemRadius, mPaint)
            start += itemWidth
        }
    }

    private fun drawHighlights(c: Canvas, indicatorStartX: Float, indicatorPosY: Float,
                               highlightPosition: Int, progress: Float, itemCount: Int) {
        mPaint.color = colorActive

        // width of item indicator including padding
        val itemWidth = mIndicatorItemLength + mIndicatorItemPadding

        if (progress == 0f) {
            // no swipe, draw a normal indicator
            val highlightStart = indicatorStartX + itemWidth * highlightPosition

            c.drawCircle(highlightStart, indicatorPosY, mIndicatorItemRadius, mPaint)

        } else {
            val highlightStart = indicatorStartX + itemWidth * highlightPosition
            // calculate partial highlight
            val partialLength = mIndicatorItemLength * progress + mIndicatorItemPadding * progress

            c.drawCircle(highlightStart + partialLength, indicatorPosY, mIndicatorItemRadius, mPaint)
        }
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.bottom = mIndicatorTopPadding + mIndicatorItemRadius.toInt()
    }
}