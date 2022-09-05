package com.scw.twtour

import android.app.Application
import android.content.pm.PackageManager
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.decode.SvgDecoder
import coil.disk.DiskCache
import coil.memory.MemoryCache
import com.scw.twtour.di.module.*
import com.scw.twtour.util.MyDebugTree
import kotlinx.coroutines.FlowPreview
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

@FlowPreview
class MyApplication : Application(), ImageLoaderFactory {

    companion object {
        private lateinit var INSTANCE: MyApplication
        private lateinit var CLIENT_ID: String
        private lateinit var CLIENT_SECRET: String

        fun get() = INSTANCE
        fun getClientId() = CLIENT_ID
        fun getClientSecret() = CLIENT_SECRET
    }

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
        initTimber()
        initApiKey()
        initKoinModules()
    }

    private fun initTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(MyDebugTree())
        }
    }

    private fun initApiKey() {
        applicationContext.packageManager.getApplicationInfo(
            applicationContext.packageName,
            PackageManager.GET_META_DATA
        ).apply {
            CLIENT_ID = metaData["clientId"].toString()
            CLIENT_SECRET = metaData["clientSecret"].toString()
            Timber.i("Api Key id: $CLIENT_ID, secret: $CLIENT_SECRET")
        }
    }

    private fun initKoinModules() {
        startKoin {
            androidContext(this@MyApplication)
            modules(
                listOf(
                    apiModule,
                    dbModule,
                    viewModule,
                    repositoryModule,
                    useCaseModule,
                    dataSourceModule,
                    utilModule
                )
            )
        }
    }

    override fun newImageLoader(): ImageLoader {
        return ImageLoader.Builder(this)
            .crossfade(true)
            .placeholder(R.drawable.ic_baseline_image_24)
            .error(R.drawable.ic_baseline_image_not_supported_24)
            .crossfade(250)
            .components {
                add(SvgDecoder.Factory())
            }
            .memoryCache {
                MemoryCache.Builder(this)
                    .maxSizePercent(0.25)
                    .build()
            }
            .diskCache {
                DiskCache.Builder()
                    .directory(cacheDir.resolve("image_cache"))
                    .maxSizePercent(0.01)
                    .build()
            }
            .build()
    }
}