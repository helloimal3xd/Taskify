package com.proyecto.loginapp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.proyecto.loginapp.Dao.AppDataBase
import com.proyecto.loginapp.Dao.Task
import com.proyecto.loginapp.Dao.TaskDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: TaskAdapter
    private lateinit var taskDao: TaskDao

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // Inicializar RecyclerView
        recyclerView = findViewById(R.id.recyclerViewTasks)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Inicializar el adaptador con una lista mutable
        adapter = TaskAdapter(this, mutableListOf())
        recyclerView.adapter = adapter

        // Inicializar TaskDao
        taskDao = AppDataBase.getDatabase(this).taskDao()

        // Cargar tareas desde la base de datos
        loadTasks()

        // Configurar botón de agregar tarea
        val addButton = findViewById<ImageView>(R.id.btn_add)
        addButton.setOnClickListener {
            val intent = Intent(this, CreateTaskActivity::class.java)
            startActivityForResult(intent, 1)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun loadTasks() {
        CoroutineScope(Dispatchers.IO).launch {
            val tasks = taskDao.getAllTasks()
            withContext(Dispatchers.Main) {
                adapter.taskList.clear()
                adapter.taskList.addAll(tasks)
                adapter.notifyDataSetChanged()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1 && resultCode == RESULT_OK) {
            val taskDescription = data?.getStringExtra("taskDescription")
            val taskTitle = data?.getStringExtra("taskTitle") ?: "Tarea sin título"
            if (!taskDescription.isNullOrEmpty()) {
                CoroutineScope(Dispatchers.IO).launch {
                    val task = Task(
                        title = taskTitle,
                        description = taskDescription,
                        category = "Categoría",
                        startDate = "Fecha de inicio",
                        endDate = "Fecha de fin"
                    )
                    taskDao.insertTask(task)

                    // Actualizar el RecyclerView en el hilo principal
                    withContext(Dispatchers.Main) {
                        adapter.addTask(task)
                    }
                }
            }
        }
    }
}

