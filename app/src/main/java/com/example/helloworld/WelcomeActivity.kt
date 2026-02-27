package com.example.helloworld

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class WelcomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        val welcome = findViewById<TextView>(R.id.textWelcome)
        val btnBack = findViewById<Button>(R.id.btnBackWelcome)

        val name = intent.getStringExtra("name")
        val display = name ?: "User"
        welcome.text = "Welcome, $display!"

        btnBack.setOnClickListener {
            // go back to previous activity
            onBackPressedDispatcher.onBackPressed()
        }
    }
}
