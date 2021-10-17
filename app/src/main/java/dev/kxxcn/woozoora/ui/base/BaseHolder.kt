package dev.kxxcn.woozoora.ui.base

import android.content.Context
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.recyclerview.widget.RecyclerView

abstract class BaseHolder(
    private val binding: ViewDataBinding,
) : RecyclerView.ViewHolder(binding.root), LifecycleOwner {

    private val lifecycleRegistry by lazy { LifecycleRegistry(this) }

    val context: Context
        get() = binding.root.context

    init {
        lifecycleRegistry.currentState = Lifecycle.State.INITIALIZED
    }

    override fun getLifecycle(): Lifecycle {
        return lifecycleRegistry
    }

    open fun onAttach() {
        lifecycleRegistry.currentState = Lifecycle.State.STARTED
    }

    open fun onDetach() {
        lifecycleRegistry.currentState = Lifecycle.State.CREATED
    }
}
