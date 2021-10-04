package dev.kxxcn.woozoora.ui.direction

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.animation.doOnEnd
import androidx.core.animation.doOnStart
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dev.kxxcn.woozoora.R
import dev.kxxcn.woozoora.common.*
import dev.kxxcn.woozoora.common.base.Scrollable
import dev.kxxcn.woozoora.common.extension.color
import dev.kxxcn.woozoora.databinding.DirectionFragmentBinding
import dev.kxxcn.woozoora.di.SavedStateViewModelFactory
import dev.kxxcn.woozoora.domain.model.HistoryData
import dev.kxxcn.woozoora.domain.model.InvitationData
import dev.kxxcn.woozoora.domain.model.TimelineData
import dev.kxxcn.woozoora.ui.base.BaseFragment
import dev.kxxcn.woozoora.ui.edit.EditBranchType
import dev.kxxcn.woozoora.ui.policy.PolicyFilterType
import dev.kxxcn.woozoora.ui.ticket.TicketFragment
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import nl.joery.animatedbottombar.AnimatedBottomBar
import javax.inject.Inject
import kotlin.properties.Delegates
import kotlin.reflect.KProperty

class DirectionFragment : BaseFragment<DirectionFragmentBinding>() {

    @Inject
    lateinit var viewModelFactory: SavedStateViewModelFactory

    private val expandAnimator = AnimatorSet()

    private val collapseAnimator = AnimatorSet()

    private lateinit var expandColorAnimator: ValueAnimator

    private lateinit var collapseColorAnimator: ValueAnimator

    private var expandable by Delegates.observable(false, ::onChange)

    override val viewModel by viewModels<DirectionViewModel> {
        viewModelFactory.create(
            this,
            arguments
        )
    }

    private val backPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            onBackPressed()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = DirectionFragmentBinding.inflate(
            inflater,
            container,
            false
        ).apply {
            viewModel = this@DirectionFragment.viewModel
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            delay(DURATION_HALF_SECONDS)
            setupPager()
            setupListener()
            setupBackPressed()
            setupAnimatorSet()
        }
    }

    override fun onDestroyView() {
        binding.directionPager.adapter = null
        expandAnimator.removeAllListeners()
        collapseAnimator.removeAllListeners()
        expandable = false
        expandColorAnimator.removeAllUpdateListeners()
        collapseColorAnimator.removeAllUpdateListeners()
        super.onDestroyView()
    }

    private fun setupPager() {
        val defaultMs = arguments
            ?.getString(KEY_DEFAULT_DATE)
            ?.toLong()

        with(binding.directionPager) {
            offscreenPageLimit = 3
            isUserInputEnabled = false
            adapter = DirectionPageAdapter(this@DirectionFragment, defaultMs)
        }
    }

    private fun setupListener() {
        binding.bottomBar.onTabSelected = { moveTab(it) }
        binding.bottomBar.onTabReselected = { scrollToTop(it) }

        viewModel.createEvent.observe(viewLifecycleOwner, EventObserver {
            create()
        })
        viewModel.editEvent.observe(viewLifecycleOwner, EventObserver {
            if (expandable) {
                edit(it)
            }
        })
        viewModel.receiptEvent.observe(viewLifecycleOwner, EventObserver {
            receipt(it)
        })
        viewModel.timelineEvent.observe(viewLifecycleOwner, EventObserver {
            timeline(it)
        })
        viewModel.contactEvent.observe(viewLifecycleOwner, EventObserver {
            contact()
        })
        viewModel.codeEvent.observe(viewLifecycleOwner, EventObserver {
            code()
        })
        viewModel.inviteEvent.observe(viewLifecycleOwner, EventObserver {
            invitation(it)
        })
        viewModel.pageEvent.observe(viewLifecycleOwner, EventObserver {
            moveTab(it, false)
        })
        viewModel.policyEvent.observe(viewLifecycleOwner, EventObserver {
            policy(it)
        })
        viewModel.askEvent.observe(viewLifecycleOwner, EventObserver {
            ask()
        })
        viewModel.notificationEvent.observe(viewLifecycleOwner, EventObserver {
            notification()
        })
        viewModel.noticeEvent.observe(viewLifecycleOwner, EventObserver {
            notice()
        })
        viewModel.profileEvent.observe(viewLifecycleOwner, EventObserver {
            profile()
        })
    }

    private fun setupBackPressed() {
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            backPressedCallback
        )
    }

    private fun setupAnimatorSet() {
        expandColorAnimator = ValueAnimator.ofArgb(
            requireContext().color(R.color.primaryOrange),
            requireContext().color(R.color.grey01)
        ).apply {
            addUpdateListener { updateBackgroundColor(it.animatedValue as Int) }
            doOnStart { binding.fabSelector.isVisible = true }
        }
        collapseColorAnimator = ValueAnimator.ofArgb(
            requireContext().color(R.color.grey01),
            requireContext().color(R.color.primaryOrange)
        ).apply {
            addUpdateListener { updateBackgroundColor(it.animatedValue as Int) }
            doOnEnd { binding.fabSelector.isVisible = false }
        }

        expandAnimator.playTogether(
            ObjectAnimator.ofFloat(binding.fabSelector, View.TRANSLATION_Y, -200f),
            ObjectAnimator.ofFloat(binding.fabSelector, View.ALPHA, 1f),
            ObjectAnimator.ofFloat(binding.parentFab, View.ROTATION, 45f),
            expandColorAnimator
        )
        collapseAnimator.playTogether(
            ObjectAnimator.ofFloat(binding.fabSelector, View.TRANSLATION_Y, 0f),
            ObjectAnimator.ofFloat(binding.fabSelector, View.ALPHA, 0f),
            ObjectAnimator.ofFloat(binding.parentFab, View.ROTATION, 0f),
            collapseColorAnimator
        )
    }

    private fun updateBackgroundColor(color: Int) {
        binding.parentFab.backgroundTintList = ColorStateList.valueOf(color)
    }

    private fun onChange(property: KProperty<*>, oldValue: Boolean, newValue: Boolean) {
        if (newValue) {
            expandAnimator.start()
        } else {
            collapseAnimator.start()
        }
    }

    private fun moveTab(tab: AnimatedBottomBar.Tab) {
        findTabPosition(tab)?.let {
            movePage(it)
            saveInstanceState(it)
        }
    }

    private fun moveTab(page: Int, animate: Boolean = true) {
        selectTabAt(page, animate)
        saveInstanceState(page)
    }

    private fun movePage(position: Int) {
        binding.directionPager.currentItem = position
    }

    private fun selectTabAt(page: Int, animate: Boolean) {
        binding.bottomBar.selectTabAt(page, animate)
    }

    private fun saveInstanceState(position: Int) {
        viewModel.saveInstanceState(position)
    }

    private fun scrollToTop(tab: AnimatedBottomBar.Tab) {
        childFragmentManager.fragments
            .takeIf { it.isNotEmpty() }
            ?.let { fragments ->
                val tabPosition = findTabPosition(tab)
                tabPosition?.let { (fragments[it] as? Scrollable)?.scrollToTop() }
            }
    }

    private fun findTabPosition(tab: AnimatedBottomBar.Tab): Int? {
        return binding.bottomBar.tabs
            .withIndex()
            .find { it.value.id == tab.id }
            ?.index
    }

    private fun onBackPressed() {
        if (isCanGoBack()) {
            moveTab(DirectionPageAdapter.PAGE_HOME)
        } else {
            viewModel.onBackPressed()
        }
    }

    private fun isCanGoBack(): Boolean {
        return binding.directionPager.currentItem != DirectionPageAdapter.PAGE_HOME
    }

    private fun create() {
        if (!expandAnimator.isRunning && !collapseAnimator.isRunning) {
            expandable = !expandable
        }
    }

    private fun edit(branchType: EditBranchType) {
        DirectionFragmentDirections.actionDirectionFragmentToEditFragment(branchType).show()
    }

    private fun receipt(history: HistoryData) {
        DirectionFragmentDirections.actionDirectionFragmentToReceiptFragment(history).show()
    }

    private fun timeline(timeline: TimelineData) {
        DirectionFragmentDirections.actionDirectionFragmentToTimelineFragment(timeline).show()
    }

    private fun contact() {
        DirectionFragmentDirections.actionDirectionFragmentToContactFragment().show()
    }

    private fun code() {
        DirectionFragmentDirections.actionDirectionFragmentToCodeFragment().show()
    }

    private fun invitation(invitation: InvitationData) {
        TicketFragment()
            .apply { arguments = bundleOf(KEY_INVITATION_ITEM to invitation) }
            .show(childFragmentManager, TicketFragment::class.java.name)
    }

    private fun policy(requestType: PolicyFilterType) {
        DirectionFragmentDirections.actionDirectionFragmentToPolicyFragment(requestType).show()
    }

    private fun ask() {
        DirectionFragmentDirections.actionDirectionFragmentToReplyFragment().show()
    }

    private fun notification() {
        DirectionFragmentDirections.actionDirectionFragmentToNotificationFragment().show()
    }

    private fun notice() {
        DirectionFragmentDirections.actionDirectionFragmentToNoticeFragment().show()
    }

    private fun profile() {
        DirectionFragmentDirections.actionDirectionFragmentToProfileFragment().show()
    }
}
