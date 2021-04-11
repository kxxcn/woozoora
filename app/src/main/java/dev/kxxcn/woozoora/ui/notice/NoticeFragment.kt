package dev.kxxcn.woozoora.ui.notice

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import dev.kxxcn.woozoora.common.EventObserver
import dev.kxxcn.woozoora.common.extension.smoothScrollForceToPosition
import dev.kxxcn.woozoora.databinding.NoticeFragmentBinding
import dev.kxxcn.woozoora.ui.base.BaseFragment
import javax.inject.Inject

class NoticeFragment : BaseFragment<NoticeFragmentBinding>() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override val viewModel by viewModels<NoticeViewModel> { viewModelFactory }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = NoticeFragmentBinding.inflate(
            inflater,
            container,
            false
        ).apply {
            viewModel = this@NoticeFragment.viewModel
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListAdapter()
        setupListener()
    }

    private fun setupListAdapter() {
        binding.noticeList.adapter = NoticeAdapter(viewModel)
    }

    private fun setupListener() {
        viewModel.scrollEvent.observe(viewLifecycleOwner, EventObserver {
            scroll(it)
        })
    }

    private fun scroll(position: Int) {
        binding.noticeList.smoothScrollForceToPosition(position)
    }
}
