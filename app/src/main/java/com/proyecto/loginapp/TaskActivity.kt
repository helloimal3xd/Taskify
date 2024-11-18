package com.proyecto.loginapp

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class TaskActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_task)

        val titulo = findViewById<TextView>(R.id.tv_at_titulo)
        val descripcion = findViewById<TextView>(R.id.tv_at_descripcion)
        val categoria = findViewById<TextView>(R.id.tv_at_categoria)
        val fechaInicio = findViewById<TextView>(R.id.tv_fecha_inicio)
        val fechaFinal = findViewById<TextView>(R.id.tv_fecha_final)
        val editar = findViewById<Button>(R.id.btn_editar)
        val borrar = findViewById<Button>(R.id.btn_borrar)

        intent?.let {
            titulo.text = it.getStringExtra("taskTitle")
            descripcion.text = it.getStringExtra("taskDescription")
            categoria.text = it.getStringExtra("taskCategory")
            fechaInicio.text = it.getStringExtra("taskStartDate")
            fechaFinal.text = it.getStringExtra("taskEndDate")
        }
    }
}