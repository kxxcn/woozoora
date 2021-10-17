package dev.kxxcn.woozoora.ui.sort

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import dev.kxxcn.woozoora.R
import dev.kxxcn.woozoora.common.EventObserver
import dev.kxxcn.woozoora.databinding.SortFragmentBinding
import dev.kxxcn.woozoora.di.SavedStateViewModelFactory
import dev.kxxcn.woozoora.ui.base.BaseFragment
import dev.kxxcn.woozoora.ui.edit.EditBranchType
import javax.inject.Inject

class SortFragment : BaseFragment<SortFragmentBinding>() {

    @Inject
    lateinit var viewModelFactory: SavedStateViewModelFactory

    override val viewModel by viewModels<SortViewModel> {
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
        binding = SortFragmentBinding.inflate(
            inflater,
            container,
            false
        ).apply {
            viewModel = this@SortFragment.viewModel
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListAdapter()
        setupListener()
    }

    private fun setupListAdapter() {
        with(binding.sortList) {
            addItemDecoration(SortSpacingDecoration())
            adapter = SortAdapter(viewModel)
        }
    }

    private fun setupListener() {
        viewModel.deleteEvent.observe(viewLifecycleOwner, EventObserver {
            confirmDeletion(it)
        })
        viewModel.createEvent.observe(viewLifecycleOwner, EventObserver {
            createCategory(it)
        })
    }

    private fun confirmDeletion(count: Int) {
        openDialog(
            R.drawable.ic_delete,
            getString(R.string.format_delete_category_detail, count),
            positive = { viewModel.deleteAll() }
        )
    }

    private fun createCategory(branch: EditBranchType) {
        SortFragmentDirections.actionSortFragmentToCreateFragment(branch).show()
    }
}
