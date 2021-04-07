package org.wordpress.android.ui.comments

import android.R.attr
import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import androidx.recyclerview.widget.RecyclerView.State
import org.wordpress.android.R.dimen

class CommentListItemDecoration(context: Context) : ItemDecoration() {
    private val divider: Drawable?
    private val bounds = Rect()
    private var dividerStartOffset = 0

    override fun onDraw(canvas: Canvas, parent: RecyclerView, state: State) {
        if (parent.layoutManager == null || divider == null) {
            return
        }
        canvas.save()
        val left: Int
        val right: Int
        if (parent.clipToPadding) {
            left = parent.paddingLeft
            right = parent.width - parent.paddingRight
            canvas.clipRect(
                    left, parent.paddingTop, right,
                    parent.height - parent.paddingBottom
            )
        } else {
            left = 0
            right = parent.width
        }
        val childCount = parent.childCount
        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)
            val viewHolder = parent.getChildViewHolder(child)
            if (viewHolder !is CommentSubHeaderViewHolder) {
                parent.getDecoratedBoundsWithMargins(child, bounds)
                val bottom = bounds.bottom + Math.round(child.translationY)
                val top = bottom - divider.intrinsicHeight
                divider.setBounds(left + dividerStartOffset, top, right, bottom)
                divider.draw(canvas)
            }
        }
        canvas.restore()
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: State
    ) {
        if (divider == null) {
            outRect[0, 0, 0] = 0
            return
        }
        outRect[0, 0, 0] = divider.intrinsicHeight
    }

    companion object {
        private val ATTRS = intArrayOf(attr.listDivider)
    }

    init {
        val a = context.obtainStyledAttributes(ATTRS)
        divider = a.getDrawable(0)
        a.recycle()
        dividerStartOffset = context.resources.getDimensionPixelOffset(dimen.divider_start_offset)
    }
}
