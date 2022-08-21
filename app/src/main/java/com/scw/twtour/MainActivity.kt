package com.scw.twtour

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.scw.twtour.databinding.ActivityMainBinding
import com.scw.twtour.domain.AuthUseCase
import com.scw.twtour.util.*
import com.scw.twtour.view.viewmodel.MainViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.launch
import org.koin.android.ext.android.get
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import pub.devrel.easypermissions.EasyPermissions

@FlowPreview
class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController
    private lateinit var viewBinding: ActivityMainBinding

    private val viewModel by viewModel<MainViewModel> {
        parametersOf(get<AuthUseCase> {
            parametersOf(
                MyApplication.getClientId(),
                MyApplication.getClientSecret()
            )
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        setupActionBar(viewBinding.toolbar)
        collectData()
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onStart() {
        super.onStart()
        viewModel.init()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    private fun setupActionBar(toolbar: Toolbar) {
        setSupportActionBar(toolbar)
        viewBinding.toolbar.setTitleTextColor(Color.WHITE)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    fun setActionBarTitle(title: String) {
        viewBinding.toolbar.title = title
    }

    private fun collectData() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.syncState.collect { updateSyncingState(it) }
            }
        }
    }

    private fun updateSyncingState(state: SyncState) {
        when (state) {
            SyncStart -> {
                viewBinding.progressBar.visibility = View.VISIBLE
                viewBinding.syncMessage.text = ""
            }
            SyncComplete, SyncNone, is SyncError -> {
                viewBinding.progressBar.visibility = View.GONE
                viewBinding.syncMessage.text = ""
            }
            is SyncProgress -> {
                viewBinding.progressBar.progress = (state.progress * 10).toInt()
                viewBinding.syncMessage.text = "更新景點資料 - ${state.city.value}"
            }
        }
    }
}