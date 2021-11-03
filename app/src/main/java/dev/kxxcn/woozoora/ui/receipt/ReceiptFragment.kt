package dev.kxxcn.woozoora.ui.receipt

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.FullScreenContentCallback
import dev.kxxcn.woozoora.R
import dev.kxxcn.woozoora.common.EventObserver
import dev.kxxcn.woozoora.databinding.ReceiptFragmentBinding
import dev.kxxcn.woozoora.di.SavedStateViewModelFactory
import dev.kxxcn.woozoora.domain.model.TransactionData
import dev.kxxcn.woozoora.ui.base.BaseFragment
import dev.kxxcn.woozoora.ui.base.CURRENT_TRANSACTION_SAVED_STATE_KEY
import dev.kxxcn.woozoora.util.AdsGenerator
import dev.kxxcn.woozoora.util.Interstitial
import javax.inject.Inject

class ReceiptFragment : BaseFragment<ReceiptFragmentBinding>() {

    @Inject
    lateinit var viewModelFactory: SavedStateViewModelFactory

    override val viewModel by viewModels<ReceiptViewModel> {
        viewModelFactory.create(
            this,
            arguments
        )
    }

    private lateinit var ads: AdsGenerator

    private val backPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            onBackPressed()
        }
    }

    private val contentCallback = object : FullScreenContentCallback() {
        override fun onAdShowedFullScreenContent() {
            super.onAdShowedFullScreenContent()
            close()
        }

        override fun onAdFailedToShowFullScreenContent(p0: AdError) {
            super.onAdFailedToShowFullScreenContent(p0)
            close()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = ReceiptFragmentBinding.inflate(
            inflater,
            container,
            false
        ).apply {
            viewModel = this@ReceiptFragment.viewModel
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupBackPressed()
        setupListener()
        setupAd()
    }

    override fun onDestroyView() {
        if (::ads.isInitialized) {
            ads.release()
        }
        super.onDestroyView()
    }

    private fun setupBackPressed() {
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            backPressedCallback
        )
    }

    private fun setupListener() {
        viewModel.editEvent.observe(viewLifecycleOwner, EventObserver {
            ReceiptFragmentDirections.actionReceiptFragmentToEditFragment(it).show()
        })
        viewModel.transactionEvent.observe(viewLifecycleOwner, {
            savedStateHandleAndDisplayAds(it)
        })
        viewModel.deleteEvent.observe(viewLifecycleOwner, EventObserver {
            deleteDialog()
        })
    }

    private fun setupAd() {
        activity?.let {
            ads = AdsGenerator(
                Interstitial,
                it,
                it.getString(R.string.admob_interstitial_transaction_id)
            ) { close() }.also { adGenerator -> adGenerator.loadInterstitial() }
        }
    }

    private fun onBackPressed() {
        viewModel.saveStateHandle()
    }

    private fun savedStateHandleAndDisplayAds(transaction: TransactionData?) {
        setSavedStateHandleToBackStackEntry(CURRENT_TRANSACTION_SAVED_STATE_KEY, transaction)
        displayAds()
    }

    private fun deleteDialog() {
        openDialog(
            R.drawable.ic_delete,
            getString(R.string.do_you_want_to_delete_the_account_book),
            positive = { viewModel.delete() }
        )
    }

    private fun displayAds() {
        if (::ads.isInitialized && viewModel.isWatchable()) {
            ads.showInterstitial(requireActivity(), contentCallback)
        } else {
            close()
        }
    }
}
