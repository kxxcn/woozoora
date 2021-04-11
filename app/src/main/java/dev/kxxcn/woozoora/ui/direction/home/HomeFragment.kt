package dev.kxxcn.woozoora.ui.direction.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import dev.kxxcn.woozoora.R
import dev.kxxcn.woozoora.common.DURATION_ONE_SECONDS
import dev.kxxcn.woozoora.common.EventObserver
import dev.kxxcn.woozoora.common.base.Scrollable
import dev.kxxcn.woozoora.common.extension.doOnTransition
import dev.kxxcn.woozoora.common.extension.smoothScrollToTop
import dev.kxxcn.woozoora.databinding.HomeFragmentBinding
import dev.kxxcn.woozoora.di.SavedStateViewModelFactory
import dev.kxxcn.woozoora.ui.base.BaseFragment
import dev.kxxcn.woozoora.ui.direction.DirectionViewModel
import dev.kxxcn.woozoora.util.AnimatableScrollListener
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import javax.inject.Inject

class HomeFragment : BaseFragment<HomeFragmentBinding>(), Scrollable {

    @Inject
    lateinit var viewModelFactory: SavedStateViewModelFactory

    private var motionScheduler: Job? = null

    override val viewModel by viewModels<HomeViewModel> {
        viewModelFactory.create(
            this,
            arguments
        )
    }

    private val sharedViewModel by viewModels<DirectionViewModel>(this::requireParentFragment)

    private val currentState: Int
        get() = binding.motionContainer.currentState

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = HomeFragmentBinding.inflate(
            inflater,
            container,
            false
        ).apply {
            viewModel = this@HomeFragment.viewModel
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListAdapter()
        setupListener()
        start()
    }

    override fun onPause() {
        observeNotification(false)
        super.onPause()
    }

    override fun onDestroyView() {
        motionScheduler?.cancel()
        motionScheduler = null
        binding.overviewList.adapter = null
        super.onDestroyView()
    }

    override fun scrollToTop() {
        if (currentState == -1) return
        binding.overviewList.smoothScrollToTop()
        binding.motionContainer.transitionToStart()
    }

    private fun setupListAdapter() {
        with(binding.overviewList) {
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if (isNotThePreparationSceneOver(dy)) {
                        recyclerView.scrollToPosition(0)
                        updateScene()
                    }
                }
            })
            addItemDecoration(HomeSpacingDecoration())
            addOnScrollListener(AnimatableScrollListener())
            adapter = HomeOverviewAdapter(viewModel)
        }
    }

    private fun setupListener() {
        binding.motionContainer.doOnTransition(onTransitionCompleted = { parent, currentState ->
            if (isNextScene(parent?.startState, currentState)) {
                updateScene()
                observeNotification(true)
            }
        })

        viewModel.isProgress.observe(viewLifecycleOwner, EventObserver {
            progress(it)
        })
        viewModel.contactEvent.observe(viewLifecycleOwner, EventObserver {
            contact()
        })
        viewModel.notificationEvent.observe(viewLifecycleOwner, EventObserver {
            notification()
        })
        sharedViewModel.refreshEvent.observe(viewLifecycleOwner, {
            viewModel.start()
        })
    }

    private fun start() {
        viewModel.start()
    }

    private fun progress(isProgress: Boolean) {
        if (isProgress) {
            prepareScene()
        } else {
            stopScene()
        }
    }

    private fun contact() {
        sharedViewModel.contact()
    }

    private fun notification() {
        sharedViewModel.notification()
    }

    private fun prepareScene() {
        binding.motionContainer.setTransition(R.id.overview_loading, R.id.overview_start)
        binding.motionContainer.progress = 0f
    }

    private fun isNextScene(startState: Int?, currentId: Int): Boolean {
        return startState == R.id.overview_loading && currentId == R.id.overview_start
    }

    private fun isNotThePreparationSceneOver(dy: Int): Boolean {
        return currentState == R.id.overview_start && dy > 0
    }

    private fun updateScene() {
        binding.motionContainer.setTransition(R.id.overview_start, R.id.overview_end)
        binding.motionContainer.progress = 0f
    }

    private fun observeNotification(force: Boolean) {
        viewModel.observe(force)
    }

    private fun stopScene() {
        motionScheduler?.cancel()
        motionScheduler = lifecycleScope.launchWhenResumed {
            delay(DURATION_ONE_SECONDS)
            binding.greetingSkeleton.stop()
            binding.nameSkeleton.stop()
            binding.profileSkeleton.stop()
            binding.motionContainer.transitionToEnd()
        }
    }
}
