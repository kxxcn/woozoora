package dev.kxxcn.woozoora.common.base

import android.app.Activity
import android.content.Context
import com.android.billingclient.api.*
import dev.kxxcn.woozoora.R
import dev.kxxcn.woozoora.ui.invoice.item.Product
import javax.inject.Inject
import kotlinx.coroutines.launch

class BillingProviderImpl @Inject constructor(
    private val context: Context,
    scope: BaseCoroutineScope
): BillingProvider, PurchasesUpdatedListener, BaseCoroutineScope by scope {

    private val billingClient = BillingClient.newBuilder(context)
        .enablePendingPurchases()
        .setListener(this)
        .build()

    private lateinit var skus: List<SkuDetails>

    override fun onConnect(callback: BillingState) {
        billingClient.startConnection(object : BillingClientStateListener {

            override fun onBillingServiceDisconnected() {
                callback.onDisconnected()
            }

            override fun onBillingSetupFinished(result: BillingResult) {
                if (result.responseCode == BillingClient.BillingResponseCode.OK) {
                    callback.onConnected()
                } else {
                    onBillingServiceDisconnected()
                }
            }
        })
    }

    override fun onDestroy() {
        billingClient.endConnection()
        release()
    }

    override fun purchase(activity: Activity, id: String) {
        skus.firstOrNull { it.sku == id }?.let { skuDetails ->
            val flowParams = BillingFlowParams
                .newBuilder()
                .setSkuDetails(skuDetails)
                .build()
            billingClient.launchBillingFlow(activity, flowParams)
        }
    }

    override suspend fun getProducts(): List<Product> {
        val skuList = context
            .resources
            .getStringArray(R.array.products)
            .toList()
        val params = SkuDetailsParams
            .newBuilder()
            .setSkusList(skuList)
            .setType(BillingClient.SkuType.SUBS)
        return billingClient
            .querySkuDetails(params.build())
            .skuDetailsList
            ?.also { skus = it }
            ?.map { Product(it.sku, it.title, it.description, it.price) }
            ?: emptyList()
    }

    override fun onPurchasesUpdated(result: BillingResult, purchases: MutableList<Purchase>?) {
        if (result.responseCode == BillingClient.BillingResponseCode.OK) {

        }
    }
}
