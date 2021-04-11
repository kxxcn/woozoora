package dev.kxxcn.woozoora.ui.direction.more.holder

import androidx.databinding.ViewDataBinding
import dev.kxxcn.woozoora.ui.base.BaseHolder
import dev.kxxcn.woozoora.ui.direction.more.MoreViewModel

abstract class MoreBaseHolder(binding: ViewDataBinding) : BaseHolder(binding) {

    abstract fun bind(viewModel: MoreViewModel)
}
