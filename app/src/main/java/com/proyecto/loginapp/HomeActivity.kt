package com.proyecto.loginapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.proyecto.loginapp.Dao.AppDataBase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var taskAdapter: TaskAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        recyclerView = findViewById(R.id.recyclerViewTasks)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val btnAdd = findViewById<ImageView>(R.id.btn_add)

        loadTasks()

        btnAdd.setOnClickListener {
            val intent = Intent(this, CreateTaskActivity::class.java)
            startActivity(intent)
        }
    }

    private fun loadTasks() {
        CoroutineScope(Dispatchers.IO).launch {
            val taskDao = AppDataBase.getDatabase(this@HomeActivity).taskDao()
            val tasks = taskDao.getAllTasks()

            withContext(Dispatchers.Main) {
                taskAdapter = TaskAdapter(this@HomeActivity, tasks)
                recyclerView.adapter = taskAdapter
            }
        }
    }
}
