package dev.kxxcn.woozoora.ui.direction.more.holder

import android.view.LayoutInflater
import android.view.ViewGroup
import dev.kxxcn.woozoora.databinding.MoreNotificationItemBinding
import dev.kxxcn.woozoora.ui.direction.more.MoreViewModel

class MoreNotificationHolder(
    private val binding: MoreNotificationItemBinding
) : MoreBaseHolder(binding) {

    override fun bind(viewModel: MoreViewModel) {
        with(binding) {
            this.lifecycleOwner = this@MoreNotificationHolder
            this.viewModel = viewModel
            this.executePendingBindings()
        }
    }

    companion object {

        fun from(parent: ViewGroup): MoreNotificationHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = MoreNotificationItemBinding.inflate(inflater, parent, false)
            return MoreNotificationHolder(binding)
        }
    }
}
