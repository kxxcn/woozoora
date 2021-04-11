package dev.kxxcn.woozoora.ui.direction.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.faltenreich.skeletonlayout.SkeletonConfig
import com.faltenreich.skeletonlayout.applySkeleton
import dev.kxxcn.woozoora.R
import dev.kxxcn.woozoora.common.EventObserver
import dev.kxxcn.woozoora.common.base.Scrollable
import dev.kxxcn.woozoora.common.base.Selectable
import dev.kxxcn.woozoora.common.extension.smoothScrollToCenter
import dev.kxxcn.woozoora.databinding.HistoryFragmentBinding
import dev.kxxcn.woozoora.di.SavedStateViewModelFactory
import dev.kxxcn.woozoora.domain.model.TransactionData
import dev.kxxcn.woozoora.ui.base.BaseFragment
import dev.kxxcn.woozoora.ui.base.CURRENT_TRANSACTION_SAVED_STATE_KEY
import dev.kxxcn.woozoora.ui.direction.DirectionViewModel
import dev.kxxcn.woozoora.util.HorizontalLinearLayoutManager
import dev.kxxcn.woozoora.util.HorizontalSpacingDecoration
import javax.inject.Inject

class HistoryFragment : BaseFragment<HistoryFragmentBinding>(), Scrollable, Selectable {

    @Inject
    lateinit var skeletonConfig: SkeletonConfig

    @Inject
    lateinit var viewModelFactory: SavedStateViewModelFactory

    override val viewModel by viewModels<HistoryViewModel> {
        viewModelFactory.create(
            this,
            arguments
        )
    }

    private val sharedViewModel by viewModels<DirectionViewModel>(this::requireParentFragment)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = HistoryFragmentBinding.inflate(
            inflater,
            container,
            false
        ).apply {
            viewModel = this@HistoryFragment.viewModel
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListAdapter()
        setupListener()
        start()
    }

    override fun onDestroyView() {
        binding.dayList.adapter = null
        binding.transactionList.adapter = null
        super.onDestroyView()
    }

    override fun scrollToTop() {
        binding.transactionList.smoothScrollToPosition(0)
    }

    override fun selectDate(timeMs: Long) {
        viewModel.selectDate(timeMs)
    }

    private fun setupListAdapter() {
        with(binding.dayList) {
            addItemDecoration(HorizontalSpacingDecoration())
            layoutManager = HorizontalLinearLayoutManager(context)
            itemAnimator = null
            adapter = HistoryDayAdapter(viewModel)

            applySkeleton(R.layout.skeleton_horizontal_list_item, 10, skeletonConfig)
                .apply { showSkeleton() }
                .also { setTag(R.id.tag_skeleton_history_day_list, it) }
        }

        with(binding.transactionList) {
            addItemDecoration(HistorySpacingDecoration())
            itemAnimator = null
            adapter = HistoryTransactionAdapter(viewModel)

            applySkeleton(R.layout.skeleton_vertical_list_item, 5, skeletonConfig)
                .apply { showSkeleton() }
                .also { setTag(R.id.tag_skeleton_history_transaction_list, it) }
        }
    }

    private fun setupListener() {
        viewModel.stopEvent.observe(viewLifecycleOwner, EventObserver {
            stopSkeleton(it)
        })
        viewModel.scrollEvent.observe(viewLifecycleOwner, {
            binding.dayList.smoothScrollToCenter(it)
        })
        viewModel.receiptEvent.observe(viewLifecycleOwner, EventObserver {
            sharedViewModel.receipt(it)
        })
        sharedViewModel.refreshEvent.observe(viewLifecycleOwner, {
            viewModel.start()
        })
    }

    private fun start() {
        val transaction = getSavedStateHandle<TransactionData>(CURRENT_TRANSACTION_SAVED_STATE_KEY)
        viewModel.start(transaction?.date)
    }

    private fun stopSkeleton(isStop: Boolean) {
        if (isStop) {
            binding.dateParent.isVisible = true
            binding.dayText.isVisible = true
        }
    }
}
