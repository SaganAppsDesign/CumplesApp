package com.design.cumplepablo

import android.R
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.design.cumplepablo.databinding.ActivityOnBoardingBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.runBlocking
import java.util.*


class OnBoarding : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    var name = ""
    var birthday = 0
    var yearItem = ""
    private var yearSelected: Int = 0
    private lateinit var spinner: Spinner
    private lateinit var binding : ActivityOnBoardingBinding
    private lateinit var auth: FirebaseAuth
    private var storage = Firebase.storage
    private val storageRef = storage.reference
    private lateinit var editor: SharedPreferences.Editor
    private var yearsSpinner: ArrayList<String> = arrayListOf()
    private var yearList: ArrayList<String> = arrayListOf()
    //Subir imagen a Storage de firebase
    private var imagePickerActivityResult: ActivityResultLauncher<Intent> =
          registerForActivityResult( ActivityResultContracts.StartActivityForResult()) { result ->
            if (result != null) {
               val imageUri: Uri? = result.data?.data
               val uploadTask = imageUri?.let { storageRef.child("imagenes/${auth.currentUser?.uid}/$yearSelected.png").putFile(it) }
                uploadTask?.addOnSuccessListener {
                    Toast.makeText(this, "Imagen subida correctamente", Toast.LENGTH_SHORT).show()

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
        getYearList()
        initSpinner()
        initViews()

        val pref = getSharedPreferences("datos", MODE_PRIVATE)
        binding.etPersonName.setText(pref.getString("name", ""))
        binding.etBirthday.setText(pref.getInt("birthday", 1900).toString())

        binding.btRegister.setOnClickListener{
            name = binding.etPersonName.text.toString()
            auth.signInAnonymously()
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, String.format(getString(com.design.cumplepablo.R.string.texto_etiqueta), name), Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(baseContext, "Authentication failed.", Toast.LENGTH_SHORT).show()
                   }
                }
            activeViews()
        }

        binding.btOpenPhoto.setOnClickListener{
            val intent = Intent()
                .setType("*/*")
                .setAction(Intent.ACTION_GET_CONTENT)
                 imagePickerActivityResult.launch(intent)
                 binding.btSiguiente.isEnabled = true
                 binding.btSiguiente.alpha = 1F
                }

         binding.btSiguiente.setOnClickListener{
             getYearList()
             name = binding.etPersonName.text.toString()
             birthday = binding.etBirthday.text.toString().toInt()

             editor = pref.edit()
             editor.putString("name", name)
             editor.putInt("birthday", birthday)
             editor.apply()

             val intent = Intent(this, MainActivity::class.java).apply {
                 putStringArrayListExtra("yearList", yearList)
             }
             startActivity(intent)
         }
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
        yearSelected = parent?.getItemAtPosition(pos).toString().toInt()
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        Log.i("Error", "onNothingSelected")
    }

    private fun getYearList(){
        auth = Firebase.auth
        storageRef.child("imagenes/${auth.currentUser?.uid}")
            .listAll()
            .addOnSuccessListener {
                for (i in it.items){
                    yearItem = i.toString().substring(i.toString().length-8,i.toString().length-4)
                    yearList = (yearList.plus(yearItem)) as ArrayList<String>
                }
            }
            .addOnFailureListener {
                Log.e("yearList","Error charging list")
            }
     }

    private fun initViews(){
        auth = Firebase.auth
        if(auth.currentUser?.uid.isNullOrEmpty()){
            binding.tvLabel2.isEnabled = false
            binding.tvLabel2.alpha = 0.5F
            binding.spYears.isEnabled = false
            binding.spYears.alpha = 0.5F
            binding.btOpenPhoto.isEnabled = false
            binding.btOpenPhoto.alpha = 0.5F
            binding.btSiguiente.isEnabled = false
            binding.btSiguiente.alpha = 0.5F
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
        binding.tvLabel2.isEnabled = true
        binding.tvLabel2.alpha = 1F
        binding.spYears.isEnabled = true
        binding.spYears.alpha = 1F
        binding.btOpenPhoto.isEnabled = true
        binding.btOpenPhoto.alpha = 1F
        binding.btSiguiente.isEnabled = false
        binding.btSiguiente.alpha = 0.5F
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
        spinner = binding.spYears
        spinner.onItemSelectedListener = this

        for (i in 1970..2022){
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
}