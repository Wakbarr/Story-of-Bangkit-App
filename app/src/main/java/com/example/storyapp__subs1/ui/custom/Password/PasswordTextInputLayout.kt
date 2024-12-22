package com.example.storyapp__subs1.ui.custom.Password

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class CustomPasswordTextInputLayout: TextInputLayout {
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    private var editText: TextInputEditText? = null

    override fun onFinishInflate() {
        super.onFinishInflate()
        hint = editText?.hint
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        hint = "password"
    }

}