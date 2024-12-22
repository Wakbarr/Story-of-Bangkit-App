package com.example.storyapp__subs1.ui.custom.Password

import android.content.Context
import android.graphics.Canvas
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.core.content.ContextCompat
import com.example.storyapp__subs1.R
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class CustomPasswordTextInputEdit: TextInputEditText {
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    init {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                val textInputLayout = parent.parent as? TextInputLayout
                textInputLayout?.let{
                    error = null
                }
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s != null && s.length < 8) {
                    setTextColor(ContextCompat.getColor(context, R.color.red))
                } else {

                    setTextColor(ContextCompat.getColor(context, android.R.color.black))
                }
            }

            override fun afterTextChanged(s: Editable?) {
                val textInputLayout = parent.parent as? TextInputLayout
                if (textInputLayout != null) {
                    if (s != null && s.length < 8) {
                        textInputLayout.error = "Password must be at least 8 characters"
                    } else {
                        textInputLayout.error = null
                    }
                }
            }
        })
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
    }
}