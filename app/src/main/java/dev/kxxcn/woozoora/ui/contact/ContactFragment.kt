package dev.kxxcn.woozoora.ui.contact

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import dev.kxxcn.woozoora.databinding.ContactFragmentBinding
import dev.kxxcn.woozoora.ui.base.BaseFragment
import dev.kxxcn.woozoora.ui.invite.InviteViewModel

class ContactFragment : BaseFragment<ContactFragmentBinding>() {

    override val viewModel by viewModels<InviteViewModel>(this::requireParentFragment)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = ContactFragmentBinding.inflate(
            inflater,
            container,
            false
        ).apply {
            this.viewModel = this@ContactFragment.viewModel
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListAdapter()
    }

    private fun setupListAdapter() {
        with(binding.contactList) {
            addItemDecoration(ContactSpacingDecoration())
            adapter = ContactAdapter(viewModel)
        }
    }
}
