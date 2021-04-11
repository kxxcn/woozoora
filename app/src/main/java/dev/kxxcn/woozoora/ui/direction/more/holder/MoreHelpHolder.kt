package dev.kxxcn.woozoora.ui.direction.more.holder

import android.view.LayoutInflater
import android.view.ViewGroup
import dev.kxxcn.woozoora.databinding.MoreHelpItemBinding
import dev.kxxcn.woozoora.ui.direction.more.MoreViewModel

class MoreHelpHolder(
    private val binding: MoreHelpItemBinding
) : MoreBaseHolder(binding) {

    override fun bind(viewModel: MoreViewModel) {
        with(binding) {
            this.lifecycleOwner = this@MoreHelpHolder
            this.viewModel = viewModel
            this.executePendingBindings()
        }
    }

    companion object {

        fun from(parent: ViewGroup): MoreHelpHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = MoreHelpItemBinding.inflate(inflater, parent, false)
            return MoreHelpHolder(binding)
        }
    }
}
