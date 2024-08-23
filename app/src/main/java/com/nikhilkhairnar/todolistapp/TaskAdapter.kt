package com.nikhilkhairnar.todolistapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.util.Calendar

class TaskAdapter(
    private val tasks: List<Task>,
    private val onDeleteClick: (Task) -> Unit,
    private val onEditClick: (Task) -> Unit,
    private val onTaskCompleteChange: (Task, Boolean) -> Unit
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    inner class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val checkBoxComplete: CheckBox = itemView.findViewById(R.id.checkBoxComplete)
        val textViewTaskName: TextView = itemView.findViewById(R.id.textViewTaskName)
        val textViewDeadline: TextView = itemView.findViewById(R.id.textViewDeadline)
        val buttonDeleteTask: ImageButton = itemView.findViewById(R.id.buttonDeleteTask)
        val buttonEditTask: ImageButton = itemView.findViewById(R.id.buttonEditTask)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.task_item, parent, false)
        return TaskViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasks[position]
        holder.textViewTaskName.text = task.name
        holder.checkBoxComplete.isChecked = task.isComplete

        // Format and display the deadline
        task.deadline?.let {
            val calendar = Calendar.getInstance().apply { timeInMillis = it }
            val formattedDeadline = "${calendar.get(Calendar.DAY_OF_MONTH)}/${calendar.get(Calendar.MONTH) + 1}/${calendar.get(Calendar.YEAR)}"
            holder.textViewDeadline.text = "Deadline: $formattedDeadline"
        } ?: run {
            holder.textViewDeadline.text = "No Deadline"
        }

        // Handle delete button click
        holder.buttonDeleteTask.setOnClickListener {
            onDeleteClick(task)
        }

        holder.buttonEditTask.setOnClickListener {
            onEditClick(task)
        }

        // Handle checkbox change
        holder.checkBoxComplete.setOnCheckedChangeListener { _, isChecked ->
            // Update task completion in the database
            onTaskCompleteChange(task, isChecked)
            // You can add code here to update the task completion status in the database
        }
    }

    override fun getItemCount() = tasks.size
}
