package dev.kxxcn.woozoora.ui.invite

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import dev.kxxcn.woozoora.ui.copy.CopyFragment
import dev.kxxcn.woozoora.ui.contact.ContactFragment

class InvitePageAdapter(
    fragment: Fragment,
) : FragmentStateAdapter(fragment.childFragmentManager, fragment.viewLifecycleOwner.lifecycle) {

    override fun getItemCount(): Int {
        return PAGE_COUNT
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            PAGE_CONTACT -> ContactFragment()
            else -> CopyFragment()
        }
    }

    companion object {

        private const val PAGE_COUNT = 2

        const val PAGE_CONTACT = 0
        const val PAGE_CODE = 1
    }
}
