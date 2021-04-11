package dev.kxxcn.woozoora.ui.direction.home.holder

import android.view.LayoutInflater
import android.view.ViewGroup
import dev.kxxcn.woozoora.databinding.HomeEmptyItemBinding
import dev.kxxcn.woozoora.ui.base.AnimatableHolder
import dev.kxxcn.woozoora.ui.direction.home.HomeViewModel

class HomeEmptyHolder(
    private val binding: HomeEmptyItemBinding,
) : AnimatableHolder(binding) {

    override fun animate() {
        binding.animationView.playAnimation()
    }

    fun bind(viewModel: HomeViewModel) {
        with(binding) {
            this.lifecycleOwner = this@HomeEmptyHolder
            this.viewModel = viewModel
            this.executePendingBindings()
        }
    }

    companion object {

        fun from(parent: ViewGroup): HomeEmptyHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = HomeEmptyItemBinding.inflate(inflater, parent, false)
            return HomeEmptyHolder(binding)
        }
    }
}
