package com.example.deflatam_calculadorapropinas

import com.example.deflatam_calculadorapropinas.utils.CalculadoraUtils
import junit.framework.TestCase.assertEquals
import org.junit.Test

class CalculadoraUtilsTests {

    private val calculadoraUtils = CalculadoraUtils()


    @Test
    fun `calculate the total final total of the tip + baseAmount`(){
        val montoTotal = 10.0
        val propina = 15.0
        val expectedTipAmount = 25.0

        assertEquals(expectedTipAmount, calculadoraUtils.calcularTotalFinal(montoTotal, propina))
    }

    @Test
    fun `calculate the tip from the total amount`(){
        val totalAmount = 100.0
        val tipPercent = 10.0
        val expectedTotal = 10.0

        assertEquals(expectedTotal, calculadoraUtils.calcularPropina(totalAmount, tipPercent))
    }

}