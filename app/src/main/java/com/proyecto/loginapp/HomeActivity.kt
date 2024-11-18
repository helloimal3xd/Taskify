package com.proyecto.loginapp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class HomeActivity : AppCompatActivity() {

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home)

        val addButton = findViewById<ImageView>(R.id.btn_add)

        addButton.setOnClickListener {
            intent = Intent(this, CreateTaskActivity::class.java)
            startActivity(intent)
        }

    }
}