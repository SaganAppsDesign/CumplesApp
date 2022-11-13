package com.design.cumplepablo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.design.cumplepablo.databinding.ActivityMainBinding
import com.design.cumplepablo.databinding.ActivityOnBoardingBinding

class OnBoarding : AppCompatActivity() {

    var name = ""

    private lateinit var binding : ActivityOnBoardingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnBoardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btSiguiente.setOnClickListener{
            name = binding.etPersonName.text.toString()
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("name", name)
            startActivity(intent)
        }
    }
}