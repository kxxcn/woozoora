package dev.kxxcn.woozoora.ui.direction.history.holder

import android.view.LayoutInflater
import android.view.ViewGroup
import dev.kxxcn.woozoora.databinding.HistoryTransactionItemBinding
import dev.kxxcn.woozoora.ui.base.BaseHolder
import dev.kxxcn.woozoora.ui.direction.history.HistoryViewModel
import dev.kxxcn.woozoora.domain.model.HistoryData

class HistoryTransactionHolder(
    private val binding: HistoryTransactionItemBinding
) : BaseHolder(binding) {

    fun bind(history: HistoryData, viewModel: HistoryViewModel) {
        with(binding) {
            this.lifecycleOwner = this@HistoryTransactionHolder
            this.history = history
            this.viewModel = viewModel
            this.executePendingBindings()
        }
    }

    companion object {

        fun from(parent: ViewGroup): HistoryTransactionHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = HistoryTransactionItemBinding.inflate(inflater, parent, false)
            return HistoryTransactionHolder(binding)
        }
    }
}
