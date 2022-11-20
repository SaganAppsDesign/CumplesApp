package com.design.cumplepablo

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.design.cumplepablo.databinding.ActivityMainBinding
import com.github.chrisbanes.photoview.PhotoView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.io.File


class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    lateinit var resultText: TextView
    lateinit var fecha: TextView
    lateinit var foto: PhotoView
    lateinit var progressbar: ProgressBar
    lateinit var hint: String
    lateinit var welcomeText: String
    private lateinit var database: DatabaseReference
    var firebaseDatabase: FirebaseDatabase? = null
    var name: String = ""
    var birthday: String = ""
    var radius: Int = 0

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        val pref = getSharedPreferences("datos", MODE_PRIVATE)
        name = pref.getString("name", "").toString()
        birthday = pref.getString("birthday", "").toString()
        firebaseDatabase = FirebaseDatabase.getInstance("https://cumplesdepablo-default-rtdb.europe-west1.firebasedatabase.app/")
        hint = String.format(getString(R.string.hint), name)
        welcomeText = String.format(getString(R.string.texto_etiqueta), name)
        //Creando binding

            binding = ActivityMainBinding.inflate(layoutInflater)
            setContentView(binding.root)

            //Textos
            resultText = binding.cuadroTextoResultadoCalculo
            binding.cuadroTextoResultadoCalculo.hint = hint
            binding.etiquetaEncimaEditText.text = welcomeText

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
       val year = datosFecha() - birthday.toInt()
       val felicidades = String.format(getString(R.string.felicidades), name, year)
       val frase1 = String.format((getString(R.string.text1)), name, year)
       val frase2 = String.format((getString(R.string.text2)), name, year)
       foto.setVisibility(View.VISIBLE)
       //Esconder teclado
       val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
       inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)

        when (year) {

            in -8000 until 0 -> {
                resultText.text = frase1
                getBirthdayNoImage ("0000")
                foto.setOnClickListener{Toast.makeText(this, getString(R.string.no_data), Toast.LENGTH_LONG).show()}
            }
            0 -> {
                resultText.text = frase2
                getBirthdayNoImage (HAPPYBIRTHDAY)
                foto.setOnClickListener{yearDescription(datosFecha().toString(), datosFecha())}
            }
            in 1..13 -> {
                resultText.text = felicidades + "\uD83D\uDE0D"
                runBlocking {
                    getBirthdayImage(datosFecha().toString())
                }
                checkFireRef()
            }
            in 14..18 -> {
                resultText.text =  "$felicidades. Estás en la etapa adolescente...\uD83D\uDE0E"
                runBlocking {
                    getBirthdayImage(datosFecha().toString())
                }
                checkFireRef()
            }
            in 19..70 -> {
                resultText.text = "$felicidades. Ya vas siendo una persona madurita... \uD83D\uDE0F"
                runBlocking {
                    getBirthdayImage(datosFecha().toString())
                }
                checkFireRef()
            }
            in 71..100 -> {
                resultText.text =
                "$felicidades. ¡¡Se te ve joven todavía!! \uD83D\uDE05"
                runBlocking {
                    getBirthdayImage(datosFecha().toString())
                }
                checkFireRef()
            }
            in 100..6000 -> {
                resultText.text =
                "$felicidades. Pero es imposible con la tecnología actual..." + "\uD83D\uDE14"
                runBlocking {
                    getBirthdayNoImage("9999")
                }
                foto.setOnClickListener{Toast.makeText(this, getString(R.string.mayor_100), Toast.LENGTH_LONG).show()}
            }
            else -> {
                resultText.text = getString(R.string.introduce_fecha)
                foto.visibility = View.INVISIBLE
            }
        }
    }
    //Toma la fecha del input text
    private fun datosFecha(): Int {
       return if (fecha.text.isEmpty() || fecha.text.contains(".") || fecha.text.contains("/")
            || fecha.text.contains("*") || fecha.text.contains("-") || fecha.text.contains("+")){
           progressbar.visibility = View.INVISIBLE
           Toast.makeText(this, "Fecha no válida", Toast.LENGTH_SHORT).show()
           -9999
           } else {
           val fechaInt = fecha.text.toString().toInt()
           fechaInt
        }
    }

    private suspend fun getBirthdayImage (name: String){
            val storage = Firebase.storage
            val storageRef = storage.reference
            val spaceRef = withContext(Dispatchers.IO){storageRef.child("imagenesCumple/${datosFecha()}.png")}
            val localfile = File.createTempFile(name, "png")
            radius = 30
            spaceRef.getFile(localfile).addOnSuccessListener {

                Glide.with(this)
                    .load(localfile)
                    .transform(RoundedCorners(radius))
                    .fitCenter()
                    .into(binding.carruselFotos)

                progressbar.visibility = View.INVISIBLE

            }.addOnFailureListener {
                getBirthdayNoImage (HAPPYBIRTHDAY)
            }
    }

    private fun getBirthdayNoImage (name: String){
        val storage = Firebase.storage
        val storageRef = storage.reference
        val spaceRef = storageRef.child("imagenesCumple/$name.png")
        val localfile = File.createTempFile(name, "png")
        radius = 30
        spaceRef.getFile(localfile).addOnSuccessListener {
            Glide.with(this)
                .load(localfile)
                .transform(RoundedCorners(radius))
                .into(binding.carruselFotos)

            progressbar.visibility = View.INVISIBLE

        }.addOnFailureListener {
            Toast.makeText(this, getString(R.string.no_data), Toast.LENGTH_LONG).show()
        }
    }

    private fun yearDescription (imagen: String, year: Int){

        database.get().addOnSuccessListener {
           val intent = Intent(this, DescriptionScreen::class.java).apply {
                putExtra("texto", it.value.toString())
                putExtra("imagen", imagen)
                putExtra("year", year)
            }
            startActivity(intent)

        }.addOnFailureListener{
            Toast.makeText(this, getString(R.string.no_data), Toast.LENGTH_LONG).show()
        }
    }

    private fun checkFireRef() {
        database = firebaseDatabase!!.getReference("efemerides").child(datosFecha().toString()).child("title")
        database.get().addOnSuccessListener{
            if(it.value.toString().isNotEmpty()){
                foto.setOnClickListener{yearDescription(datosFecha().toString(), datosFecha())}
            } else {
                foto.setOnClickListener{Toast.makeText(this, getString(R.string.no_data), Toast.LENGTH_LONG).show()}
            }
          }.addOnFailureListener {
            Toast.makeText(this, getString(R.string.no_data), Toast.LENGTH_LONG).show()
        }
    }
}

