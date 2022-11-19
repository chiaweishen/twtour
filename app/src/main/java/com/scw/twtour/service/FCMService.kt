package com.scw.twtour.service

import android.content.Intent
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import timber.log.Timber

class FCMService : FirebaseMessagingService() {
    override fun handleIntent(intent: Intent?) {
        super.handleIntent(intent)
        Timber.tag("fcm").i("handleIntent ${intent?.also { it.dataString }}")
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        Timber.tag("fcm").i("onMessageReceived $message")
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Timber.tag("fcm").i("onNewToken $token")
    }
}