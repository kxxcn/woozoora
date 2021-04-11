package dev.kxxcn.woozoora.ui.timeline

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dev.kxxcn.woozoora.common.EventObserver
import dev.kxxcn.woozoora.databinding.TimelineFragmentBinding
import dev.kxxcn.woozoora.di.SavedStateViewModelFactory
import dev.kxxcn.woozoora.domain.model.HistoryData
import dev.kxxcn.woozoora.domain.model.TransactionData
import dev.kxxcn.woozoora.ui.base.BaseFragment
import dev.kxxcn.woozoora.ui.base.CURRENT_TRANSACTION_SAVED_STATE_KEY
import javax.inject.Inject

class TimelineFragment : BaseFragment<TimelineFragmentBinding>() {

    @Inject
    lateinit var viewModelFactory: SavedStateViewModelFactory

    override val viewModel by viewModels<TimelineViewModel> {
        viewModelFactory.create(
            this,
            arguments
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = TimelineFragmentBinding.inflate(
            inflater,
            container,
            false
        ).apply {
            viewModel = this@TimelineFragment.viewModel
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListAdapter()
        setupListener()
        start()
    }

    private fun setupListAdapter() {
        binding.timelineList.addItemDecoration(TimelineSpacingDecoration())
        binding.timelineList.adapter = TimelineAdapter(viewModel)
    }

    private fun setupListener() {
        viewModel.receiptEvent.observe(viewLifecycleOwner, EventObserver {
            receipt(it)
        })
        viewModel.transactions.observe(viewLifecycleOwner, {
            if (it.isEmpty()) close()
        })
    }

    private fun start() {
        findNavController()
            .currentBackStackEntry
            ?.savedStateHandle
            ?.get<TransactionData>(CURRENT_TRANSACTION_SAVED_STATE_KEY)
            .let { viewModel.start(it) }
    }

    private fun receipt(history: HistoryData) {
        TimelineFragmentDirections.actionTimelineFragmentToReceiptFragment(history).show()
    }
}
