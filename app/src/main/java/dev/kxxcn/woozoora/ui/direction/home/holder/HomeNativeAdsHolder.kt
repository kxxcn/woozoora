package dev.kxxcn.woozoora.ui.direction.home.holder

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.isVisible
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.VideoOptions
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdOptions
import com.google.android.gms.ads.nativead.NativeAdView
import dev.kxxcn.woozoora.R
import dev.kxxcn.woozoora.common.extension.asTextView
import dev.kxxcn.woozoora.databinding.HomeNativeAdsItemBinding
import dev.kxxcn.woozoora.ui.base.BaseHolder
import dev.kxxcn.woozoora.util.AdsGenerator
import dev.kxxcn.woozoora.util.Native

class HomeNativeAdsHolder(
    private val binding: HomeNativeAdsItemBinding
) : BaseHolder(binding) {

    private var currentNativeAd: NativeAdView? = null

    private val listener = object : AdListener() {
        override fun onAdFailedToLoad(p0: LoadAdError) {
            super.onAdFailedToLoad(p0)
            binding.container.removeAllViews()
        }
    }

    override fun onAttach() {
        super.onAttach()
        setupNativeAds()
    }

    override fun onDetach() {
        currentNativeAd?.destroy()
        super.onDetach()
    }

    fun bind() {
        with(binding) {
            this.lifecycleOwner = this@HomeNativeAdsHolder
            this.executePendingBindings()
        }
    }

    @SuppressLint("InflateParams")
    fun setupNativeAds() {
        val videoOptions = VideoOptions.Builder()
            .setStartMuted(true)
            .build()

        val adOptions = NativeAdOptions.Builder()
            .setVideoOptions(videoOptions)
            .build()

        val inflater = LayoutInflater.from(context)
        val adView = inflater.inflate(R.layout.home_native_ads_view, null) as NativeAdView

        AdsGenerator(
            Native,
            context,
            context.getString(R.string.admob_native_home_personal_id)
        ).loadNative(adOptions, listener) {
            binding.container.removeAllViews()
            binding.container.addView(adView)
            populateNativeAdView(this, adView)
        }
    }

    private fun populateNativeAdView(
        nativeAd: NativeAd,
        adView: NativeAdView
    ) {
        currentNativeAd?.destroy()
        currentNativeAd = adView

        with(adView) {
            mediaView = findViewById(R.id.ad_media)
            bodyView = findViewById(R.id.ad_body)
            callToActionView = findViewById(R.id.ad_call_to_action)

            mediaView?.setImageScaleType(ImageView.ScaleType.CENTER_CROP)

            bodyView?.isVisible = nativeAd.body
                ?.let { bodyView?.asTextView()?.text = it }
                ?.run { true }
                ?: false
            callToActionView?.asTextView()?.text =
                nativeAd.callToAction ?: context.getString(R.string.more)

            setNativeAd(nativeAd)
        }
    }

    companion object {

        fun from(parent: ViewGroup): HomeNativeAdsHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = HomeNativeAdsItemBinding.inflate(inflater, parent, false)
            return HomeNativeAdsHolder(binding)
        }
    }
}
