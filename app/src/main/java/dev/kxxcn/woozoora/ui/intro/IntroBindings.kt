package dev.kxxcn.woozoora.ui.intro

import android.widget.TextView
import androidx.core.text.buildSpannedString
import androidx.core.text.color
import androidx.databinding.BindingAdapter
import dev.kxxcn.woozoora.R
import dev.kxxcn.woozoora.common.extension.color

@BindingAdapter("app:signInDesc")
fun setSignInDescription(view: TextView, desc: String) {
    val blackColor = view.context.color(R.color.primaryBlack)
    val orangeColor = view.context.color(R.color.primaryOrange)
    val (which, way, started, comfortable) = desc.split("\n")
    view.text = buildSpannedString {
        color(blackColor) { appendLine(which) }
        color(orangeColor) { appendLine(way) }
        color(blackColor) { appendLine(started) }
        color(blackColor) { appendLine(comfortable) }
    }
}

@BindingAdapter("app:budgetDesc")
fun setBudgetDescription(view: TextView, desc: String) {
    val blackColor = view.context.color(R.color.primaryBlack)
    val orangeColor = view.context.color(R.color.primaryOrange)
    val (month, budget, enter) = desc.split("\n")
    view.text = buildSpannedString {
        color(blackColor) { appendLine(month) }
        color(orangeColor) { appendLine(budget) }
        color(blackColor) { appendLine(enter) }
    }
}

@BindingAdapter("app:yearDesc")
fun setYearDescription(view: TextView, desc: String) {
    val blackColor = view.context.color(R.color.primaryBlack)
    val orangeColor = view.context.color(R.color.primaryOrange)
    val (year, enter) = desc.split("\n")
    view.text = buildSpannedString {
        color(orangeColor) { appendLine(year) }
        color(blackColor) { appendLine(enter) }
    }
}
