package com.design.cumplepablo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast

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
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

}