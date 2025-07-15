package com.example.deflatam_calculadorapropinas

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.deflatam_calculadorapropinas.utils.CalculadoraUtils

class MainActivity : AppCompatActivity() {

    private lateinit var inputMontoTotal: EditText
    private lateinit var radioBtnGroupPorcentaje: RadioGroup
    private lateinit var txtPropina: TextView
    private lateinit var txtTotalFinal: TextView
    private lateinit var btnCalcular: Button
    private lateinit var btnLimpiar: Button
    private lateinit var calculadoraUtils: CalculadoraUtils

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        initComponents()

        btnCalcular.setOnClickListener {
            initCalculosNecesarios()
        }
        btnLimpiar.setOnClickListener {
            limpiarCampos()
        }
    }


    fun initComponents() {
        inputMontoTotal = findViewById(R.id.inputMontoTotal)
        radioBtnGroupPorcentaje = findViewById(R.id.radioBtnGroup_Porcentaje)
        txtPropina = findViewById(R.id.txtPropina)
        txtTotalFinal = findViewById(R.id.txtTotal)
        btnCalcular = findViewById(R.id.btnCalcular)
        btnLimpiar = findViewById(R.id.btnLimpiar)
        calculadoraUtils = CalculadoraUtils()
    }

    fun initCalculosNecesarios() {
        val montoTotal: Double? = inputMontoTotal.text.toString().toDoubleOrNull()
        val propinaSeleccionada = when (radioBtnGroupPorcentaje.checkedRadioButtonId) {
            R.id.rBtn_10 -> 10.0
            R.id.rBtn_15 -> 15.0
            R.id.rBtn_20 -> 20.0
            else -> 0.0
        }
        if (montoTotal == null) {
            message("Ingrese un monto total")
        } else {
            val textPropina =
                "Propina: $${calculadoraUtils.calcularPropina(montoTotal, propinaSeleccionada)}"
            val textTotalFinal = "Total a pagar: $${
                calculadoraUtils.calcularTotalFinal(
                    montoTotal,
                    calculadoraUtils.calcularPropina(montoTotal, propinaSeleccionada)
                )
            }"
            txtPropina.text = textPropina
            txtTotalFinal.text = textTotalFinal
            txtTotalFinal.visibility = TextView.VISIBLE
            txtPropina.visibility = TextView.VISIBLE
        }
    }

    fun limpiarCampos() {
        inputMontoTotal.text.clear()
        radioBtnGroupPorcentaje.clearCheck()
        txtPropina.text = "Propina: $0.00"
        txtTotalFinal.text = "Total a pagar: $0.00"
        txtTotalFinal.visibility = TextView.GONE
        txtPropina.visibility = TextView.GONE
    }

    fun message(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }
}