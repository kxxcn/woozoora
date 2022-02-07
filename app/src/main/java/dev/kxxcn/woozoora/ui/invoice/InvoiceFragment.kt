package dev.kxxcn.woozoora.ui.invoice

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import dev.kxxcn.woozoora.common.EventObserver
import dev.kxxcn.woozoora.databinding.InvoiceFragmentBinding
import dev.kxxcn.woozoora.ui.base.BaseFragment
import dev.kxxcn.woozoora.common.base.BillingProvider
import dev.kxxcn.woozoora.common.base.BillingState
import kotlinx.coroutines.launch
import javax.inject.Inject

class InvoiceFragment : BaseFragment<InvoiceFragmentBinding>(), BillingState {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var billing: BillingProvider

    override val viewModel by viewModels<InvoiceViewModel> { viewModelFactory }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = InvoiceFragmentBinding.inflate(
            inflater,
            container,
            false
        ).apply {
            viewModel = this@InvoiceFragment.viewModel
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListener()
        setupBillingProvider()
    }

    override fun onDestroyView() {
        billing.onDestroy()
        super.onDestroyView()
    }

    override fun onConnected() {
        lifecycleScope.launch {
            viewModel.bind(billing.getProducts())
        }
    }

    override fun onDisconnected() {

    }

    private fun setupListener() {
        viewModel.purchaseEvent.observe(viewLifecycleOwner, EventObserver {
            billing.purchase(requireActivity(), it)
        })
    }

    private fun setupBillingProvider() {
        billing.onConnect(this)
    }
}
