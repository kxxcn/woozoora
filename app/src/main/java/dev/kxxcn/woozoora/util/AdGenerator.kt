package dev.kxxcn.woozoora.util

import android.app.Activity
import android.view.ViewGroup
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import dev.kxxcn.woozoora.R
import dev.kxxcn.woozoora.common.extension.or

class AdGenerator(
    private val filterType: AdType,
    private val activity: Activity,
    private val adUnitId: String,
) {

    private val adTestId: String
        get() = when (filterType) {
            AdBanner -> activity.getString(R.string.admob_banner_test_id)
            AdInterstitial -> activity.getString(R.string.admob_interstitial_test_id)
            AdNative -> activity.getString(R.string.admob_native_test_id)
        }

    fun loadBanner(container: ViewGroup, size: AdSize) {
        with(AdView(activity)) {
            container.addView(this)
            adUnitId = this@AdGenerator.adUnitId or adTestId
            adSize = size
            loadAd(AdRequest.Builder().build())
        }
    }

    fun loadInterstitial(callback: InterstitialAdLoadCallback) {
        val request = AdRequest.Builder().build()
        InterstitialAd.load(
            activity,
            this@AdGenerator.adUnitId or adTestId,
            request,
            callback
        )
    }

    fun loadNative() {

    }
}

sealed class AdType
object AdBanner : AdType()
object AdInterstitial : AdType()
object AdNative : AdType()
