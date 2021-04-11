package dev.kxxcn.woozoora.ui.direction.home.holder

import android.view.LayoutInflater
import android.view.ViewGroup
import dev.kxxcn.woozoora.R
import dev.kxxcn.woozoora.databinding.HomeTitleItemBinding
import dev.kxxcn.woozoora.ui.base.BaseHolder

class HomeTitleHolder(private val binding: HomeTitleItemBinding) : BaseHolder(binding) {

    fun bindUserTitle() {
        val title = context.getString(R.string.personal_spending)
        val iconRes = R.drawable.ic_emoji_fire
        bind(title, iconRes)
    }

    fun bindGroupTitle() {
        val title = context.getString(R.string.group_spending)
        val iconRes = R.drawable.ic_emoji_family
        bind(title, iconRes)
    }

    private fun bind(title: String, iconRes: Int) {
        with(binding) {
            this.lifecycleOwner = this@HomeTitleHolder
            this.title = title
            this.iconRes = iconRes
            this.executePendingBindings()
        }
    }

    companion object {

        fun from(parent: ViewGroup): HomeTitleHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = HomeTitleItemBinding.inflate(inflater, parent, false)
            return HomeTitleHolder(binding)
        }
    }
}
