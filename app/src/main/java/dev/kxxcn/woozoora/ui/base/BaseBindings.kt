package dev.kxxcn.woozoora.ui.base

import android.text.SpannableStringBuilder
import android.view.View
import android.webkit.WebView
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.widget.doOnTextChanged
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import androidx.databinding.adapters.ListenerUtil
import com.airbnb.lottie.LottieAnimationView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import dev.kxxcn.woozoora.R
import dev.kxxcn.woozoora.common.FORMAT_DATE_MONTH_DOT_DAY
import dev.kxxcn.woozoora.common.extension.countAnimation
import dev.kxxcn.woozoora.common.extension.dpToPx
import dev.kxxcn.woozoora.common.extension.month
import dev.kxxcn.woozoora.common.extension.year
import dev.kxxcn.woozoora.util.Converter


@BindingAdapter("app:bindingText")
fun setBindingText(view: EditText, value: String?) {
    val oldText = view.text.toString()
    if (oldText == value || (value == null && oldText.isEmpty())) {
        return
    }
    value?.let {
        view.text = SpannableStringBuilder(it)
        view.setSelection(it.length)
    }
}

@InverseBindingAdapter(attribute = "app:bindingText")
fun getBindingText(view: EditText): String {
    return view.text.toString()
}

@BindingAdapter("app:bindingTextAttrChanged")
fun setBindingTextListener(view: EditText, bindingTextAttrChanged: InverseBindingListener?) {
    val newValue = view.doOnTextChanged { _, _, _, _ ->
        bindingTextAttrChanged?.onChange()
    }
    val oldValue = ListenerUtil.trackListener(view, newValue, R.id.textWatcher)
    oldValue?.let {
        view.removeTextChangedListener(it)
    }
    newValue.let {
        view.addTextChangedListener(it)
    }
}

@BindingAdapter("app:inputType")
fun setInputType(view: EditText, inputType: Int?) {
    inputType?.let { view.inputType = it }
}

@BindingAdapter("app:month")
fun setMonthText(view: TextView, month: Int) {
    val args = (month + 1).toString()
    view.text = view.context.getString(R.string.format_month, args)
}

@BindingAdapter("app:price")
fun setPriceText(view: TextView, price: String?) {
    price?.takeIf { it.isNotEmpty() }?.let {
        view.text = view.context.getString(R.string.format_price, Converter.moneyFormat(it))
    }
}

@BindingAdapter("app:rangeText", "app:bracket")
fun setRangeText(
    view: TextView,
    range: Pair<Long, Long>?,
    bracket: Boolean,
) {
    range?.let { (startDate, endDate) ->
        view.text = view.context.getString(
            if (bracket) R.string.format_date_with_bracket else R.string.format_date,
            Converter.dateFormat(FORMAT_DATE_MONTH_DOT_DAY, startDate),
            Converter.dateFormat(FORMAT_DATE_MONTH_DOT_DAY, endDate)
        )
    }
}

@BindingAdapter("app:countAnimation")
fun setCountAnimationText(view: TextView, count: Int) {
    view.countAnimation(to = count) {
        view.context.getString(R.string.format_price, Converter.moneyFormat(it.toString()))
    }
}

@BindingAdapter("app:dateTime")
fun setDateText(view: TextView, timeMs: Long?) {
    timeMs?.let {
        view.text = view.context.getString(R.string.format_date_year_month, it.year, it.month + 1)
    }
}

@BindingAdapter("app:circleImage")
fun setCircleImage(view: ImageView, url: String?) {
    url?.let { Glide.with(view.context).load(it).circleCrop().into(view) }
}

@BindingAdapter("app:roundImage")
fun setRoundImage(view: ImageView, url: String?) {
    url.let {
        Glide.with(view)
            .load(it)
            .transform(CenterCrop(), RoundedCorners(15.dpToPx))
            .into(view)
    }
}

@BindingAdapter("visibleOnMotionLayout")
fun setVisibleUnlessOnMotionLayout(view: View, visible: Boolean) {
    if (view.parent is MotionLayout) {
        val layout = view.parent as MotionLayout
        val setToVisibility = if (visible) View.VISIBLE else View.GONE
        val setToAlpha = if (visible) 1f else 0f
        for (constraintId in layout.constraintSetIds) {
            val constraint = layout.getConstraintSet(constraintId)
            if (constraint != null) {
                constraint.setVisibility(view.id, setToVisibility)
                constraint.setAlpha(view.id, setToAlpha)
                constraint.applyTo(layout)
            }
        }
    }
}

@BindingAdapter("invisibleOnMotionLayout")
fun setInVisibleUnlessOnMotionLayout(view: View, invisible: Boolean) {
    if (view.parent is MotionLayout) {
        val layout = view.parent as MotionLayout
        val setToVisibility = if (invisible) View.INVISIBLE else View.VISIBLE
        val setToAlpha = if (invisible) 0f else 1f
        for (constraintId in layout.constraintSetIds) {
            val constraint = layout.getConstraintSet(constraintId)
            if (constraint != null) {
                constraint.setVisibility(view.id, setToVisibility)
                constraint.setAlpha(view.id, setToAlpha)
                constraint.applyTo(layout)
            }
        }
    }
}

@BindingAdapter("app:percent")
fun setPercent(view: ProgressBar, percent: Int?) {
    percent?.let { view.progress = it }
}

@BindingAdapter("app:loadUrl")
fun setWebView(view: WebView, url: String?) {
    url?.let { view.loadUrl(it) }
}

@BindingAdapter("app:lottie_file")
fun setLottieFile(view: LottieAnimationView, fileName: String) {
    view.setAnimation(fileName)
}
