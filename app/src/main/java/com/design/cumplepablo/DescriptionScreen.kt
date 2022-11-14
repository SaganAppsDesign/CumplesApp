package com.design.cumplepablo

import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.design.cumplepablo.databinding.ActivityDescriptionScreenBinding
import com.design.cumplepablo.databinding.ActivityMainBinding
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.File

class DescriptionScreen : AppCompatActivity() {

    private lateinit var binding : ActivityDescriptionScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDescriptionScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get the Intent that started this activity and extract the string
        val message = intent.getStringExtra("texto")
        binding.textDescription.text = message

        //Imagen efemérides
        val imagen = intent.getStringExtra("imagen")
        imagen?.let { getEfemeridesImage(it) }

        //Año efemérides

        val bundle2 = intent.extras
        val year = bundle2!!.getInt("year")

        binding.textEfemerides.setText("Efemérides año " + year)
    }

    private fun getEfemeridesImage (name: String){
        val storage = Firebase.storage
        val storageRef = storage.reference
        val spaceRef = storageRef.child("efemerides/$name.png")
        val localfile = File.createTempFile(name, "png")

        spaceRef.getFile(localfile).addOnSuccessListener {

            val bitmap = BitmapFactory.decodeFile(localfile.absolutePath)
            binding.imageDescription.setImageBitmap(bitmap)

        }.addOnFailureListener {
            Toast.makeText(this, "Error cargando imagen", Toast.LENGTH_SHORT).show()
        }

    }

}