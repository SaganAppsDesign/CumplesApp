package com.design.cumplepablo

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
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
import java.util.ArrayList


class MainActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    private lateinit var binding : ActivityMainBinding
    private lateinit var resultText: TextView
    private lateinit var foto: PhotoView
    private lateinit var progressbar: ProgressBar
    private lateinit var spinner: Spinner
    private lateinit var hint: String
    private lateinit var welcomeText: String
    private var database: DatabaseReference? = null
    private lateinit var inputMethodManager: InputMethodManager
    private var firebaseDatabase: FirebaseDatabase? = null
    private var storage = Firebase.storage
    private val storageRef = storage.reference
    private var yearSelected: Int = 0
    private lateinit var yearList: ArrayList<String>
    var name: String = ""
    var birthday: String = ""
    var radius: Int = 0


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        //Creando binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //getyearlistfrom splash
        yearList = intent.getStringArrayListExtra("yearList") as ArrayList<String>

        //Spinner
        spinner = binding.spYearList
        spinner.onItemSelectedListener = this
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            yearList
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner.adapter = adapter
        }

        //shared preferences
        val pref = getSharedPreferences("datos", MODE_PRIVATE)
        name = pref.getString("name", "").toString()
        birthday = pref.getString("birthday", "").toString()
        firebaseDatabase = FirebaseDatabase.getInstance("https://cumplesdepablo-default-rtdb.europe-west1.firebasedatabase.app/")
        hint = String.format(getString(R.string.hint), name)
        welcomeText = String.format(getString(R.string.texto_etiqueta), name)

        //Textos
        resultText = binding.cuadroTextoResultadoCalculo
        binding.cuadroTextoResultadoCalculo.hint = hint
        binding.etiquetaEncimaEditText.text = welcomeText

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
       val year = yearSelected - birthday.toInt()
       val congrats = String.format(getString(R.string.felicidades), name, year)
       val welcomeText = String.format((getString(R.string.text2)), name, year)
       foto.setVisibility(View.VISIBLE)
       //Esconder teclado
       inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
       inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)

        when (year) {
            0 -> {
                resultText.text = welcomeText
                getBirthdayNoImage (HAPPYBIRTHDAY)
                foto.setOnClickListener{yearDescription(yearSelected.toString(), yearSelected)}
            }
            in 1..13 -> {
                resultText.text = congrats + "\uD83D\uDE0D"
                runBlocking {
                    getBirthdayImage(yearSelected.toString())
                }
                checkFireRef()
            }
            in 14..18 -> {
                resultText.text =  "$congrats. Estás en la etapa adolescente...\uD83D\uDE0E"
                runBlocking {
                    getBirthdayImage(yearSelected.toString())
                }
                checkFireRef()
            }
            in 19..70 -> {
                resultText.text = "$congrats. Ya vas siendo una persona madurita... \uD83D\uDE0F"
                runBlocking {
                    getBirthdayImage(yearSelected.toString())
                }
                checkFireRef()
               }
            in 71..100 -> {
                resultText.text =
                "$congrats. ¡¡Se te ve joven todavía!! \uD83D\uDE05"
                runBlocking {
                    getBirthdayImage(yearSelected.toString())
                }
                checkFireRef()
            }
            in 100..6000 -> {
                resultText.text =
                "$congrats. Pero es imposible con la tecnología actual..." + "\uD83D\uDE14"
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

    private suspend fun getBirthdayImage (name: String){

            val spaceRef = withContext(Dispatchers.IO){storageRef.child("imagenesCumple/${yearSelected}.png")}
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
           radius = 30
           Glide.with(this)
                .load(R.drawable.happybirthday)
                .transform(RoundedCorners(radius))
                .into(binding.carruselFotos)
            progressbar.visibility = View.INVISIBLE
    }

    private fun yearDescription (imagen: String, year: Int){
        database?.get()?.addOnSuccessListener {
           val intent = Intent(this, DescriptionScreen::class.java).apply {
                putExtra("texto", it.value.toString())
                putExtra("imagen", imagen)
                putExtra("year", year)
            }
            startActivity(intent)

        }?.addOnFailureListener{
            Toast.makeText(this, getString(R.string.no_data), Toast.LENGTH_LONG).show()
        }
    }

    private fun checkFireRef() {
        database = firebaseDatabase!!.getReference("efemerides").child(yearSelected.toString()).child("title")
        database?.get()?.addOnSuccessListener{
            if(it.value.toString().isNotEmpty()){
                foto.setOnClickListener{yearDescription(yearSelected.toString(), yearSelected)}
            } else {
                foto.setOnClickListener{Toast.makeText(this, getString(R.string.no_data), Toast.LENGTH_LONG).show()}
            }
          }?.addOnFailureListener {
            Toast.makeText(this, getString(R.string.no_data), Toast.LENGTH_LONG).show()
        }
    }

    //Spinner
    override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
        yearSelected = parent?.getItemAtPosition(pos).toString().toInt()
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
            Log.i("hola", "hola")
    }
}

