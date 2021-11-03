package dev.kxxcn.woozoora.ui.direction.more.holder

import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.ViewGroup
import dev.kxxcn.woozoora.R
import dev.kxxcn.woozoora.databinding.BannerItemBinding
import dev.kxxcn.woozoora.ui.base.BaseHolder
import dev.kxxcn.woozoora.util.AdsGenerator
import dev.kxxcn.woozoora.util.Banner

class MoreBannerHolder(
    private val metrics: DisplayMetrics,
    private val binding: BannerItemBinding,
) : BaseHolder(binding) {

    fun bind() {
        setupArguments()
        setupBanner()
    }

    private fun setupArguments() {
        with(binding) {
            this.lifecycleOwner = this@MoreBannerHolder
            this.executePendingBindings()
        }
    }

    private fun setupBanner() {
        AdsGenerator(
            Banner,
            context,
            context.getString(R.string.admob_banner_more_id)
        ).loadBanner(binding.container, metrics)
    }

    companion object {

        fun from(metrics: DisplayMetrics, parent: ViewGroup): MoreBannerHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = BannerItemBinding.inflate(inflater, parent, false)
            return MoreBannerHolder(metrics, binding)
        }
    }
}
