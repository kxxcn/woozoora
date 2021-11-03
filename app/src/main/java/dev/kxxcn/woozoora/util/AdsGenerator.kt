package dev.kxxcn.woozoora.util

import android.app.Activity
import android.content.Context
import android.util.DisplayMetrics
import android.view.ViewGroup
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdOptions
import dev.kxxcn.woozoora.R
import dev.kxxcn.woozoora.common.extension.or
import java.lang.ref.WeakReference

class AdsGenerator(
    private val filterType: AdType,
    private val context: Context,
    private val adUnitId: String
) {

    private lateinit var activityRef: WeakReference<Activity>

    private var interstitialAd: InterstitialAd? = null

    private var countRef = 0

    private val isRequested: Boolean
        get() = countRef != 0

    private val hasInterstitial: Boolean
        get() = interstitialAd != null

    private var contentCallback: FullScreenContentCallback? = null

    private val interstitialCallback = object : InterstitialAdLoadCallback() {
        override fun onAdLoaded(ad: InterstitialAd) {
            super.onAdLoaded(ad)
            interstitialAd = ad
            showInterstitial()
        }

        override fun onAdFailedToLoad(p0: LoadAdError) {
            super.onAdFailedToLoad(p0)
            loadInterstitial()
        }
    }

    private val adTestId: String
        get() = when (filterType) {
            Banner -> context.getString(R.string.admob_banner_test_id)
            Interstitial -> context.getString(R.string.admob_interstitial_test_id)
            Native -> context.getString(R.string.admob_native_test_id)
        }

    fun loadBanner(container: ViewGroup, metrics: DisplayMetrics) {
        with(AdView(context)) {
            container.removeAllViews()
            container.addView(this)
            adUnitId = this@AdsGenerator.adUnitId or adTestId
            adSize = getSize(metrics)
            loadAd(AdRequest.Builder().build())
        }
    }

    fun loadInterstitial() {
        val request = AdRequest.Builder().build()
        InterstitialAd.load(
            context,
            this@AdsGenerator.adUnitId or adTestId,
            request,
            interstitialCallback
        )
    }

    fun loadNative(
        option: NativeAdOptions,
        listener: AdListener,
        block: NativeAd.() -> Unit
    ) {
        AdLoader.Builder(context, this@AdsGenerator.adUnitId or adTestId)
            .forNativeAd { block(it) }
            .withAdListener(listener)
            .withNativeAdOptions(option)
            .build()
            .also { it.loadAd(AdRequest.Builder().build()) }
    }

    fun showInterstitial(activity: Activity? = null, callback: FullScreenContentCallback? = null) {
        val isEnable = callback != null && hasInterstitial
        if (isEnable || isRequested) {
            val canvas = activity ?: activityRef.get() ?: return
            interstitialAd?.fullScreenContentCallback = callback ?: contentCallback
            interstitialAd?.show(canvas)
        } else {
            countRef++
        }
        activityRef = WeakReference(activity)
        contentCallback = callback
    }

    fun release() {
        interstitialAd = null
    }

    private fun getSize(metrics: DisplayMetrics): AdSize {
        val adWidthPixels = metrics.widthPixels.toFloat()
        val adWidth = (adWidthPixels / metrics.density).toInt()
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(context, adWidth)
    }
}

sealed class AdType
object Banner : AdType()
object Interstitial : AdType()
object Native : AdType()
