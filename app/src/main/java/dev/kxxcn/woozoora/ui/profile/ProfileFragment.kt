package dev.kxxcn.woozoora.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import dev.kxxcn.woozoora.R
import dev.kxxcn.woozoora.common.EventObserver
import dev.kxxcn.woozoora.databinding.ProfileFragmentBinding
import dev.kxxcn.woozoora.domain.model.ChangeData
import dev.kxxcn.woozoora.ui.base.BaseFragment
import javax.inject.Inject

class ProfileFragment : BaseFragment<ProfileFragmentBinding>() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override val viewModel by viewModels<ProfileViewModel> { viewModelFactory }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = ProfileFragmentBinding.inflate(
            inflater,
            container,
            false
        ).apply {
            viewModel = this@ProfileFragment.viewModel
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
        binding.profileList.adapter = ProfileAdapter(viewModel)
    }

    private fun setupListener() {
        viewModel.changeEvent.observe(viewLifecycleOwner, EventObserver {
            change(it)
        })
        viewModel.leaveEvent.observe(viewLifecycleOwner, EventObserver {
            leaveDialog()
        })
    }

    private fun start() {
        viewModel.start()
    }

    private fun change(data: ChangeData) {
        ProfileFragmentDirections.actionProfileFragmentToChangeFragment(data).show()
    }

    private fun leaveDialog() {
        openDialog(
            R.drawable.ic_leave,
            getString(R.string.do_you_want_to_leave),
            positive = { viewModel.leave() }
        )
    }
}
