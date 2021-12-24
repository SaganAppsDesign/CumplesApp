package com.design.cumplepablo.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.design.cumplepablo.R
import com.design.cumplepablo.fragments.OnFragmentActionListener

class InfoScreen : AppCompatActivity(), OnFragmentActionListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info_screen)
    }

    override fun onClickFragmentButton1() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    override fun onClickFragmentButton2() {
        Toast.makeText(this, "Has pulsado el botón 2", Toast.LENGTH_SHORT).show()
    }

}