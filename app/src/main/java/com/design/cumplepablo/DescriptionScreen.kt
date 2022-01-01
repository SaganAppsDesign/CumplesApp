package com.design.cumplepablo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.design.cumplepablo.databinding.ActivityDescriptionScreenBinding
import com.design.cumplepablo.databinding.ActivityMainBinding

class DescriptionScreen : AppCompatActivity() {

    private lateinit var binding : ActivityDescriptionScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDescriptionScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get the Intent that started this activity and extract the string
        val message = intent.getStringExtra("texto")
        binding.textDescription.setText(message)

        //Imagen efemérides
        val bundle = intent.extras
        val imagen = bundle!!.getInt("imagen")

        binding.imageDescription.setImageResource(imagen)

        //Año efemérides

        val bundle2 = intent.extras
        val year = bundle2!!.getInt("year")

        binding.textEfemerides.setText("Efemérides año " + year)






    }
}