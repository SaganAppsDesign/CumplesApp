package com.design.cumplepablo

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.design.cumplepablo.databinding.ActivityMainBinding
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.File


class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    lateinit var resultText: TextView
    lateinit var textoYear: TextView
    lateinit var fecha: TextView
    lateinit var foto: ImageView
    lateinit var progressbar: ProgressBar

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        //Creando binding

            Log.i("info","OnCreate")
            binding = ActivityMainBinding.inflate(layoutInflater)
            setContentView(binding.root)

            //Textos
            resultText = binding.cuadroTextoResultadoCalculo
            textoYear = binding.textYear
            //Fecha
            fecha = binding.editTextFecha

            //Fotos
            foto = binding.carruselFotos
            foto.setCropToPadding(true)
            foto.setVisibility(View.VISIBLE)

            //Botón
            binding.calculoEdad.setOnClickListener { calculoEdad(it) }

            //Fondo de pantalla

            binding.imagenCumple2.setImageResource(R.drawable.fondocalculo)

            //Progress bar
            progressbar = binding.determinateBar

    }

    //Ciclo de vida
    override fun onStart() {
        super.onStart()
        Log.i("info","onStart")
    }


    override fun onRestart() {
        super.onRestart()
        Log.i("info","onRestart")
    }

    override fun onResume() {
        super.onResume()
        Log.i("info","OnResume")
    }

    override fun onPause() {
        super.onPause()
        Log.i("info","OnPause")
    }

    override fun onStop() {
        super.onStop()
        Log.i("info","OnStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i("info","OnDestroy")
    }
//Functions

   private fun calculoEdad (view: View) {

       progressbar.visibility = View.VISIBLE
       val resultFecha = datosFecha()
       val year = datosFecha() + 2013
       val felicidades = String.format(getString(R.string.felicidades), resultFecha)
       foto.setVisibility(View.VISIBLE)

       //Esconder teclado
       val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
       inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)

        when (resultFecha) {

            in -8000..-1 -> {
                resultText.text = "No has nacido todavía Pablo ¡¡Disfruta de tu soledad cósmica!!"
                getImagesFirebase("pablononacido")
            }
            0 -> {
                resultText.text = "¡¡Acabas de nacer, Pablo!! ¡¡Bienvenido a este mundo!!"
                getImagesFirebase("happybirthday")
                foto.setOnClickListener{yearDescription("En este año $year, en concreto el 10 de octubre, fue el bicentenario " +
                        "del nacimiento del compositor italiano Giuseppe Verdi.", R.drawable.giuseppeverdi, year)}
            }
            1 -> {
                "Felicidades Pablo, hoy cumples " + resultFecha + " año"
                getImagesFirebase("pablobebe")
                foto.setOnClickListener{Toast.makeText(this, "Prueba fecha = $resultFecha", Toast.LENGTH_SHORT).show()}
                foto.setOnClickListener{yearDescription("El 2 de junio de $year en Madrid (España), el rey de España, " +
                        "Juan Carlos I anuncia en mensaje oficial a las 13:00 que abdica en favor de su hijo el Príncipe Felipe, " +
                        "que reinará bajo el nombre de Felipe VI de España..", R.drawable.reyjuancarlos , year)}
            }
            2 -> {
                resultText.text = felicidades + "\uD83D\uDE0D"
                getImagesFirebase("pablo2015")
                foto.setOnClickListener{yearDescription("El 9 de mayo de $year la Organización Mundial de la Salud declara que el brote de la " +
                        "epidemia de Ebola en Liberia ha terminado, después de más de un año.", R.drawable.onu , year)}
            }
            3 -> {
                resultText.text = felicidades + "\uD83D\uDE0D"
                getImagesFirebase("pablo2016")
                foto.setOnClickListener{yearDescription("El 28 de mayo de $year, el Real Madrid gana su undécima Liga de Campeones de la UEFA tras vencer " +
                        "en los penalties 5-3 al Atlético de Madrid.", R.drawable.realmadrid , year)}
            }
            4 -> {
                resultText.text = felicidades + "\uD83D\uDE0D"
                getImagesFirebase("pablo2017")
                }

            5 -> {
                resultText.text = felicidades + "\uD83D\uDE0D"
                getImagesFirebase("happybirthday")
                foto.setImageResource(R.drawable.happybirthday)
            }
            6 -> {
                resultText.text = felicidades + "\uD83D\uDE0D"
                getImagesFirebase("pablo2019")

            }
            7 -> {
                resultText.text = felicidades + "\uD83D\uDE0D"
                getImagesFirebase("happybirthday")

            }
            8 -> {
                resultText.text = felicidades + "\uD83D\uDE0D"
                getImagesFirebase("pablo2021")

            }
            9 -> {
                resultText.text = felicidades + "\uD83D\uDE0D"
                getImagesFirebase("happybirthday")

            }
            10 -> {
                resultText.text = felicidades + "\uD83D\uDE0D"
                getImagesFirebase("happybirthday")
            }

            11 -> {
                resultText.text = felicidades + "\uD83D\uDE0D"
                getImagesFirebase("happybirthday")
            }
            12 -> {
                resultText.text = felicidades + "\uD83D\uDE0D"
                getImagesFirebase("happybirthday")
            }
            in 13..17 -> {
                resultText.text =
                    "$felicidades. Estás en la etapa adolescente...\uD83D\uDE0E"
                getImagesFirebase("pabloadolescente")
            }
            in 18..60 -> {
                resultText.text =
                    "$felicidades. Ya vas siendo una persona madurita... \uD83D\uDE0F"
               getImagesFirebase("pablomaduro")

            }
            in 61..120 -> {
                resultText.text =
                    "$felicidades. ¡¡Ya eres un viejete!! \uD83D\uDE05"
                    getImagesFirebase("pabloanciano")
                }

            in 121..6000 -> {
                resultText.text =
                    "$felicidades. Pero es imposible con la tecnología actual..." + "\uD83D\uDE14"
               getImagesFirebase("pablo200")
            }
            else -> {
                resultText.text = getString(R.string.introduce_fecha)
                foto.visibility = View.INVISIBLE
            }
        }
    }

    //Toma la fecha del input text
    private fun datosFecha(): Int {

        if (fecha.text.isNotEmpty()) {

            val fechaString = fecha.getText().toString();
            val fechaInt = fechaString.toInt()
            textoYear.text = "Año " + fechaInt.toString()
            return fechaInt - 2013

        } else {
            textoYear.text = getString(R.string.calcula_tu_edad)
            Toast.makeText(this, "Introduce una fecha para continuar", Toast.LENGTH_SHORT).show()
            return -9999
        }
    }

    private fun getImagesFirebase (name: String){

            //Firebase
            val storage = Firebase.storage
            val storageRef = storage.reference
            val spaceRef = storageRef.child("$name.png")

            val localfile = File.createTempFile(name, "png")

            spaceRef.getFile(localfile).addOnSuccessListener {

                val bitmap = BitmapFactory.decodeFile(localfile.absolutePath)
                binding.carruselFotos.setImageBitmap(bitmap)
                progressbar.visibility = View.INVISIBLE

            }.addOnFailureListener {
                Toast.makeText(this, "Error cargando imagen", Toast.LENGTH_SHORT).show()
            }

    }

    private fun yearDescription (texto: String, imagen: Int, year: Int){

               val intent = Intent(this, DescriptionScreen::class.java).apply {
                    putExtra("texto", texto)
                    putExtra("imagen", imagen)
                    putExtra("year", year)
        }
        startActivity(intent)
    }
}

