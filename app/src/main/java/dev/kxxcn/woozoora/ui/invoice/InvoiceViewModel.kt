package dev.kxxcn.woozoora.ui.invoice

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dev.kxxcn.woozoora.common.Event
import dev.kxxcn.woozoora.ui.base.BaseViewModel
import dev.kxxcn.woozoora.ui.invoice.item.Product
import javax.inject.Inject

class InvoiceViewModel @Inject constructor() : BaseViewModel() {

    private val _products = MutableLiveData<List<Product>>()
    val products: LiveData<List<Product>> = _products

    private val _purchaseEvent = MutableLiveData<Event<String>>()
    val purchaseEvent: LiveData<Event<String>> = _purchaseEvent

    fun bind(products: List<Product>) {
        _products.value = products
    }

    fun purchase(id: String) {
        _purchaseEvent.value = Event(id)
    }
}
