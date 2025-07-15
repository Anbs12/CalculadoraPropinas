package com.example.deflatam_calculadorapropinas.utils

import android.content.Context
import android.content.SharedPreferences

/**SharedPreferences para guardar el porcentaje de propina seleccionado por el usuario*/
class SavePorcentaje(context: Context) {

    /** Instancia de SharedPreferences para guardar la sesión del usuario */
    private val prefs: SharedPreferences = context.getSharedPreferences(
        "user_session_porcentaje", Context.MODE_PRIVATE
    )

    /** Guarda el porcentaje en SharedPreferences */
    fun savePorcentaje(porcentaje: Double) {
        val editor = prefs.edit()
        editor.putFloat("porcentaje", porcentaje.toFloat())
        editor.apply()
    }

    /** Obtiene el porcentaje de la propina anteriormente guardada */
    fun getPorcentaje(): Float {
        return prefs.getFloat("porcentaje", 0f)
    }


}