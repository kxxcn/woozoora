package dev.kxxcn.woozoora.ui.notice

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import dev.kxxcn.woozoora.domain.model.NoticeData

@BindingAdapter("app:notice")
fun setNoticeList(view: RecyclerView, items: List<NoticeData>?) {
    items?.let {
        val newList = NoticeAdapter.create(it)
        (view.adapter as? NoticeAdapter)?.submitList(newList)
    }
}
