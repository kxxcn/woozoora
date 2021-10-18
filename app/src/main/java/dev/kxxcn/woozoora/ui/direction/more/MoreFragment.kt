package dev.kxxcn.woozoora.ui.direction.more

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import dev.kxxcn.woozoora.R
import dev.kxxcn.woozoora.common.EventObserver
import dev.kxxcn.woozoora.common.extension.*
import dev.kxxcn.woozoora.databinding.MoreFragmentBinding
import dev.kxxcn.woozoora.domain.model.OptionData
import dev.kxxcn.woozoora.ui.base.BaseFragment
import dev.kxxcn.woozoora.ui.direction.DirectionViewModel
import dev.kxxcn.woozoora.ui.edit.EditBranchType
import dev.kxxcn.woozoora.ui.policy.PolicyFilterType
import java.util.*
import javax.inject.Inject

class MoreFragment : BaseFragment<MoreFragmentBinding>() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override val viewModel by viewModels<MoreViewModel> { viewModelFactory }

    private val sharedViewModel by viewModels<DirectionViewModel>(this::requireParentFragment)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = MoreFragmentBinding.inflate(
            inflater,
            container,
            false
        ).apply {
            viewModel = this@MoreFragment.viewModel
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListAdapter()
        setupListener()
    }

    override fun onResume() {
        super.onResume()
        setupNotification()
    }

    private fun setupListAdapter() {
        binding.moreList.addItemDecoration(MoreSpacingDecoration())
        binding.moreList.adapter = MoreAdapter(viewModel).apply { submitList(MoreAdapter.create()) }
    }

    private fun setupListener() {
        viewModel.contactEvent.observe(viewLifecycleOwner, EventObserver {
            contact()
        })
        viewModel.codeEvent.observe(viewLifecycleOwner, EventObserver {
            code()
        })
        viewModel.reviewEvent.observe(viewLifecycleOwner, EventObserver {
            review()
        })
        viewModel.askEvent.observe(viewLifecycleOwner, EventObserver {
            ask()
        })
        viewModel.noticeEvent.observe(viewLifecycleOwner, EventObserver {
            notice()
        })
        viewModel.profileEvent.observe(viewLifecycleOwner, EventObserver {
            profile()
        })
        viewModel.notificationEvent.observe(viewLifecycleOwner, EventObserver {
            setNotificationOption(it)
        })
        viewModel.policyEvent.observe(viewLifecycleOwner, EventObserver {
            policy(it)
        })
        viewModel.sortEvent.observe(viewLifecycleOwner, EventObserver {
            sort(it)
        })
    }

    private fun setupNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val values = requireContext()
                .notificationManager
                .notificationChannels
                .map { it.id to it.isEnabled() }
            viewModel.saveNotificationOption(values)
        }
    }

    private fun contact() {
        sharedViewModel.contact()
    }

    private fun code() {
        sharedViewModel.code()
    }

    private fun setNotificationOption(option: OptionData) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS).apply {
                putExtra(Settings.EXTRA_CHANNEL_ID, option.channel)
                putExtra(Settings.EXTRA_APP_PACKAGE, activity?.packageName)
            }.also {
                startActivity(it)
            }
        } else {
            viewModel.saveNotificationOption(option)
        }
    }

    private fun review() {
        val packageName = context?.packageName
        try {
            val uri = getString(R.string.play_store_app, packageName)
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(uri)))
        } catch (e: ActivityNotFoundException) {
            val uri = getString(R.string.play_store_web, packageName)
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(uri)))
        }
    }

    private fun policy(requestType: PolicyFilterType) {
        sharedViewModel.policy(requestType)
    }

    private fun ask() {
        sharedViewModel.ask()
    }

    private fun notice() {
        sharedViewModel.notice()
    }

    private fun profile() {
        sharedViewModel.profile()
    }

    private fun sort(branch: EditBranchType) {
        sharedViewModel.sort(branch)
    }
}
