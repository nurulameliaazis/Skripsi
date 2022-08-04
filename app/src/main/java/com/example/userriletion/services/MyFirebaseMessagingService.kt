//package com.example.userriletion.services
//
//import android.util.Log
//import androidx.core.app.NotificationCompat
//import androidx.core.app.NotificationManagerCompat
//import com.google.firebase.messaging.FirebaseMessagingService
//import com.google.firebase.messaging.RemoteMessage
//
//
//class MyFirebaseMessagingService: FirebaseMessagingService() {
//    override fun onNewToken(p0: String) {
//        super.onNewToken(p0)
//
//     val refreshToken = FirebaseInstanceId.getInstance().token
//        Log.e("refresh token", refreshToken)
//    }
//
//    override fun onMessageReceived(p0: RemoteMessage) {
//        super.onMessageReceived(p0)
//        showNotification(p0.notification?.title.toString(), p0.notification?.body.toString())
//    }
//    fun showNotification(title: String, massage: String) {
//        val builder= NotificationCompat.Builder(this, "test-notif")
//            .setContentTitle(title)
//            .setContentText(massage)
//
//        val manager = NotificationManagerCompat.from(this)
//        manager.notify(222, builder.build())
//    }
//}