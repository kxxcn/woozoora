package dev.kxxcn.woozoora.common.base

import android.app.Activity
import androidx.lifecycle.LiveData
import dev.kxxcn.woozoora.ui.invoice.item.Product

interface BillingProvider {

    val products: LiveData<List<Product>>

    fun onDestroy()

    fun purchase(activity: Activity, id: String)
}
