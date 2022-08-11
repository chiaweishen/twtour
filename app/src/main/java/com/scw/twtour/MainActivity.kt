package com.scw.twtour

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.scw.twtour.databinding.ActivityMainBinding
import com.scw.twtour.domain.AuthUseCase
import com.scw.twtour.util.*
import com.scw.twtour.view.viewmodel.MainViewModel
import kotlinx.coroutines.launch
import org.koin.android.ext.android.get
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class MainActivity : AppCompatActivity() {

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

        collectData()
    }

    override fun onStart() {
        super.onStart()
        viewModel.init()
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
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