package com.jcy.ch24_subwayapp.presentation.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import com.jcy.ch24_subwayapp.extension.dip

class Badge constructor(
    context: Context,
    attrs: AttributeSet? = null
) : AppCompatTextView(context, attrs) {

    var badgeColor = Color.LTGRAY
        set(value) {
            field = value
            invalidate()
        }

    private val paint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)

    init {
        val verticalPadding = dip(4f)
        val horizontalPadding = dip(8f)
        setPadding(
            horizontalPadding,
            verticalPadding,
            horizontalPadding,
            verticalPadding
        )
        setTextColor(Color.WHITE)
        textSize = 12f
        typeface = Typeface.DEFAULT_BOLD
        background = null
    }

    override fun onDraw(canvas: Canvas?) {
        canvas ?: return

        canvas.drawRoundRect(
            0f,
            0f,
            width.toFloat(),
            height.toFloat(),
            height / 2f, height / 2f,
            paint.apply { color = badgeColor }
        )

        super.onDraw(canvas)
    }
}