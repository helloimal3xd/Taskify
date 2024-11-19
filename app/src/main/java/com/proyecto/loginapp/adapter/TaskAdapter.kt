package com.proyecto.loginapp

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.proyecto.loginapp.Dao.Task

class TaskAdapter(
    private val context: Context,
    val taskList: MutableList<Task> // Cambiado a MutableList
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    inner class TaskViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.tv_titulo)
        val description: TextView = view.findViewById(R.id.tv_descripcion)
        val category: TextView = view.findViewById(R.id.tv_categoria)
        val startDate: TextView = view.findViewById(R.id.tv_fecha1)
        val endDate: TextView = view.findViewById(R.id.tv_fecha2)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_task, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = taskList[position]
        holder.title.text = task.title
        holder.description.text = task.description
        holder.category.text = task.category
        holder.startDate.text = task.startDate
        holder.endDate.text = task.endDate

        holder.itemView.setOnClickListener {
            val intent = Intent(context, TaskActivity::class.java).apply {
                putExtra("taskTitle", task.title)
                putExtra("taskDescription", task.description)
                putExtra("taskCategory", task.category)
                putExtra("taskStartDate", task.startDate)
                putExtra("taskEndDate", task.endDate)
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = taskList.size

    fun addTask(task: Task) {
        taskList.add(task)
        notifyItemInserted(taskList.size - 1)
    }
}
