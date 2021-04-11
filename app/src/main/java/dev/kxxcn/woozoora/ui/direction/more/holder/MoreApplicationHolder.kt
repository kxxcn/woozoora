package dev.kxxcn.woozoora.ui.direction.more.holder

import android.view.LayoutInflater
import android.view.ViewGroup
import dev.kxxcn.woozoora.databinding.MoreApplicationItemBinding
import dev.kxxcn.woozoora.ui.direction.more.MoreViewModel

class MoreApplicationHolder(
    private val binding: MoreApplicationItemBinding
) : MoreBaseHolder(binding) {

    override fun bind(viewModel: MoreViewModel) {
        with(binding) {
            this.lifecycleOwner = this@MoreApplicationHolder
            this.viewModel = viewModel
            this.executePendingBindings()
        }
    }

    companion object {

        fun from(parent: ViewGroup): MoreApplicationHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = MoreApplicationItemBinding.inflate(inflater, parent, false)
            return MoreApplicationHolder(binding)
        }
    }
}
