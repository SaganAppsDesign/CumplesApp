package com.design.cumplepablo.activities

import android.R
import android.content.BroadcastReceiver
import android.content.Intent
import android.content.IntentFilter
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.design.cumplepablo.ConnectionReceiver
import com.design.cumplepablo.ExtenFuncs.batteryLevel
import com.design.cumplepablo.databinding.ActivityOnBoardingBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.util.*


class OnBoarding : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    private var name = ""
    private var birthday = 0
    private var yearItem = ""
    private var bool: Boolean = true
    private var yearsSpinner: ArrayList<String> = arrayListOf()
    private val calendar: Calendar = Calendar.getInstance()
    private val year = calendar[Calendar.YEAR]
    private var yearSelected: Int = 0
    private lateinit var spinner: Spinner
    private lateinit var binding : ActivityOnBoardingBinding
    private lateinit var auth: FirebaseAuth
    private var storage = Firebase.storage
    private val storageRef = storage.reference
    private lateinit var editor: SharedPreferences.Editor
    private var yearList: ArrayList<String> = arrayListOf()
    private var br: BroadcastReceiver = ConnectionReceiver()
    //Subir imagen a Storage de firebase
    private var imagePickerActivityResult: ActivityResultLauncher<Intent> =
          registerForActivityResult( ActivityResultContracts.StartActivityForResult()) { result ->
            if (result != null) {
               initAnimation()
               val imageUri: Uri? = result.data?.data
                if (imageUri == null){
                    binding.btSiguiente.isEnabled = true
                    binding.btSiguiente.alpha = 1F
                    finishAnimation()
                }
               val uploadTask = imageUri?.let { storageRef.child("imagenesTest/${auth.currentUser?.uid}/$yearSelected.png").putFile(it) }
                uploadTask?.addOnSuccessListener {
                    Toast.makeText(this, "Imagen subida correctamente", Toast.LENGTH_SHORT).show()
                    finishAnimation()
                    binding.btSiguiente.isEnabled = true
                    binding.btSiguiente.alpha = 1F
                    finishAnimation()
                    }?.addOnFailureListener {
                    Log.e("Firebase", "Image Upload fail")
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnBoardingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Initialize Firebase Auth
        auth = Firebase.auth

        initViews()
        batteryLevel(this, binding)

        //Shared preferences
        val pref = getSharedPreferences("datos", MODE_PRIVATE)
        binding.etPersonName.setText(pref.getString("name", ""))
        binding.etBirthday.setText(pref.getInt("birthday", 1950).toString())

        // Add a text changed listener to the EditText
        binding.etBirthday.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                Log.i("LOG afterTextChanged", "$s")
                val str: String = binding.etBirthday.text.toString()
                var number = 0
                number = try {
                    str.toInt()
                  } catch (e: NumberFormatException) {
                    0
                }
                if (number > year){
                    Toast.makeText(baseContext, "Se requiere un año igual o inferior al actual $year", Toast.LENGTH_SHORT).show()
                } else if(number < 1930){
                   Log.i("INFO control años", "No tenemos registros para ese año. Introduce uno mayor a 1930 o inferior al actual $year")
                } else Toast.makeText(baseContext, "Año correcto", Toast.LENGTH_SHORT).show()
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // This method is called to notify you that, within s,
                // the count characters beginning at start are about to be replaced by new text with length after.
                Log.i("LOG beforeTextChanged", "$s - $start - $count - $after")
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                Log.i("LOG onTextChanged", "$s - $start - $before - $count")

            }
        })

        binding.btRegister.setOnClickListener{
            name = binding.etPersonName.text.toString()
            birthday = binding.etBirthday.text.toString().toInt()
            if (binding.etBirthday.length()<4){
                checkDigitsYearNumber()
            } else {
                auth.signInAnonymously()
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, String.format(getString(com.design.cumplepablo.R.string.register_ok), name), Toast.LENGTH_LONG).show()
                        } else {
                            Toast.makeText(baseContext, "Authentication failed.", Toast.LENGTH_SHORT).show()
                        }
                    }
                activeViews()
            }
        }

        binding.btEdit.setOnClickListener {
            binding.etPersonName.alpha = 1F
            binding.etPersonName.isEnabled = true
            binding.etBirthday.alpha = 1F
            binding.etBirthday.isEnabled = true
            if (bool) {
                Toast.makeText(baseContext, "Introduce uno mayor a 1930 o inferior al actual $year", Toast.LENGTH_SHORT).show()
                binding.btEdit.text = "Finaliza edición"
                binding.etPersonName.isEnabled = true
                binding.etPersonName.alpha = 1F
                binding.etBirthday.isEnabled = true
                binding.etBirthday.alpha = 1F
                binding.tvLabel2.isEnabled = false
                binding.tvLabel2.alpha = 0.5F
                binding.spYears.isEnabled = false
                binding.spYears.alpha = 0.5F
                binding.btOpenPhoto.isEnabled = false
                binding.btOpenPhoto.alpha = 0.5F
                binding.btSiguiente.isEnabled = false
                binding.btSiguiente.alpha = 0.5F
                binding.animationView.alpha = 0.5F
                bool = false
            } else  {
                binding.btEdit.text = "Editar"
                activeViews()
                binding.btSiguiente.isEnabled = true
                binding.btSiguiente.alpha = 1F
                Toast.makeText(this, "Edición finalizada con éxito, $name", Toast.LENGTH_SHORT).show()
                bool = true
            }
        }

        binding.btOpenPhoto.setOnClickListener{
            val intent = Intent()
                .setType("*/*")
                .setAction(Intent.ACTION_GET_CONTENT)
                 imagePickerActivityResult.launch(intent)
                 binding.btSiguiente.isEnabled = false
                 binding.btSiguiente.alpha = 0.5F
              }

         binding.btSiguiente.setOnClickListener{
             name = binding.etPersonName.text.toString()
             birthday = binding.etBirthday.text.toString().toInt()
             editor = pref.edit()
             editor.putString("name", name)
             editor.putInt("birthday", birthday)
             editor.apply()
             getYearList()
           }
     }

    override fun onStart() {
        super.onStart()
        activeReceiver()
    }

    override fun onResume() {
        super.onResume()
        initSpinner()
        activeReceiver()
        batteryLevel(this, binding)
    }

    override fun onBackPressed() {}

    //Spinner
    override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
        yearSelected = parent?.getItemAtPosition(pos).toString().toInt()
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        Log.i("Error", "onNothingSelected")
    }

    private fun getYearList(){
        auth = Firebase.auth
        storageRef.child("imagenesTest/${auth.currentUser?.uid}")
            .listAll()
            .addOnSuccessListener {
                for (i in it.items){
                    yearItem = i.toString().substring(i.toString().length-8,i.toString().length-4)
                    yearList = (yearList.plus(yearItem)) as ArrayList<String>
                    val intent = Intent(this, MainActivity::class.java).apply {
                        putStringArrayListExtra("yearList", yearList)
                        putExtra("size",yearList.size.toString())
                    }
                    startActivity(intent)
                }
            }
            .addOnFailureListener {
                Log.e("yearList","Error charging list")
            }
     }

    private fun initViews(){
        auth = Firebase.auth
        Toast.makeText(baseContext, "Introduce uno mayor a 1930 o inferior al actual $year", Toast.LENGTH_LONG).show()
        if(auth.currentUser?.uid.isNullOrEmpty()){
            binding.btEdit.isEnabled = false
            binding.btEdit.alpha = 0.5F
            binding.tvLabel2.isEnabled = false
            binding.tvLabel2.alpha = 0.5F
            binding.spYears.isEnabled = false
            binding.spYears.alpha = 0.5F
            binding.btOpenPhoto.isEnabled = false
            binding.btOpenPhoto.alpha = 0.5F
            binding.btSiguiente.isEnabled = false
            binding.btSiguiente.alpha = 0.5F
            binding.animationView.alpha = 0.5F

          } else {
            binding.tvLabel.isEnabled = false
            binding.tvLabel.alpha = 0.5F
            binding.etPersonName.isEnabled = false
            binding.etPersonName.alpha = 0.5F
            binding.etBirthday.isEnabled = false
            binding.etBirthday.alpha = 0.5F
            binding.btRegister.isEnabled = false
            binding.btRegister.alpha = 0.5F
          }
        }

    private fun activeViews(){
        initSpinner()

        binding.tvLabel2.isEnabled = true
        binding.tvLabel2.alpha = 1F
        binding.spYears.isEnabled = true
        binding.spYears.alpha = 1F
        binding.btOpenPhoto.isEnabled = true
        binding.btOpenPhoto.alpha = 1F
        binding.animationView.alpha = 1F

        binding.tvLabel.isEnabled = false
        binding.tvLabel.alpha = 0.5F
        binding.etPersonName.isEnabled = false
        binding.etPersonName.alpha = 0.5F
        binding.etBirthday.isEnabled = false
        binding.etBirthday.alpha = 0.5F
        binding.btRegister.isEnabled = false
        binding.btRegister.alpha = 0.5F
    }
    private fun initSpinner(){
        var yearsSpinner: ArrayList<String> = arrayListOf()
        val calendar = Calendar.getInstance()
        val year = calendar[Calendar.YEAR]
        birthday = binding.etBirthday.text.toString().toInt()
        spinner = binding.spYears
        spinner.onItemSelectedListener = this

        for (i in birthday..year){
            yearsSpinner = (yearsSpinner.plus(i) as ArrayList<String>)
        }
        ArrayAdapter(
            this,
            R.layout.simple_spinner_item,
            yearsSpinner
        ).also { adapter ->
            adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }
    }

    private fun initAnimation(){
        binding.animationView.playAnimation()
        binding.animationView.repeatCount = 10
     }

    private fun finishAnimation(){
       binding.animationView.pauseAnimation()
    }

    private fun activeReceiver(){
        val networkIntentFilter = IntentFilter()
        networkIntentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION)
        registerReceiver(br, networkIntentFilter)
    }

    private fun checkDigitsYearNumber(){
        Toast.makeText(this, "Se requiere el año con 4 dígitos para poder continuar.", Toast.LENGTH_LONG).show()
        binding.tvLabel2.isEnabled = false
        binding.tvLabel2.alpha = 0.5F
        binding.spYears.isEnabled = false
        binding.spYears.alpha = 0.5F
        binding.btOpenPhoto.isEnabled = false
        binding.btOpenPhoto.alpha = 0.5F
        binding.btSiguiente.isEnabled = false
        binding.btSiguiente.alpha = 0.5F
        binding.animationView.alpha = 0.5F
    }

}