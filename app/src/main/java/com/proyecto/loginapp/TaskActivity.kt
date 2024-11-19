package com.proyecto.loginapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.proyecto.loginapp.Dao.AppDataBase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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

        editar.setOnClickListener {
            if (taskId != -1) {
                val editIntent = Intent(this, EditTaskActivity::class.java).apply {
                    putExtra("taskId", taskId)
                    putExtra("taskTitle", titulo.text.toString())
                    putExtra("taskDescription", descripcion.text.toString())
                    putExtra("taskCategory", categoria.text.toString())
                    putExtra("taskStartDate", fechaInicio.text.toString())
                    putExtra("taskEndDate", fechaFinal.text.toString())
                }
                startActivity(editIntent)
            }
        }

        borrar.setOnClickListener {
            if (taskId != -1) {
                showDeleteConfirmationDialog()
            }
            else {
                Toast.makeText(this, "No se pudo cargar la tarea", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showDeleteConfirmationDialog() {
        val dialogBuilder = AlertDialog.Builder(this)
            .setMessage("¿Estás seguro de que deseas eliminar esta tarea?")
            .setCancelable(false)
            .setPositiveButton("Sí") { _, _ ->
                deleteTask(taskId) // Si confirma, eliminar la tarea
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss() // Si cancela, cerrar el diálogo
            }

        val alert = dialogBuilder.create()
        alert.show()
    }

    private fun deleteTask(taskId: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            val taskDao = AppDataBase.getDatabase(this@TaskActivity).taskDao()
            val task = taskDao.getTaskById(taskId)
            if (task != null) {
                taskDao.deleteTask(task)

                withContext(Dispatchers.Main) {
                    Toast.makeText(this@TaskActivity, "Tarea eliminada", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}