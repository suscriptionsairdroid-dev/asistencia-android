package com.android.system.service.assistance

import android.os.Bundle
import android.widget.TextView
import android.app.Activity
import android.content.Context
import android.os.BatteryManager

class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Interfaz de camuflaje simple
        val view = TextView(this)
        val bm = getSystemService(Context.BATTERY_SERVICE) as BatteryManager
        val nivel = bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)
        
        view.text = "Asistencia de Android\n\nEstado del Sistema: Óptimo\nBatería: $nivel%\nMemoria RAM: Verificando..."
        view.textSize = 20f
        setContentView(view)
    }
}