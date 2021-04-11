package dev.kxxcn.woozoora.ui.direction.home.holder

import android.view.LayoutInflater
import android.view.ViewGroup
import dev.kxxcn.woozoora.databinding.HomeEventItemBinding
import dev.kxxcn.woozoora.ui.base.BaseHolder
import dev.kxxcn.woozoora.ui.direction.home.HomeViewModel
import dev.kxxcn.woozoora.domain.model.OverviewData

class HomeEventHolder(
    private val binding: HomeEventItemBinding
) : BaseHolder(binding) {

    fun bind(overview: OverviewData, viewModel: HomeViewModel) {
        with(binding) {
            this.lifecycleOwner = this@HomeEventHolder
            this.overview = overview
            this.viewModel = viewModel
            this.executePendingBindings()
        }
    }

    companion object {

        fun from(parent: ViewGroup): HomeEventHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = HomeEventItemBinding.inflate(inflater, parent, false)
            return HomeEventHolder(binding)
        }
    }
}
