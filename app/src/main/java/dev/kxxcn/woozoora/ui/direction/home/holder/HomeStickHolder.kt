package dev.kxxcn.woozoora.ui.direction.home.holder

import android.view.LayoutInflater
import android.view.ViewGroup
import dev.kxxcn.woozoora.databinding.HomeStickItemBinding
import dev.kxxcn.woozoora.ui.base.BaseHolder

class HomeStickHolder(binding: HomeStickItemBinding) : BaseHolder(binding) {

    companion object {

        fun from(parent: ViewGroup): HomeStickHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = HomeStickItemBinding.inflate(inflater, parent, false)
            return HomeStickHolder(binding)
        }
    }
}
