package dev.kxxcn.woozoora.ui.notification

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import dev.kxxcn.woozoora.databinding.NotificationFragmentBinding
import dev.kxxcn.woozoora.ui.base.BaseFragment
import javax.inject.Inject

class NotificationFragment : BaseFragment<NotificationFragmentBinding>() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override val viewModel by viewModels<NotificationViewModel> { viewModelFactory }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = NotificationFragmentBinding.inflate(
            inflater,
            container,
            false
        ).apply {
            viewModel = this@NotificationFragment.viewModel
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListAdapter()
        setupListener()
    }

    private fun setupListAdapter() {
        with(binding.notificationList) {
            addItemDecoration(NotificationSpacingDecoration())
            itemAnimator = null
            adapter = NotificationAdapter(viewModel)
        }
    }

    private fun setupListener() {
        viewModel.notifications.observe(viewLifecycleOwner, {
            viewModel.update()
        })
    }
}
