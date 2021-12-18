package com.design.cumplepablo

import android.app.Activity
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import org.w3c.dom.Text

class MainActivity : AppCompatActivity() {

    lateinit var resultText: TextView
    lateinit var fecha: TextView
    lateinit var foto: ImageView


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Texto
        resultText = findViewById(R.id.texto1)
        //Fecha
        fecha = findViewById(R.id.fecha)

        //Fotos
        foto = findViewById(R.id.fotopablo)

        //Botón
        val buttonCalc: Button
        buttonCalc = findViewById(R.id.boton1)
        buttonCalc.setOnClickListener { calculoEdad() }

        //Fondo de pantalla
        val fondo: ImageView = findViewById(R.id.imagenCumple)
        fondo.setImageResource(R.drawable.cumple)

    }
//Functions

   fun calculoEdad() {

        val resultFecha = datosFecha()
        val felicidades = "Felicidades Pablo, este año cumples $resultFecha primaveras"
        foto.setBackgroundColor(R.drawable.image_border)
        //val ramdomInt = (1..6).random()
        //resultText.text = ramdomInt.toString()
        //Log.i("Número aleatorio: ", ramdomInt.toString())


        when (resultFecha) {

            in -10000..-1 -> {
                resultText.text = "No has nacido todavía Pablo ¡¡Disfruta de tu soledad cósmica!!"
                foto.setImageResource(R.drawable.pablononacido)

            }
            0 -> {
                resultText.text = "¡¡Acabas de nacer, Pablo!! ¡¡Bienvenido a este mundo!!"
                foto.setImageResource(R.drawable.happybirthday)

            }
            1 -> {
                "Felicidades Pablo, hoy cumples " + resultFecha + " año"
                foto.setImageResource(R.drawable.pablobebe)

            }
            2 -> {
                resultText.text = felicidades
                foto.setImageResource(R.drawable.pablo2015)
            }
            3 -> {
                resultText.text = felicidades
                foto.setImageResource(R.drawable.pablo2016)
            }
            4 -> {
                resultText.text = felicidades
                foto.setImageResource(R.drawable.pablo2017)
            }
            5 -> {
                resultText.text = felicidades
                foto.setImageResource(R.drawable.happybirthday)
            }
            6 -> {
                resultText.text = felicidades
                foto.setImageResource(R.drawable.pablo2019)
            }
            7 -> {
                resultText.text = felicidades
                foto.setImageResource(R.drawable.happybirthday)
            }
            8 -> {
                resultText.text = felicidades
                foto.setImageResource(R.drawable.pablo2021)
            }
            in 9..17 -> {
                resultText.text =
                    "$felicidades. Estás en la etapa adolescente..."
                foto.setImageResource(R.drawable.pabloadolescente)
            }
            in 18..60 -> {
                resultText.text =
                    "$felicidades. Ya vas siendo una persona madurita..."
                foto.setImageResource(R.drawable.pablomaduro)
            }
            in 61..120 -> {
                resultText.text =
                    "$felicidades. ¡¡Ya eres un viejete!!"
                foto.setImageResource(R.drawable.pabloanciano)
            }

            in 121..6000 -> {
                resultText.text =
                    "$felicidades. Pero es imposible con la tecnología actual..."
                foto.setImageResource(R.drawable.pabloanciano)
            }
            else -> {
                resultText.text =
                    "Introduce valor correcto"
                foto.setImageResource(R.drawable.pablo200)
            }


        }



    }

    fun datosFecha(): Int {

        if (fecha.text.isNotEmpty()) {

            val fechaString = fecha.getText().toString();
            val fechaInt = fechaString.toInt()
            return fechaInt - 2013

        } else {Toast.makeText(this, "Introduce una fecha para continuar", Toast.LENGTH_SHORT).show()
            return 0
        }

    }




}