package dev.kxxcn.woozoora

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import dagger.android.support.DaggerAppCompatActivity
import dev.kxxcn.woozoora.databinding.WoozooraActivityBinding

class WoozooraActivity : DaggerAppCompatActivity() {

    private lateinit var binding: WoozooraActivityBinding

    private val hostFragment: Fragment
        get() = supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupBinding()
        setupLifecycle()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        currentFragment().onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun setupBinding() {
        binding = WoozooraActivityBinding
            .inflate(layoutInflater)
            .also { setContentView(it.root) }
    }

    private fun setupLifecycle() {
        binding.lifecycleOwner = this
    }

    private fun currentFragment(): Fragment {
        return hostFragment.childFragmentManager.fragments[0]
    }
}
