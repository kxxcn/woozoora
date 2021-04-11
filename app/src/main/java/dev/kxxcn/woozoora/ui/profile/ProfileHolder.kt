package dev.kxxcn.woozoora.ui.profile

import android.view.LayoutInflater
import android.view.ViewGroup
import dev.kxxcn.woozoora.databinding.ProfileItemBinding
import dev.kxxcn.woozoora.ui.base.BaseHolder
import dev.kxxcn.woozoora.ui.profile.item.ProfileItem

class ProfileHolder(
    private val binding: ProfileItemBinding,
) : BaseHolder(binding) {

    fun bind(viewModel: ProfileViewModel, item: ProfileItem) {
        with(binding) {
            this.lifecycleOwner = this@ProfileHolder
            this.viewModel = viewModel
            this.item = item
            this.executePendingBindings()
        }
    }

    companion object {

        fun from(parent: ViewGroup): ProfileHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ProfileItemBinding.inflate(inflater, parent, false)
            return ProfileHolder(binding)
        }
    }
}
