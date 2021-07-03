package dev.kxxcn.woozoora.ui.reply

import android.view.LayoutInflater
import android.view.ViewGroup
import dev.kxxcn.woozoora.databinding.ReplyItemBinding
import dev.kxxcn.woozoora.domain.model.AskData
import dev.kxxcn.woozoora.ui.base.BaseHolder

class ReplyHolder(
    private val binding: ReplyItemBinding,
) : BaseHolder(binding) {

    fun bind(ask: AskData) {
        with(binding) {
            this.lifecycleOwner = this@ReplyHolder
            this.ask = ask
            this.executePendingBindings()
        }
    }

    companion object {

        fun from(parent: ViewGroup): ReplyHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ReplyItemBinding.inflate(inflater, parent, false)
            return ReplyHolder(binding)
        }
    }
}
