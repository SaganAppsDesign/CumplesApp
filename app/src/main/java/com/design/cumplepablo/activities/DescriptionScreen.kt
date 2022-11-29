package com.design.cumplepablo.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.design.cumplepablo.R
import com.design.cumplepablo.databinding.ActivityDescriptionScreenBinding
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.io.File

class DescriptionScreen : AppCompatActivity() {

    var radius: Int = 0
    var message = ""
    var imagen = ""
    var year = 0
    private lateinit var binding : ActivityDescriptionScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDescriptionScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get the Intent that started this activity and extract the string
        message = intent.getStringExtra("texto").toString()
        binding.textDescription.text = message
        //Imagen efemérides
        imagen = intent.getStringExtra("imagen").toString()
        //Año efemérides
        year = intent.getIntExtra("year", 0)
        binding.textEfemerides.text = "Efemérides año " + year

        runBlocking {
            imagen?.let {getEfemeridesImage(it)}
        }

        //Botón back
        binding.btBack.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
     }

    override fun onBackPressed() {}

    private suspend fun getEfemeridesImage (name: String){
        val storage = Firebase.storage
        val storageRef = storage.reference
        val spaceRef = withContext(Dispatchers.IO){(storageRef.child("efemerides/$name.png"))}
        val localfile = File.createTempFile(name, "png")
        radius = 30

        spaceRef.getFile(localfile).addOnSuccessListener {
            Glide.with(this)
                .load(localfile)
                .transform(RoundedCorners(radius))
                .fitCenter()
                .into(binding.imageDescription)

            }.addOnFailureListener {
            Toast.makeText(this, getString(R.string.no_data), Toast.LENGTH_LONG).show()
        }
    }
}