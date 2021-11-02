package dev.kxxcn.woozoora.ui.notification

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import dev.kxxcn.woozoora.ui.notification.item.NotificationItem

@BindingAdapter("app:notifications")
fun setNotificationList(view: RecyclerView, notifications: List<NotificationItem>?) {
    notifications?.let {
        (view.adapter as? NotificationAdapter)?.submitList(it)
    }
}
