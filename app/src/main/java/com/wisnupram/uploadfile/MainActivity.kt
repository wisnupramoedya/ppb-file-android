package com.wisnupram.uploadfile

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var btnStart: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnStart = findViewById(R.id.buttonStart)

        btnStart.setOnClickListener{
            openKTPVerif()
        }
    }

    private fun openKTPVerif() {
        val intent = Intent(this, Detail::class.java)
        startActivity(intent)
    }
}