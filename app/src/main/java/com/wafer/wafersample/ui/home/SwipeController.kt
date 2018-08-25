package com.wafer.wafersample.ui.home

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.RectF
import android.graphics.drawable.ColorDrawable
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper.*
import android.view.MotionEvent
import com.wafer.wafersample.R


internal class SwipeController(private var buttonsActions: SwipeControllerActions, context: Context) : Callback() {

    internal enum class ButtonsState {
        GONE,
        VISIBLE
    }

    private var swipeBack = false

    private var lastX = 0f

    private var buttonShowedState = ButtonsState.GONE

    private var buttonInstance: RectF? = RectF()

    private var currentItemViewHolder: RecyclerView.ViewHolder? = null

    private val background = ColorDrawable(ContextCompat.getColor(context, R.color.purple))

    private val deleteIcon = ContextCompat.getDrawable(context, R.drawable.ic_delete)

    private val intrinsicWidth = deleteIcon?.intrinsicWidth ?: 0

    private val intrinsicHeight = deleteIcon?.intrinsicHeight ?: 0

    private val iconMargin = context.resources.getDimensionPixelSize(R.dimen.icon_margin)

    private val totalIconWidth = (intrinsicWidth + (2 * iconMargin)).toFloat()

    private var newX = 0f

    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
        return makeMovementFlags(0, LEFT)
    }

    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        buttonsActions.onItemSwiped(viewHolder.adapterPosition)
    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        super.clearView(recyclerView, viewHolder)
        if (buttonShowedState != ButtonsState.GONE) {
            lastX = -totalIconWidth
        } else {
            lastX = 0f
            currentItemViewHolder = null
        }
    }

    override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
        newX = lastX + dX
        if (actionState == ACTION_STATE_SWIPE) {
            if (buttonShowedState != ButtonsState.GONE) {
                if (buttonShowedState == ButtonsState.VISIBLE) newX = Math.min(dX, -totalIconWidth)
            } else {
                setTouchListener(c, recyclerView, viewHolder, dX)
            }
        }

        newX = Math.min(0f, newX)

        super.onChildDraw(c, recyclerView, viewHolder, newX, dY, actionState, isCurrentlyActive)

        if (viewHolder != currentItemViewHolder) {
            currentItemViewHolder?.itemView?.x = 0f
            currentItemViewHolder = viewHolder
        }
    }


    @SuppressLint("ClickableViewAccessibility")
    private fun setTouchListener(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float) {
        recyclerView.setOnTouchListener { v, event ->
            swipeBack = event.action == MotionEvent.ACTION_CANCEL || event.action == MotionEvent.ACTION_UP
            if (swipeBack) {
                if (lastX + dX < -totalIconWidth) {
                    buttonShowedState = ButtonsState.VISIBLE
                }
            } else {
                buttonShowedState = ButtonsState.GONE
            }

            buttonInstance?.let {
                if (event.action == MotionEvent.ACTION_UP && buttonShowedState == ButtonsState.VISIBLE && it.contains(event.x, event.y)) {
                    buttonsActions.onButtonClicked(viewHolder.adapterPosition)
                }
            }
            false
        }
    }

    fun onDraw(c: Canvas) {
        currentItemViewHolder?.let {

            val itemView = it.itemView
            val itemHeight = itemView.bottom - itemView.top

            background.setBounds(itemView.right + newX.toInt(), itemView.top, itemView.right, itemView.bottom)

            background.draw(c)

            val deleteIconMargin = (itemHeight - intrinsicHeight) / 2
            val deleteIconTop = itemView.top + deleteIconMargin
            val deleteIconLeft = itemView.right - iconMargin - intrinsicWidth
            val deleteIconRight = itemView.right - iconMargin
            val deleteIconBottom = deleteIconTop + intrinsicHeight

            deleteIcon?.setBounds(deleteIconLeft, deleteIconTop, deleteIconRight, deleteIconBottom)

            buttonInstance?.apply {
                left = itemView.right.toFloat() - totalIconWidth
                top = itemView.top.toFloat()
                right = itemView.right.toFloat()
                bottom = itemView.bottom.toFloat()
            }
            deleteIcon?.draw(c)
        }

    }

    fun reset() {
        currentItemViewHolder?.let {
            it.itemView.x = 0f
        }
    }
}