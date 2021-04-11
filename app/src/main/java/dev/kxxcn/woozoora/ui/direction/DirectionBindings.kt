package dev.kxxcn.woozoora.ui.direction

import androidx.databinding.BindingAdapter
import nl.joery.animatedbottombar.AnimatedBottomBar

@BindingAdapter("app:currentTab")
fun setCurrentTab(view: AnimatedBottomBar, position: Int?) {
    position?.let { view.selectTabAt(it, false) }
}
