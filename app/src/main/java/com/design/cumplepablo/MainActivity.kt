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

    lateinit var resultText : TextView
    lateinit var fecha : TextView
    lateinit var foto : ImageView


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
        val roll_boton: Button = findViewById(R.id.boton1)

        //Fondo de pantalla
        val fondo : ImageView = findViewById(R.id.imagenCumple)
        fondo.setImageResource(R.drawable.cumple)

        roll_boton.setOnClickListener{calculoEdad()}
/*
        val drawableResource 0.= when (randomInt) {
            1 -> R.drawable.dice_1
            2 -> R.drawable.dice_2
            3 -> R.drawable.dice_3
            4 -> R.drawable.dice_4
            5 -> R.drawable.dice_5
            else -> R.drawable.dice_6
        }
*/

    }
//Functions

    private fun calculoEdad() {
        val fechaString = fecha.getText().toString();
        val fechaInt = fechaString.toInt()
        val result = fechaInt - 2013
        val resultString = result.toString()

        //val ramdomInt = (1..6).random()
        //resultText.text = ramdomInt.toString()
        //Log.i("Número aleatorio: ", ramdomInt.toString())
        //Toast.makeText(this, "¡¡Muchas Felicidades, Pablo!!", Toast.LENGTH_SHORT).show()
        if (result < 0){

            resultText.text = "No has nacido todavía Pablo ¡¡Disfruta de tu soledad cósmica!!"
            foto.setImageResource(R.drawable.pablononacido)

        } else if (result == 0) {

            resultText.text = "¡¡Acabas de nacer, Pablo!! ¡¡Bienvenido a este mundo!!"
            foto.setImageResource(R.drawable.pablobebe)
        } else if (result == 1) {
            resultText.text = "Felicidades Pablo, hoy cumples " + resultString + " año"
            foto.setImageResource(R.drawable.happybirthday)
        } else if (result == 2){
            resultText.text = "Felicidades Pablo, hoy cumples " + resultString + " años"
            foto.setImageResource(R.drawable.pablo2015)
        } else if (result == 3){
            resultText.text = "Felicidades Pablo, hoy cumples " + resultString + " años"
            foto.setImageResource(R.drawable.pablo2016)
        } else if (result == 4){
            resultText.text = "Felicidades Pablo, hoy cumples " + resultString + " años"
            foto.setImageResource(R.drawable.pablo2017)
        } else if (result == 5){
            resultText.text = "Felicidades Pablo, hoy cumples " + resultString + " años"
            foto.setImageResource(R.drawable.happybirthday)
        } else if (result == 6){
            resultText.text = "Felicidades Pablo, hoy cumples " + resultString + " años"
            foto.setImageResource(R.drawable.pablo2019)
        } else if (result == 7){
            resultText.text = "Felicidades Pablo, hoy cumples " + resultString + " años"
            foto.setImageResource(R.drawable.happybirthday)
        } else if (result == 8){
            resultText.text = "Felicidades Pablo, hoy cumples " + resultString + " años"
            foto.setImageResource(R.drawable.pablo2021)

        } else if (result > 8 && result < 18){
            resultText.text = "Felicidades Pablo, hoy cumples " + resultString + " años. Estás en la etapa adolescente..."
            foto.setImageResource(R.drawable.pabloadolescente)
        } else if (result >= 18 && result < 60){
            resultText.text = "Felicidades Pablo, hoy cumples " + resultString + " años. Ya eres un hombre hecho y derecho..."
            foto.setImageResource(R.drawable.pablomaduro)
        } else if (result >= 60 && result <= 120) {
            resultText.text = "Felicidades Pablo, hoy cumples " + resultString + " años. ¡¡Ya eres un viejete!!"
            foto.setImageResource(R.drawable.pabloanciano)
        } else {
            resultText.text = "Felicidades Pablo, hoy cumples " + resultString + " años. Pero es imposible..."
            foto.setImageResource(R.drawable.pablo200)
        }

    }


}