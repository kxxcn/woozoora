package dev.kxxcn.woozoora.ui.contact

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.firebase.dynamiclinks.DynamicLink
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks
import com.google.firebase.dynamiclinks.ktx.*
import dev.kxxcn.woozoora.R
import dev.kxxcn.woozoora.common.*
import dev.kxxcn.woozoora.databinding.ContactFragmentBinding
import dev.kxxcn.woozoora.domain.model.UserData
import dev.kxxcn.woozoora.ui.base.BaseFragment
import dev.kxxcn.woozoora.ui.contact.item.ContactItem
import dev.kxxcn.woozoora.util.Permission
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ContactFragment : BaseFragment<ContactFragmentBinding>() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var dynamicLinks: FirebaseDynamicLinks

    override val viewModel by viewModels<ContactViewModel> { viewModelFactory }

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
            viewModel = this@ContactFragment.viewModel
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListAdapter()
        setupListener()
        requestPermission()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            when (requestCode) {
                REQUEST_CODE_PERMISSION_READ_CONTACT -> getContacts()
            }
        } else {
            toastAndClose()
        }
    }

    private fun setupListAdapter() {
        with(binding.contactList) {
            addItemDecoration(ContactSpacingDecoration())
            adapter = ContactAdapter(viewModel)
        }
    }

    private fun setupListener() {
        viewModel.inviteEvent.observe(viewLifecycleOwner, EventObserver {
            invite(it)
        })
    }

    private fun requestPermission() {
        Permission.request(
            requireActivity(),
            REQUEST_CODE_PERMISSION_READ_CONTACT,
            Manifest.permission.READ_CONTACTS
        )
    }

    private fun toastAndClose() {
        lifecycleScope.launch {
            toast(R.string.you_will_not_be_able_to_use_the_invitation)
            delay(DURATION_HALF_SECONDS)
            close()
        }
    }

    private fun getContacts() {
        lifecycleScope.launch(Dispatchers.IO) {
            requireActivity().contentResolver?.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null,
                null,
                null,
                ContactsContract.Contacts.DISPLAY_NAME
            )?.use {
                val contacts = mutableListOf<ContactItem>()

                while (it.moveToNext()) {
                    val name =
                        it.getString(it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
                    val phone =
                        it.getString(it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                            .replace("-".toRegex(), "")
                            .replace("\\+82".toRegex(), "0")
                            .replace(" ".toRegex(), "")

                    contacts.add(ContactItem(name, phone))
                }

                withContext(Dispatchers.Main) { viewModel.contact(contacts) }
            }
        }
    }

    private fun invite(pair: Pair<ContactItem, UserData>) {
        loading(true, LOTTIE_FILE_INVITE)
        val (invite, user) = pair
        val inviteLink = getString(R.string.format_invite_link, user.name, user.id)
        val prefixLink = getString(R.string.prefix_invite_link)

        dynamicLinks.shortLinkAsync {
            link = Uri.parse(inviteLink)
            domainUriPrefix = prefixLink
            setAndroidParameters(
                DynamicLink.AndroidParameters
                    .Builder(requireActivity().packageName)
                    .build()
            )
        }.addOnSuccessListener { (shortLink, _) ->
            lifecycleScope.launch {
                delay(DURATION_TWO_SECONDS)
                loading(false)
                val subject = getString(R.string.woozoora_invitation)
                val name = getString(R.string.format_invite_name, user.name)
                val linkDesc = getString(R.string.connect_via_the_link_below)
                val warning = getString(R.string.invite_warning)
                val body = buildString {
                    appendLine(name)
                    appendLine()
                    appendLine(linkDesc)
                    appendLine(shortLink?.toString())
                    appendLine()
                    append(warning)
                }
                val intent = Intent(Intent.ACTION_SENDTO).apply {
                    putExtra("subject", subject)
                    putExtra("sms_body", body)
                    data = Uri.parse(getString(R.string.format_sms_to, invite.phone))
                }

                startActivity(intent)
            }
        }
    }
}
