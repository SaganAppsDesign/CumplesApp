package com.design.cumplepablo

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.design.cumplepablo.databinding.ActivityMainBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
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
    lateinit var hint: String
    lateinit var welcomeText: String
    private lateinit var database: DatabaseReference
    var firebaseDatabase: FirebaseDatabase? = null
    var name: String = ""
    var birthday: String = ""

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        val pref = getSharedPreferences("datos", MODE_PRIVATE)
        name = pref.getString("name", "").toString()
        birthday = pref.getString("birthday", "").toString()

        firebaseDatabase = FirebaseDatabase.getInstance("https://cumplesdepablo-default-rtdb.europe-west1.firebasedatabase.app/")
//        name = intent.getStringExtra("name").toString()
//        birthday = intent.getStringExtra("birthday").toString()
        hint = String.format(getString(R.string.hint), name)
        welcomeText = String.format(getString(R.string.texto_etiqueta), name)
        //Creando binding

            binding = ActivityMainBinding.inflate(layoutInflater)
            setContentView(binding.root)

            //Textos
            resultText = binding.cuadroTextoResultadoCalculo
            binding.cuadroTextoResultadoCalculo.hint = hint
            binding.etiquetaEncimaEditText.text = welcomeText
            textoYear = binding.textYear

            //Fecha
            fecha = binding.editTextFecha

            //Fotos
            foto = binding.carruselFotos
            foto.setCropToPadding(true)
            foto.setVisibility(View.VISIBLE)

            //Botón
            binding.btnCalculaEdad.setOnClickListener{ calculoEdad(it) }

            //Fondo de pantalla
            binding.ivBackground.setImageResource(R.drawable.fondocalculo)

            //Progress bar
            progressbar = binding.determinateBar
    }

   private fun calculoEdad (view: View) {

       progressbar.visibility = View.VISIBLE
       val resultFecha = datosFecha()
       val year = datosFecha() - birthday.toInt()
       val felicidades = String.format(getString(R.string.felicidades), name, year)
       val felicidades2 = String.format(getString(R.string.felicidades2), name, year)
       foto.setVisibility(View.VISIBLE)
       //Esconder teclado
       val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
       inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)

        when (resultFecha) {

            in -8000 until birthday.toInt() -> {
                resultText.text = getString(R.string.text1)
                getBirthdayImage("pablononacido")
            }

            birthday.toInt() -> {
                resultText.text = getString(R.string.text2)
                getBirthdayImage(HAPPYBIRTHDAY)
                foto.setOnClickListener{yearDescription(GIUSEPPE_VERDI_NAME, birthday.toInt())}
            }

            2013 -> {
                resultText.text = felicidades2
                getBirthdayImage(HAPPYBIRTHDAY)
                foto.setOnClickListener{yearDescription(GIUSEPPE_VERDI_NAME, birthday.toInt())}
            }
            2014 -> {
                resultText.text = felicidades2
                getBirthdayImage("pablobebe")
                foto.setOnClickListener{yearDescription(REY_NAME, birthday.toInt())}
            }
            2015 -> {
                resultText.text = felicidades + "\uD83D\uDE0D"
                getBirthdayImage("pablo2015")
                foto.setOnClickListener{yearDescription(ONU, birthday.toInt())}
            }
            2016 -> {
                resultText.text = felicidades + "\uD83D\uDE0D"
                getBirthdayImage("pablo2016")
                foto.setOnClickListener{yearDescription(REAL_MADRID, birthday.toInt())}
            }
            2017 -> {
                resultText.text = felicidades + "\uD83D\uDE0D"
                getBirthdayImage("pablo2017")
                foto.setOnClickListener{Toast.makeText(this, getString(R.string.no_data), Toast.LENGTH_LONG).show()}
                }

            2018 -> {
                resultText.text = felicidades + "\uD83D\uDE0D"
                getBirthdayImage(HAPPYBIRTHDAY)
                foto.setOnClickListener{Toast.makeText(this, getString(R.string.no_data), Toast.LENGTH_LONG).show()}
                }
            2019 -> {
                resultText.text = felicidades + "\uD83D\uDE0D"
                getBirthdayImage("pablo2019")
                foto.setOnClickListener{Toast.makeText(this, getString(R.string.no_data), Toast.LENGTH_LONG).show()}
            }
            2020 -> {
                resultText.text = felicidades + "\uD83D\uDE0D"
                getBirthdayImage(HAPPYBIRTHDAY)
                foto.setOnClickListener{Toast.makeText(this, getString(R.string.no_data), Toast.LENGTH_LONG).show()}
            }
            2021 -> {
                resultText.text = felicidades + "\uD83D\uDE0D"
                getBirthdayImage("pablo2021")
                foto.setOnClickListener{Toast.makeText(this, getString(R.string.no_data), Toast.LENGTH_LONG).show()}
            }
            2022 -> {
                resultText.text = felicidades + "\uD83D\uDE0D"
                getBirthdayImage(HAPPYBIRTHDAY)
                foto.setOnClickListener{Toast.makeText(this, getString(R.string.no_data), Toast.LENGTH_LONG).show()}
            }
            2023 -> {
                resultText.text = felicidades + "\uD83D\uDE0D"
                getBirthdayImage(HAPPYBIRTHDAY)
                foto.setOnClickListener{Toast.makeText(this, getString(R.string.no_data), Toast.LENGTH_LONG).show()}
            }

            2024 -> {
                resultText.text = felicidades + "\uD83D\uDE0D"
                getBirthdayImage(HAPPYBIRTHDAY)
                foto.setOnClickListener{Toast.makeText(this, getString(R.string.no_data), Toast.LENGTH_LONG).show()}
            }
            2025 -> {
                resultText.text = felicidades + "\uD83D\uDE0D"
                getBirthdayImage(HAPPYBIRTHDAY)
                foto.setOnClickListener{Toast.makeText(this, getString(R.string.no_data), Toast.LENGTH_LONG).show()}
            }
            in 2026..2029 -> {
                resultText.text =
                    "$felicidades. Estás en la etapa adolescente...\uD83D\uDE0E"
                getBirthdayImage("pabloadolescente")
                foto.setOnClickListener{Toast.makeText(this, getString(R.string.no_data), Toast.LENGTH_LONG).show()}
            }
            in 2030..2072 -> {
                resultText.text =
                    "$felicidades. Ya vas siendo una persona madurita... \uD83D\uDE0F"
               getBirthdayImage("pablomaduro")
               foto.setOnClickListener{Toast.makeText(this, getString(R.string.no_data), Toast.LENGTH_LONG).show()}
            }
            in 2073..2133 -> {
                resultText.text =
                    "$felicidades. ¡¡Ya eres un viejete!! \uD83D\uDE05"
                    getBirthdayImage("pabloanciano")
                    foto.setOnClickListener{Toast.makeText(this, getString(R.string.no_data), Toast.LENGTH_LONG).show()}
                }

            in 2134..6000 -> {
                resultText.text =
                    "$felicidades. Pero es imposible con la tecnología actual..." + "\uD83D\uDE14"
                    getBirthdayImage("pablo200")
                    foto.setOnClickListener{Toast.makeText(this, "No existen datos de momento. Estamos en ello", Toast.LENGTH_LONG).show()}
            }
            else -> {
                resultText.text = getString(R.string.introduce_fecha)
                foto.visibility = View.INVISIBLE
            }
        }
    }
    //Toma la fecha del input text
    private fun datosFecha(): Int {
        return if (fecha.text.isNotEmpty()) {
            val fechaString = fecha.text.toString().toInt()
            textoYear.text = "Año $fechaString"
            fechaString
        } else {
            textoYear.text = getString(R.string.calcula_tu_edad)
            Toast.makeText(this, "Introduce una fecha para continuar", Toast.LENGTH_SHORT).show()
            -9999
        }
    }

    private fun getBirthdayImage (name: String){
            val storage = Firebase.storage
            val storageRef = storage.reference
            val spaceRef = storageRef.child("imagenesCumple/$name.png")
            val localfile = File.createTempFile(name, "png")

            spaceRef.getFile(localfile).addOnSuccessListener {

                val bitmap = BitmapFactory.decodeFile(localfile.absolutePath)
                binding.carruselFotos.setImageBitmap(bitmap)
                progressbar.visibility = View.INVISIBLE

            }.addOnFailureListener {
                Toast.makeText(this, "Error cargando imagen", Toast.LENGTH_SHORT).show()
            }
    }

    private fun yearDescription (imagen: String, year: Int){
        database = firebaseDatabase!!.getReference("efemerides").child(year.toString()).child("title")
        database.get().addOnSuccessListener {
            Log.i("firebase", "Got value ${it.value}")
            val intent = Intent(this, DescriptionScreen::class.java).apply {
                putExtra("texto", it.value.toString())
                putExtra("imagen", imagen)
                putExtra("year", year)
            }
            startActivity(intent)

        }.addOnFailureListener{
            Log.e("firebase", "Error getting data", it)
        }
    }
}

