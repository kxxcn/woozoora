package dev.kxxcn.woozoora.ui.invite

import android.widget.TextView
import androidx.databinding.BindingAdapter
import dev.kxxcn.woozoora.R
import dev.kxxcn.woozoora.common.extension.color

@BindingAdapter("app:inviteSelection")
fun setInviteTabs(view: TextView, filterType: InviteFilterType) {
    val colorRes = when (view.tag) {
        filterType -> R.color.primaryBlack
        else -> R.color.grey01
    }
    view.setTextColor(view.context.color(colorRes))
}
