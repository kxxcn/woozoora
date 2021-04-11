package dev.kxxcn.woozoora.ui.notification

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import dev.kxxcn.woozoora.domain.model.NotificationData

@BindingAdapter("app:notifications")
fun setNotificationList(view: RecyclerView, notifications: List<NotificationData>?) {
    notifications?.let {
        (view.adapter as? NotificationAdapter)?.submitList(NotificationAdapter.create(it))
    }
}
