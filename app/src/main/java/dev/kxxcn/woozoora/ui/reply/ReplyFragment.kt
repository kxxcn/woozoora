package dev.kxxcn.woozoora.ui.reply

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import dev.kxxcn.woozoora.common.EventObserver
import dev.kxxcn.woozoora.databinding.ReplyFragmentBinding
import dev.kxxcn.woozoora.ui.base.BaseFragment
import javax.inject.Inject

class ReplyFragment : BaseFragment<ReplyFragmentBinding>() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override val viewModel by viewModels<ReplyViewModel> { viewModelFactory }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = ReplyFragmentBinding.inflate(
            inflater,
            container,
            false
        ).apply {
            viewModel = this@ReplyFragment.viewModel
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
        viewModel.start()
    }

    private fun setupListAdapter() {
        with(binding.askList) {
            addItemDecoration(ReplySpacingDecoration())
            adapter = ReplyAdapter()
        }
    }

    private fun setupListener() {
        viewModel.askEvent.observe(viewLifecycleOwner, EventObserver {
            ask()
        })
    }

    private fun ask() {
        ReplyFragmentDirections.actionReplyFragmentToAskFragment().show()
    }
}
