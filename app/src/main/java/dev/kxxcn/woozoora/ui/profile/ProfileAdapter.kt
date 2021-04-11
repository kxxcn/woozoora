package dev.kxxcn.woozoora.ui.profile

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import dev.kxxcn.woozoora.R
import dev.kxxcn.woozoora.domain.model.UserData
import dev.kxxcn.woozoora.ui.base.BaseAdapter
import dev.kxxcn.woozoora.ui.change.ChangeFilterType
import dev.kxxcn.woozoora.ui.profile.item.ProfileItem
import dev.kxxcn.woozoora.util.Converter

class ProfileAdapter(
    private val viewModel: ProfileViewModel,
) : BaseAdapter<ProfileItem, ProfileHolder>(ProfileDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileHolder {
        return ProfileHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ProfileHolder, position: Int) {
        holder.bind(viewModel, getItem(position))
    }

    class ProfileDiffCallback : DiffUtil.ItemCallback<ProfileItem>() {

        override fun areItemsTheSame(oldItem: ProfileItem, newItem: ProfileItem): Boolean {
            return oldItem.res == newItem.res
        }

        override fun areContentsTheSame(oldItem: ProfileItem, newItem: ProfileItem): Boolean {
            return oldItem == newItem
        }
    }

    companion object {

        fun create(user: UserData): List<ProfileItem> {
            return listOf(
                ProfileItem(
                    R.string.membership,
                    user.id
                ),
                ProfileItem(
                    R.string.name,
                    user.name
                ),
                ProfileItem(
                    R.string.email,
                    user.email
                ),
                ProfileItem(
                    R.string.birth_year,
                    user.year.toString(),
                    ChangeFilterType.YEAR
                ),
                ProfileItem(
                    R.string.budget,
                    Converter.moneyFormat(user.budget.toInt()),
                    ChangeFilterType.BUDGET
                )
            )
        }
    }
}
