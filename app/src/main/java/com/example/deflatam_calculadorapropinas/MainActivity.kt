package com.example.deflatam_calculadorapropinas

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.deflatam_calculadorapropinas.utils.CalculadoraUtils
import com.example.deflatam_calculadorapropinas.utils.SavePorcentaje

class MainActivity : AppCompatActivity() {

    private lateinit var inputMontoTotal: EditText
    private lateinit var radioBtnGroupPorcentaje: RadioGroup
    private lateinit var txtPropina: TextView
    private lateinit var txtTotalFinal: TextView
    private lateinit var txtPropinaPersonalizada: TextView
    private lateinit var txtPorcentajeAnterior: TextView
    private lateinit var btnCalcular: Button
    private lateinit var btnLimpiar: Button
    private lateinit var calculadoraUtils: CalculadoraUtils
    private lateinit var savePorcentaje: SavePorcentaje
    private var optionPersonalizada = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        initComponents()
        radioBtnListenerPropinaPersonalizada()
        mostrarPorcentajeAnterior()

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
        txtPropinaPersonalizada = findViewById(R.id.edTxtCantidadPorcentajePersonalizada)
        txtPorcentajeAnterior = findViewById(R.id.txtAnteriorPorcentaje)
        btnCalcular = findViewById(R.id.btnCalcular)
        btnLimpiar = findViewById(R.id.btnLimpiar)
        calculadoraUtils = CalculadoraUtils()
        savePorcentaje = SavePorcentaje(this)
    }

    fun mostrarPorcentajeAnterior(){
        val porcentajeAnterior = savePorcentaje.getPorcentaje()
        if (porcentajeAnterior != 0f) {
            txtPorcentajeAnterior.text = "Tu anterior propina fue de: $porcentajeAnterior%"
            txtPorcentajeAnterior.visibility = TextView.VISIBLE
        }
    }

    fun initCalculosNecesarios() {

        //Valores obtenidos de los campos
        val montoTotal: Double? = inputMontoTotal.text.toString().toDoubleOrNull()
        val propinaSeleccionada = when (radioBtnGroupPorcentaje.checkedRadioButtonId) {
            R.id.rBtn_10 -> 10.0
            R.id.rBtn_15 -> 15.0
            R.id.rBtn_20 -> 20.0
            R.id.rBtn_personalizado -> txtPropinaPersonalizada.text.toString().toDoubleOrNull()
                ?: 0.0

            else -> 0.0
        }

        //Si se activara el modo propina personalizada
        if (optionPersonalizada == true) {
            if (txtPropinaPersonalizada.text.toString().toDoubleOrNull() == null) {
                message("Ingrese un monto personalizado")
                return
            }
            if (txtPropinaPersonalizada.text.toString()
                    .toDouble() < 5 || txtPropinaPersonalizada.text.toString().toDouble() > 99
            ) {
                message("Ingrese entre 5 y 99")
                return
            }
        }

        //Calculos totales  y de propina
        if (montoTotal == null) {
            message("Ingrese un monto total")
        } else {
            //Obtenemos resultados
            val textPropina =
                "Propina: $${calculadoraUtils.calcularPropina(montoTotal, propinaSeleccionada)}"
            val textTotalFinal = "Total a pagar: $${
                calculadoraUtils.calcularTotalFinal(
                    montoTotal,
                    calculadoraUtils.calcularPropina(montoTotal, propinaSeleccionada)
                )
            }"
            //Asigna resultados a los campos
            txtPropina.text = textPropina
            txtTotalFinal.text = textTotalFinal
            txtTotalFinal.visibility = TextView.VISIBLE
            txtPropina.visibility = TextView.VISIBLE
        }
        //Guardamos la propina seleccionada
        savePorcentaje.savePorcentaje(propinaSeleccionada)
        mostrarPorcentajeAnterior()
    }

    fun radioBtnListenerPropinaPersonalizada() {
        radioBtnGroupPorcentaje.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rBtn_personalizado -> {
                    optionPersonalizada = true
                    txtPropinaPersonalizada.visibility = TextView.VISIBLE
                }

                else -> {
                    optionPersonalizada = false
                    txtPropinaPersonalizada.visibility = TextView.GONE
                }
            }
        }
    }

    fun limpiarCampos() {
        inputMontoTotal.text.clear()
        radioBtnGroupPorcentaje.clearCheck()
        txtPropina.text = "Propina: $0.00"
        txtTotalFinal.text = "Total a pagar: $0.00"
        txtTotalFinal.visibility = TextView.GONE
        txtPropina.visibility = TextView.GONE
        txtPropinaPersonalizada.visibility = TextView.GONE
    }

    fun message(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }
}