package com.android.project.lightweight.ui.navhostfragment

import android.content.Context
import android.content.IntentFilter
import androidx.navigation.fragment.NavHostFragment
import com.android.project.lightweight.network.NetworkBroadcastReceiver
import com.android.project.lightweight.ui.fragment.AppFragmentFactory
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainNavHostFragment : NavHostFragment() {
    @Inject
    lateinit var fragmentFactory: AppFragmentFactory

    override fun onAttach(context: Context) {
        super.onAttach(context)
        childFragmentManager.fragmentFactory = fragmentFactory
        IntentFilter("android.net.conn.CONNECTIVITY_CHANGE").also {
            context.registerReceiver(NetworkBroadcastReceiver, it)
        }
    }

    override fun onDetach() {
        super.onDetach()
        requireContext().unregisterReceiver(NetworkBroadcastReceiver)
    }
}