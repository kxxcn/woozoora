package dev.kxxcn.woozoora.common.base

import android.app.Activity
import dev.kxxcn.woozoora.ui.invoice.item.Product

interface BillingProvider {

    fun onConnect(callback: BillingState)

    fun onDestroy()

    fun purchase(activity: Activity, id: String)

    suspend fun getProducts(): List<Product>
}

interface BillingState {

    fun onConnected()

    fun onDisconnected()
}
