package dev.kxxcn.woozoora.ui.reply

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import dev.kxxcn.woozoora.domain.model.AskData
import dev.kxxcn.woozoora.ui.base.BaseAdapter

class ReplyAdapter : BaseAdapter<AskData, ReplyHolder>(ReplyItemCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReplyHolder {
        return ReplyHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ReplyHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ReplyItemCallback : DiffUtil.ItemCallback<AskData>() {

        override fun areItemsTheSame(oldItem: AskData, newItem: AskData): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: AskData, newItem: AskData): Boolean {
            return oldItem == newItem
        }
    }
}
