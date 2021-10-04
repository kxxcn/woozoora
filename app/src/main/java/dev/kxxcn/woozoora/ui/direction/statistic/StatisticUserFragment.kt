package dev.kxxcn.woozoora.ui.direction.statistic

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import dev.kxxcn.woozoora.common.*
import dev.kxxcn.woozoora.databinding.StatisticUserFragmentBinding

class StatisticUserFragment : Fragment() {

    private lateinit var binding: StatisticUserFragmentBinding

    private val viewModel by viewModels<StatisticViewModel>(this::requireParentFragment)

    private val userId by lazy { arguments?.getString(KEY_USER_ID) }
    private val userName by lazy { arguments?.getString(KEY_USER_NAME) }
    private val userProfile by lazy { arguments?.getString(KEY_USER_PROFILE) }
    private val userPrice by lazy { arguments?.getInt(KEY_USER_PRICE) }
    private val userAsset by lazy { arguments?.getInt(KEY_USER_ASSET) }
    private val transactionCount by lazy { arguments?.getInt(KEY_USER_TRANSACTION_COUNT) }
    private val paymentCash by lazy { arguments?.getInt(KEY_USER_PAYMENT_CASH) }
    private val paymentCard by lazy { arguments?.getInt(KEY_USER_PAYMENT_CARD) }
    private val usageRate by lazy { arguments?.getInt(KEY_USER_BUDGET_USAGE_RATE) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = StatisticUserFragmentBinding.inflate(
            inflater,
            container,
            false
        ).apply {
            name = userName
            profile = userProfile
            price = userPrice
            asset = userAsset
            count = transactionCount
            cash = paymentCash
            card = paymentCard
            rate = usageRate
        }
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        viewModel.setUserId(userId)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupBinding()
    }

    override fun onDestroyView() {
        Glide.with(this).clear(binding.profileImage)
        super.onDestroyView()
    }

    private fun setupBinding() {
        binding.lifecycleOwner = viewLifecycleOwner
    }
}
