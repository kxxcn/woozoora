package dev.kxxcn.woozoora.ui.direction.more.holder

import android.view.LayoutInflater
import android.view.ViewGroup
import dev.kxxcn.woozoora.databinding.MorePolicyItemBinding
import dev.kxxcn.woozoora.ui.direction.more.MoreViewModel

class MorePolicyHolder(
    private val binding: MorePolicyItemBinding
) : MoreBaseHolder(binding) {

    override fun bind(viewModel: MoreViewModel) {
        with(binding) {
            this.lifecycleOwner = this@MorePolicyHolder
            this.viewModel = viewModel
            this.executePendingBindings()
        }
    }

    companion object {

        fun from(parent: ViewGroup): MorePolicyHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = MorePolicyItemBinding.inflate(inflater, parent, false)
            return MorePolicyHolder(binding)
        }
    }
}
