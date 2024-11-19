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

class EditTaskActivity : AppCompatActivity() {

    private var taskId: Int = 0
    private lateinit var task: Task

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_edit_task)

        val calendar = Calendar.getInstance()

        val titulo = findViewById<EditText>(R.id.et_et_titulo)
        val descripcion = findViewById<EditText>(R.id.et_et_descipcion)
        val categoria = findViewById<Spinner>(R.id.et_spinner_categoria)
        val fechaInicio = findViewById<EditText>(R.id.et_et_fecha_inicio)
        val fechaFin = findViewById<EditText>(R.id.et_et_fecha_fin)
        val btnGuardar = findViewById<Button>(R.id.btn_et_guardar)

        // Recuperar los datos del Intent
        intent?.let {
            taskId = it.getIntExtra("taskId", -1)
            if (taskId != -1) {
                loadTaskDetails(taskId)
            }
        }

        btnGuardar.setOnClickListener {
            val title = titulo.text.toString()
            val description = descripcion.text.toString()
            val category = categoria.selectedItem.toString()
            val startDate = fechaInicio.text.toString()
            val endDate = fechaFin.text.toString()

            if (title.isNotEmpty() && description.isNotEmpty() && startDate.isNotEmpty() && endDate.isNotEmpty()) {
                updateTask(Task(taskId, title, description, category, startDate, endDate))
            } else {
                Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show()
            }
        }

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

    private fun loadTaskDetails(taskId: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            val taskDao = AppDataBase.getDatabase(this@EditTaskActivity).taskDao()
            task = taskDao.getTaskById(taskId) ?: return@launch

            // Asignar los valores recuperados a los campos
            runOnUiThread {
                findViewById<EditText>(R.id.et_titulo).setText(task.title)
                findViewById<EditText>(R.id.et_descipcion).setText(task.description)
                findViewById<Spinner>(R.id.spinner_categoria).setSelection(getCategoryIndex(task.category))
                findViewById<EditText>(R.id.et_fecha_inicio).setText(task.startDate)
                findViewById<EditText>(R.id.et_fecha_fin).setText(task.endDate)
            }
        }
    }

    private fun getCategoryIndex(category: String): Int {
        //lógica para mapear la categoría al índice correspondiente en el Spinner
        val categories = resources.getStringArray(R.array.spinner_options)
        return categories.indexOf(category)
    }

    private fun updateTask(task: Task) {
        CoroutineScope(Dispatchers.IO).launch {
            val taskDao = AppDataBase.getDatabase(this@EditTaskActivity).taskDao()
            taskDao.updateTask(task)

            runOnUiThread {
                Toast.makeText(this@EditTaskActivity, "Tarea actualizada", Toast.LENGTH_SHORT).show()
                finish() // Regresar a la actividad anterior
            }
        }
    }
}
