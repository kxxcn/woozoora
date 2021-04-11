package dev.kxxcn.woozoora.common.extension

import android.graphics.Typeface
import android.text.SpannableStringBuilder
import androidx.core.text.inSpans
import dev.kxxcn.woozoora.util.CustomTypefaceSpan

inline fun SpannableStringBuilder.font(
    typeface: Typeface?,
    builderAction: SpannableStringBuilder.() -> Unit
) = inSpans(CustomTypefaceSpan(typeface), builderAction = builderAction)
