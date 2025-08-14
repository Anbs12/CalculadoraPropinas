package com.example.deflatam_calculadorapropinas

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.deflatam_calculadorapropinas.databinding.ActivityMainBinding
import com.example.deflatam_calculadorapropinas.utils.CalculadoraUtils
import com.example.deflatam_calculadorapropinas.utils.SavePorcentaje

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var calculadoraUtils: CalculadoraUtils
    private lateinit var savePorcentaje: SavePorcentaje
    private var optionPersonalizada = false

    // Animaciones
    private lateinit var fadeInAnimation: Animation
    private lateinit var fadeOutAnimation: Animation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        cargaAnimaciones()
        initComponents()
        radioBtnListenerPropinaPersonalizada()
        mostrarPorcentajeAnterior()

    }

    private fun initComponents() {
        calculadoraUtils = CalculadoraUtils()
        savePorcentaje = SavePorcentaje(this)

        binding.btnCalcular.setOnClickListener {
            initCalculosNecesarios()
        }
        binding.btnLimpiar.setOnClickListener {
            limpiarCampos()
        }
    }

    private fun cargaAnimaciones() {
        // Carga de las animaciones
        fadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        fadeOutAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_out)
    }

    /** Muestra el porcentaje anterior guardado en SharedPreferences */
    @SuppressLint("SetTextI18n")
    private fun mostrarPorcentajeAnterior() {
        val porcentajeAnterior = savePorcentaje.getPorcentaje()
        if (porcentajeAnterior != 0f) {
            binding.txtAnteriorPorcentaje.text = "Tu anterior propina fue de: $porcentajeAnterior%"
            showViewWithFade(binding.txtAnteriorPorcentaje)
        }
    }

    /** Inicia los calculos luego al presionar el boton calcular*/
    private fun initCalculosNecesarios() {

        //Valores obtenidos de los campos
        val montoTotal: Double? = binding.inputMontoTotal.text.toString().toDoubleOrNull()
        val propinaSeleccionada = getPropinaSeleccionada()

        //Si se activara el modo propina personalizada
        modoPropinaPersonalizada()

        //Rellena los campos
        calculosTotalesYpropina(montoTotal = montoTotal, propina = propinaSeleccionada)

        //Guardamos la propina seleccionada
        savePorcentaje.savePorcentaje(propinaSeleccionada)
        mostrarPorcentajeAnterior()
    }

    private fun modoPropinaPersonalizada(){
        if (optionPersonalizada) {
            if (binding.edTxtCantidadPorcentajePersonalizada.text.toString()
                    .toDoubleOrNull() == null
            ) {
                message("Ingrese un monto personalizado")
                return
            }
        }
    }

    /**Calcula y asigna los resultados a los campos de texto*/
    private fun calculosTotalesYpropina(montoTotal: Double?, propina: Double) {
        //Calculos totales  y de propina
        try {

            if (montoTotal != null) {
                //Obtenemos resultados
                val textPropina = "$${calculadoraUtils.calcularPropina(montoTotal, propina)}"
                val textTotalFinal = "$${
                    calculadoraUtils.calcularTotalFinal(
                        montoTotal,
                        calculadoraUtils.calcularPropina(montoTotal, propina)
                    )
                }"
                //Asigna resultados a los campos
                binding.txtPropina.text = textPropina
                binding.txtTotal.text = textTotalFinal
                showViewWithFade(binding.txtPropina)
                showViewWithFade(binding.txtTotal)
                showViewWithFade(binding.propinaLayout)
                showViewWithFade(binding.totalLayout)
            } else {
                message("Ingrese un monto total")
            }

        } catch (e: Exception) {
            Log.e("Error", "Error al calcular ${e.message}")
            message("Error al calcular")
        }
    }

    /** retorna el monto de la propina seleccionada*/
    private fun getPropinaSeleccionada(): Double {
        return when (binding.radioBtnGroupPorcentaje.checkedRadioButtonId) {
            R.id.rBtn_10 -> 10.0
            R.id.rBtn_15 -> 15.0
            R.id.rBtn_20 -> 20.0
            R.id.rBtn_personalizado -> binding.edTxtCantidadPorcentajePersonalizada.text.toString()
                .toDoubleOrNull()
                ?: 0.0

            else -> {
                savePorcentaje.getPorcentaje().toDouble()
            }
        }
    }

    /**Controla la  visibilidad del campo de propina personalizada*/
    private fun radioBtnListenerPropinaPersonalizada() {
        binding.radioBtnGroupPorcentaje.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rBtn_personalizado -> {
                    optionPersonalizada = true
                    showViewWithFade(binding.edTxtCantidadPorcentajePersonalizada)
                }

                else -> {
                    optionPersonalizada = false
                    hideViewWithFade(binding.edTxtCantidadPorcentajePersonalizada)
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun limpiarCampos() {
        binding.inputMontoTotal.text.clear()
        binding.radioBtnGroupPorcentaje.clearCheck()

        hideViewWithFade(binding.txtTotal)
        hideViewWithFade(binding.txtPropina)
        hideViewWithFade(binding.propinaLayout)
        hideViewWithFade(binding.totalLayout)
    }

    /**Para mostrar mensajes al usuario*/
    private fun message(text: String) {
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