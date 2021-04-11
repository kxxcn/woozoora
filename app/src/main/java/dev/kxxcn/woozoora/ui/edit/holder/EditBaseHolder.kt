package dev.kxxcn.woozoora.ui.edit.holder

import androidx.databinding.ViewDataBinding
import dev.kxxcn.woozoora.ui.base.BaseHolder
import dev.kxxcn.woozoora.ui.edit.EditViewModel

abstract class EditBaseHolder(binding: ViewDataBinding) : BaseHolder(binding) {

    abstract fun bind(viewModel: EditViewModel)
}
