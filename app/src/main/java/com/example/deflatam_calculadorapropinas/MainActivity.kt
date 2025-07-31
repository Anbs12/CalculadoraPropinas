package com.example.deflatam_calculadorapropinas

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.deflatam_calculadorapropinas.utils.CalculadoraUtils
import com.example.deflatam_calculadorapropinas.utils.SavePorcentaje
import androidx.core.view.isVisible

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

    // Animaciones
    private lateinit var fadeInAnimation: Animation
    private lateinit var fadeOutAnimation: Animation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Carga de las animaciones
        fadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        fadeOutAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_out)

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

    /** Muestra el porcentaje anterior guardado en SharedPreferences */
    @SuppressLint("SetTextI18n")
    fun mostrarPorcentajeAnterior(){
        val porcentajeAnterior = savePorcentaje.getPorcentaje()
        if (porcentajeAnterior != 0f) {
            txtPorcentajeAnterior.text = "Tu anterior propina fue de: $porcentajeAnterior%"
            //txtPorcentajeAnterior.visibility = TextView.VISIBLE
            showViewWithFade(txtPorcentajeAnterior)
        }
    }

    /** */
    fun initCalculosNecesarios() {
        val recuperarPorcentajeAnterior = savePorcentaje.getPorcentaje()
        //Valores obtenidos de los campos
        val montoTotal: Double? = inputMontoTotal.text.toString().toDoubleOrNull()
        val propinaSeleccionada = when (radioBtnGroupPorcentaje.checkedRadioButtonId) {
            R.id.rBtn_10 -> 10.0
            R.id.rBtn_15 -> 15.0
            R.id.rBtn_20 -> 20.0
            R.id.rBtn_personalizado -> txtPropinaPersonalizada.text.toString().toDoubleOrNull()
                ?: 0.0

            else -> {
                if (recuperarPorcentajeAnterior != 0f) {
                    recuperarPorcentajeAnterior.toDouble()
                } else {
                    0.0
                }
            }
        }

        //Si se activara el modo propina personalizada
        if (optionPersonalizada == true) {
            if (txtPropinaPersonalizada.text.toString().toDoubleOrNull() == null) {
                message("Ingrese un monto personalizado")
                return
            }/*
            if (txtPropinaPersonalizada.text.toString()
                    .toDouble() < 5 || txtPropinaPersonalizada.text.toString().toDouble() > 99
            ) {
                message("Ingrese entre 5 y 99")
                return
            }*/
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
            /*txtTotalFinal.visibility = TextView.VISIBLE
            txtPropina.visibility = TextView.VISIBLE*/
            showViewWithFade(txtTotalFinal)
            showViewWithFade(txtPropina)
        }
        //Guardamos la propina seleccionada
        savePorcentaje.savePorcentaje(propinaSeleccionada)
        mostrarPorcentajeAnterior()
    }

    /**Controla la  visibilidad del campo de propina personalizada*/
    fun radioBtnListenerPropinaPersonalizada() {
        radioBtnGroupPorcentaje.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rBtn_personalizado -> {
                    optionPersonalizada = true
                    //txtPropinaPersonalizada.visibility = TextView.VISIBLE
                    showViewWithFade(txtPropinaPersonalizada)
                }

                else -> {
                    optionPersonalizada = false
                    //txtPropinaPersonalizada.visibility = TextView.GONE
                    hideViewWithFade(txtPropinaPersonalizada)
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    fun limpiarCampos() {
        inputMontoTotal.text.clear()
        radioBtnGroupPorcentaje.clearCheck()
        /*txtPropina.text = "Propina: $0.00"
        txtTotalFinal.text = "Total a pagar: $0.00"
        txtTotalFinal.visibility = TextView.GONE
        txtPropina.visibility = TextView.GONE
        txtPropinaPersonalizada.visibility = TextView.GONE*/

        hideViewWithFade(txtTotalFinal)
        hideViewWithFade(txtPropina)
    }

    /**Para mostrar mensajes al usuario*/
    fun message(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }

    /** Muestra una vista con una animación de fade-in.
     * Si la vista ya es visible, simplemente la asegura.*/
    private fun showViewWithFade(view: View) {
        if (view.visibility != View.VISIBLE) {
            view.visibility = View.VISIBLE
            view.startAnimation(fadeInAnimation)
        }
    }

    /**Oculta una vista con una animacion de fade-out.
     * La vista se establece a GONE una vez que la animacion termina.*/
    private fun hideViewWithFade(view: View) {
        if (view.isVisible) {
            val currentFadeOutAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_out)
            currentFadeOutAnimation.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation?) {}
                override fun onAnimationEnd(animation: Animation?) {
                    view.visibility = View.GONE
                }
                override fun onAnimationRepeat(animation: Animation?) {}
            })
            view.startAnimation(currentFadeOutAnimation)
        }
    }

}