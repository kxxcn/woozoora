package dev.kxxcn.woozoora.ui.direction.more.holder

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import com.google.android.gms.ads.AdSize
import dev.kxxcn.woozoora.R
import dev.kxxcn.woozoora.common.extension.getDisplaySize
import dev.kxxcn.woozoora.databinding.BannerItemBinding
import dev.kxxcn.woozoora.ui.base.BaseHolder
import dev.kxxcn.woozoora.util.AdBanner
import dev.kxxcn.woozoora.util.AdGenerator

class MoreBannerHolder(
    private val activity: Activity,
    private val binding: BannerItemBinding,
) : BaseHolder(binding) {

    private val adSize: AdSize
        get() {
            val outMetrics = activity.getDisplaySize()
            val density = outMetrics.density

            var adWidthPixels = binding.container.width.toFloat()
            if (adWidthPixels == 0f) {
                adWidthPixels = outMetrics.widthPixels.toFloat()
            }

            val adWidth = (adWidthPixels / density).toInt()
            return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(
                activity,
                adWidth
            )
        }

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
        AdGenerator(
            AdBanner,
            activity,
            context.getString(R.string.admob_banner_more_id)
        ).loadBanner(binding.container, adSize)
    }

    companion object {

        fun from(activity: Activity, parent: ViewGroup): MoreBannerHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = BannerItemBinding.inflate(inflater, parent, false)
            return MoreBannerHolder(activity, binding)
        }
    }
}
