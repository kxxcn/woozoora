package dev.kxxcn.woozoora.ui.direction.more.holder

import android.view.LayoutInflater
import android.view.ViewGroup
import dev.kxxcn.woozoora.databinding.MoreGroupItemBinding
import dev.kxxcn.woozoora.ui.direction.more.MoreViewModel

class MoreGroupHolder(
    private val binding: MoreGroupItemBinding,
) : MoreBaseHolder(binding) {

    override fun bind(viewModel: MoreViewModel) {
        with(binding) {
            this.lifecycleOwner = this@MoreGroupHolder
            this.viewModel = viewModel
            this.executePendingBindings()
        }
    }

    companion object {

        fun from(parent: ViewGroup): MoreGroupHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = MoreGroupItemBinding.inflate(inflater, parent, false)
            return MoreGroupHolder(binding)
        }
    }
}
