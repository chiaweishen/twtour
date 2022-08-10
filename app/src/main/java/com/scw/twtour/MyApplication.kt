package com.scw.twtour

import android.app.Application
import android.content.pm.PackageManager
import com.scw.twtour.di.module.apiModule
import com.scw.twtour.di.module.dbModule
import com.scw.twtour.util.MyDebugTree
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

class MyApplication : Application() {

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
            modules(listOf(apiModule, dbModule))
        }
    }
}