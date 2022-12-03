package com.design.cumplepablo.activities

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.design.cumplepablo.ConnectionReceiver
import com.design.cumplepablo.ExtenFuncs.loadUrl
import com.design.cumplepablo.R
import com.design.cumplepablo.databinding.ActivityMainBinding
import com.github.chrisbanes.photoview.PhotoView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.io.File
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    private lateinit var binding : ActivityMainBinding
    private lateinit var resultText: TextView
    private lateinit var foto: PhotoView
    private lateinit var progressbar: ProgressBar
    private lateinit var spinner: Spinner
    private lateinit var info: String
    private lateinit var welcomeText: String
    private var database: DatabaseReference? = null
    private lateinit var inputMethodManager: InputMethodManager
    private var firebaseDatabase: FirebaseDatabase? = null
    private var storage = Firebase.storage
    private val storageRef = storage.reference
    private lateinit var auth: FirebaseAuth
    private var br: BroadcastReceiver = ConnectionReceiver()
    private var yearSelected: Int = 0
    private var yearList: ArrayList<String> = arrayListOf()
    private var name: String = ""
    private var birthday: Int = 0
    private var radius: Int = 0
    private var yearListSize: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        //Creando binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //getyearlistfrom splash
        yearList = intent.getStringArrayListExtra("yearList") as ArrayList<String>
        yearListSize = intent.getStringExtra("size").toString()
        if(yearListSize.toInt() > 1) binding.tvInfo.visibility = View.GONE

        //Spinner
        spinner = binding.spYearList
        spinner.onItemSelectedListener = this
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            yearList
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }

        //shared preferences
        val pref = getSharedPreferences("datos", MODE_PRIVATE)
        name = pref.getString("name", "").toString()
        birthday = pref.getInt("birthday", 0)
        firebaseDatabase = FirebaseDatabase.getInstance("https://cumplesdepablo-default-rtdb.europe-west1.firebasedatabase.app/")
        info = String.format(getString(R.string.hint), name)
        welcomeText = String.format(getString(R.string.texto_etiqueta), yearListSize)

        //Textos
        resultText = binding.tvCuadroCalculo
        binding.tvInfo.text = info
        binding.etiquetaEncimaEditText.text = welcomeText

        //Fotos
        foto = binding.carruselFotos
        foto.setCropToPadding(true)
        foto.setVisibility(View.VISIBLE)

        //Botón
        binding.btnCalculaEdad.setOnClickListener{
            calculoEdad(it)
            binding.tvCuadroCalculo.visibility = View.VISIBLE
            binding.tvInfo.visibility = View.GONE
        }

        //Botón back
        binding.btBack.setOnClickListener {
            val intent = Intent(this, OnBoarding::class.java)
            startActivity(intent)
        }

        //Fondo de pantalla
        binding.ivBackground.setImageResource(R.drawable.background)

        //Progress bar
        progressbar = binding.determinateBar
    }

    override fun onStart() {
        super.onStart()
        activeReceiver()
    }

   override fun onBackPressed() {}

   private fun calculoEdad (view: View) {
       progressbar.visibility = View.VISIBLE
       val year = yearSelected - birthday
       val congrats = String.format(getString(R.string.felicidades), name, year)
       val congrats2 = String.format(getString(R.string.felicidades2), name, year)
       val congrats3 = String.format(getString(R.string.felicidades3), name, year)
       val welcomeText = String.format((getString(R.string.text2)), name, year)
       foto.setVisibility(View.VISIBLE)

       val calendar = Calendar.getInstance()
       val currentYear = calendar[Calendar.YEAR]
       //Esconder teclado
       inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
       inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)

        when (year) {

            currentYear - birthday -> {
                resultText.text = congrats3
                checkFireRef()
            }
            0 -> {
                resultText.text = welcomeText
                checkFireRef()
            }
            1 -> {
                resultText.text = congrats
                checkFireRef()
            }
            in 2..70 -> {
                resultText.text = congrats2
                checkFireRef()
            }
            in 71..110 -> {
                resultText.text =
                "$congrats2. ¡Los años no pasan por ti!!"
                checkFireRef()
            }
            else -> {
                resultText.text = getString(R.string.introduce_fecha)
                foto.visibility = View.INVISIBLE
            }
        }
    }

    private suspend fun getBirthdayImage (name: String){
            auth = Firebase.auth
            val spaceRef = withContext(Dispatchers.IO){storageRef.child("imagenes/${auth.currentUser?.uid}/${yearSelected}.png")}
            val localfile = File.createTempFile(name, "png")
            radius = 30
            spaceRef.getFile(localfile).addOnSuccessListener {

                binding.carruselFotos.loadUrl(localfile, radius)
                progressbar.visibility = View.INVISIBLE

            }.addOnFailureListener {
                getBirthdayNoImage ()
            }
    }

    private fun getBirthdayNoImage (){
          radius = 30
          binding.carruselFotos.loadUrl(null, radius)
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
        runBlocking {
            getBirthdayImage(yearSelected.toString())
        }
        database = firebaseDatabase!!.getReference("efemerides").child(yearSelected.toString()).child("title")
        database?.get()?.addOnSuccessListener{
            if(it.value.toString().isNotEmpty()){
                foto.setOnClickListener{yearDescription(yearSelected.toString(), yearSelected)}
                initAnimation()
            } else {
                foto.setOnClickListener{Toast.makeText(this, getString(R.string.no_data), Toast.LENGTH_LONG).show()}
            }
          }?.addOnFailureListener {
            Toast.makeText(this, getString(R.string.no_data), Toast.LENGTH_SHORT).show()
        }
        initAnimation()
     }

    private fun initAnimation(){
        binding.animationView.visibility = View.VISIBLE
        binding.animationView.playAnimation()
     }

    //Spinner
    override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
        yearSelected = parent?.getItemAtPosition(pos).toString().toInt()
    }
    override fun onNothingSelected(p0: AdapterView<*>?) {
            Log.i("Error", "Error")
    }

    fun activeReceiver(){
        val networkIntentFilter = IntentFilter()
        networkIntentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION)
        registerReceiver(br, networkIntentFilter)
    }
}

