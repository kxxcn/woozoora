package dev.kxxcn.woozoora.common.base

import android.app.Activity
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.billingclient.api.*
import com.orhanobut.logger.Logger
import dev.kxxcn.woozoora.R
import dev.kxxcn.woozoora.ui.invoice.item.Product
import javax.inject.Inject
import kotlinx.coroutines.launch

class BillingProviderImpl @Inject constructor(
    private val context: Context,
    scope: BaseCoroutineScope
): BillingProvider, BillingClientStateListener, PurchasesUpdatedListener, BaseCoroutineScope by scope {

    private val billingClient = BillingClient.newBuilder(context)
        .enablePendingPurchases()
        .setListener(this)
        .build()
        .also { it.startConnection(this) }

    private lateinit var skus: List<SkuDetails>

    private val _products = MutableLiveData<List<Product>>()
    override val products: LiveData<List<Product>> = _products

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

    override fun onBillingServiceDisconnected() {

    }

    override fun onBillingSetupFinished(result: BillingResult) {
        if (result.responseCode == BillingClient.BillingResponseCode.OK) {
            setupProducts()
        }
    }

    override fun onPurchasesUpdated(result: BillingResult, purchases: MutableList<Purchase>?) {
        if (result.responseCode == BillingClient.BillingResponseCode.OK) {

        }
    }

    private fun setupProducts() {
        launch {
            val skuList = context
                .resources
                .getStringArray(R.array.products)
                .toList()
            val params = SkuDetailsParams
                .newBuilder()
                .setSkusList(skuList)
                .setType(BillingClient.SkuType.SUBS)
            val items = billingClient
                .querySkuDetails(params.build())
                .skuDetailsList
                ?.also { skus = it }
                ?.map { Product(it.sku, it.title, it.description, it.price) }
                ?: emptyList()
            _products.postValue(items)
        }
    }
}
