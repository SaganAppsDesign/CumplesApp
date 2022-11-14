package com.design.cumplepablo

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.design.cumplepablo.databinding.ActivityOnBoardingBinding


class OnBoarding : AppCompatActivity() {

    var name = ""

    private lateinit var binding : ActivityOnBoardingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnBoardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pref = getSharedPreferences("datos", MODE_PRIVATE)
        binding.etPersonName.setText(pref.getString("name", ""))

        binding.btSiguiente.setOnClickListener{
            val editor: SharedPreferences.Editor = pref.edit()
            editor.putString("name",  binding.etPersonName.text.toString())
            editor.apply()
            finish()
            name = binding.etPersonName.text.toString()
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("name", name)
            startActivity(intent)
        }
    }
}