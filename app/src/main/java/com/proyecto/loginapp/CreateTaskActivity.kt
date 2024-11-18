package com.proyecto.loginapp

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.proyecto.loginapp.Dao.AppDataBase
import com.proyecto.loginapp.Dao.Task
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Calendar

class CreateTaskActivity : AppCompatActivity() {

    private lateinit var database: AppDataBase

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_create_task)

        database = AppDataBase.getDatabase(this)
        val calendar = Calendar.getInstance()

        val titulo = findViewById<EditText>(R.id.et_titulo)
        val descripcion = findViewById<EditText>(R.id.et_descipcion)
        val categoria = findViewById<Spinner>(R.id.spinner_categoria)
        val fechaInicio = findViewById<EditText>(R.id.et_fecha_inicio)
        val fechaFin = findViewById<EditText>(R.id.et_fecha_fin)
        val btnGuardar = findViewById<Button>(R.id.btn_guardar)

        btnGuardar.setOnClickListener {
            val title = titulo.text.toString()
            val description = descripcion.text.toString()
            val category = categoria.selectedItem.toString()
            val startDate = fechaInicio.text.toString()
            val endDate = fechaFin.text.toString()

            if (title.isNotEmpty() && description.isNotEmpty() && startDate.isNotEmpty() && endDate.isNotEmpty()) {

                CoroutineScope(Dispatchers.IO).launch {
                    val task = Task(
                        title = title,
                        description = description,
                        category = category,
                        startDate = startDate,
                        endDate = endDate
                    )
                    database.taskDao().insertTask(task)

                    runOnUiThread {
                        Toast.makeText(this@CreateTaskActivity, "Tarea creada exitosamente", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
            } else {
                Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show()
            }
        }

        // Configurar DatePickerDialog para la fecha de inicio
        fechaInicio.setOnClickListener {
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
                val date = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                fechaInicio.setText(date)
            }, year, month, day).show()
        }

        // Configurar DatePickerDialog para la fecha de fin
        fechaFin.setOnClickListener {
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
                val date = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                fechaFin.setText(date)
            }, year, month, day).show()
        }

    }
}