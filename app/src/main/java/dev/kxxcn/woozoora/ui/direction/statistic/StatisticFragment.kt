package dev.kxxcn.woozoora.ui.direction.statistic

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.faltenreich.skeletonlayout.SkeletonConfig
import com.faltenreich.skeletonlayout.applySkeleton
import com.google.android.material.tabs.TabLayoutMediator
import dev.kxxcn.woozoora.R
import dev.kxxcn.woozoora.common.DURATION_QUARTER_SECONDS
import dev.kxxcn.woozoora.common.EventObserver
import dev.kxxcn.woozoora.common.base.Scrollable
import dev.kxxcn.woozoora.common.base.Selectable
import dev.kxxcn.woozoora.common.extension.dpToPx
import dev.kxxcn.woozoora.common.extension.smoothScrollToTop
import dev.kxxcn.woozoora.databinding.StatisticFragmentBinding
import dev.kxxcn.woozoora.di.SavedStateViewModelFactory
import dev.kxxcn.woozoora.domain.model.OverviewData
import dev.kxxcn.woozoora.ui.base.BaseFragment
import dev.kxxcn.woozoora.ui.direction.DirectionViewModel
import dev.kxxcn.woozoora.util.ZoomFadeTransformer
import javax.inject.Inject

class StatisticFragment : BaseFragment<StatisticFragmentBinding>(), Scrollable, Selectable {

    @Inject
    lateinit var skeletonConfig: SkeletonConfig

    @Inject
    lateinit var viewModelFactory: SavedStateViewModelFactory

    override val viewModel by viewModels<StatisticViewModel> {
        viewModelFactory.create(
            this,
            arguments
        )
    }

    private val sharedViewModel by viewModels<DirectionViewModel>(this::requireParentFragment)

    private var tabLayoutMediator: TabLayoutMediator? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = StatisticFragmentBinding.inflate(
            inflater,
            container,
            false
        ).apply {
            viewModel = this@StatisticFragment.viewModel
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupSkeleton()
        setupPageAdapter()
        setupListAdapter()
        setupListener()
        start()
    }

    override fun onDestroyView() {
        viewModel.saveInstanceState(binding.statisticPager.currentItem)
        binding.statisticPager.adapter = null
        binding.statisticList.adapter = null
        detachTabLayout()
        super.onDestroyView()
    }

    override fun scrollToTop() {
        binding.statisticList.smoothScrollToTop()
    }

    override fun selectDate(timeMs: Long) {
        viewModel.saveInstanceState(binding.statisticPager.currentItem)
        viewModel.selectDate(timeMs)
    }

    private fun setupSkeleton() {
        binding.pagerSkeleton.showSkeleton()
    }

    private fun setupPageAdapter() {
        with(binding.statisticPager) {
            setPadding(50.dpToPx, 0, 0, 0)
            setPageTransformer(ZoomFadeTransformer())
            adapter = StatisticPageAdapter(this@StatisticFragment, viewModel)
            TabLayoutMediator(binding.statisticTab, this) { _, _ -> }
                .also {
                    tabLayoutMediator = it
                    it.attach()
                }
        }
    }

    private fun setupListAdapter() {
        with(binding.statisticList) {
            itemAnimator = null
            adapter = StatisticAdapter(viewModel)

            applySkeleton(R.layout.skeleton_vertical_list_item, 5, skeletonConfig)
                .apply { showSkeleton() }
                .also { setTag(R.id.tag_skeleton_statistic_list, it) }
        }
    }

    private fun setupListener() {
        viewModel.overview.observe(viewLifecycleOwner, {
            updatePageItems(it)
        })
        viewModel.timelineEvent.observe(viewLifecycleOwner, EventObserver {
            sharedViewModel.timeline(it)
        })
        sharedViewModel.refreshEvent.observe(viewLifecycleOwner, {
            viewModel.start()
        })
    }

    private fun updatePageItems(overview: OverviewData?) {
        overview?.let {
            with(binding.statisticPager) {
                (adapter as? StatisticPageAdapter)?.update(it)
                postDelayed({
                    requestTransform()
                    swipePage(viewModel.savedPageState)
                    visibleTabLayout(it.hasGroup)
                    stopSkeleton()
                }, DURATION_QUARTER_SECONDS)
            }
        }
    }

    private fun start() {
        viewModel.start()
    }

    private fun swipePage(currentPageState: Int?) {
        currentPageState?.let { binding.statisticPager.setCurrentItem(it, false) }
    }

    private fun visibleTabLayout(hasGroup: Boolean) {
        binding.statisticTab.isInvisible = !hasGroup
    }

    private fun stopSkeleton() {
        binding.dateParent.isVisible = true
        binding.pagerSkeleton.isVisible = false
    }

    private fun detachTabLayout() {
        tabLayoutMediator?.detach()
        tabLayoutMediator = null
    }
}
