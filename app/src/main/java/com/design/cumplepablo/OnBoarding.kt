package com.design.cumplepablo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.design.cumplepablo.databinding.ActivityMainBinding
import com.design.cumplepablo.databinding.ActivityOnBoardingBinding

class OnBoarding : AppCompatActivity() {

    private lateinit var binding : ActivityOnBoardingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnBoardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btSiguiente.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

    }
}