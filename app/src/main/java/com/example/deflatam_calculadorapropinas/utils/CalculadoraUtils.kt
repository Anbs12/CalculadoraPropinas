package com.example.deflatam_calculadorapropinas.utils

class CalculadoraUtils {

    /**Calcula total final a partir de monto total y propina calculada*/
    fun calcularTotalFinal(montoTotal: Double, propina: Double): Double {
        return montoTotal + propina
    }

    /**Calcula propina a partir de monto total y porcentaje ingresado*/
    fun calcularPropina(montoTotal: Double, porcentaje: Double): Double {
        return montoTotal * (porcentaje / 100)
    }

}