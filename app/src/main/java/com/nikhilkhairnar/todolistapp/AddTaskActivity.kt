package com.nikhilkhairnar.todolistapp

import android.app.DatePickerDialog
import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.nikhilkhairnar.todolistapp.databinding.ActivityAddTaskBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Calendar

class AddTaskActivity : AppCompatActivity() {

    private lateinit var taskDao: TaskDao
    private lateinit var binding: ActivityAddTaskBinding
    private var taskId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize the database and DAO
        val database = TaskDatabase.getDatabase(this)
        taskDao = database.taskDao()

        // Check if we are editing an existing task
        taskId = intent.getIntExtra("taskId", -1)
        if (taskId != -1) {
            lifecycleScope.launch {
                val task = taskId?.let { taskDao.getTaskById(it) }
                task?.let {
                    binding.editTextTaskName.setText(it.name)
                    binding.editTextCategory.setText(it.category)
                    it.deadline?.let { deadlineMillis ->
                        val calendar = Calendar.getInstance().apply { timeInMillis = deadlineMillis }
                        val formattedDate = "${calendar.get(Calendar.DAY_OF_MONTH)}/${calendar.get(Calendar.MONTH) + 1}/${calendar.get(Calendar.YEAR)}"
                        binding.buttonSetDeadline.text = formattedDate
                    }
                }
            }
        }

        // Set up the Save button click listener
        binding.buttonSaveTask.setOnClickListener {
            val taskName = binding.editTextTaskName.text.toString()
            val category = binding.editTextCategory.text.toString()
            val deadline = binding.buttonSetDeadline.text.toString()

            if (taskName.isNotEmpty() && category.isNotEmpty()) {
                if (deadline.isEmpty() || deadline == "Set Deadline") {
                    showWarningDialog()
                } else {
                    val deadlineMillis = Calendar.getInstance().apply {
                        val dateParts = deadline.split("/")
                        set(Calendar.DAY_OF_MONTH, dateParts[0].toInt())
                        set(Calendar.MONTH, dateParts[1].toInt() - 1)
                        set(Calendar.YEAR, dateParts[2].toInt())
                    }.timeInMillis

                    // Show loading animation
//                    showLoading()
                    binding.progressBar.visibility = View.VISIBLE

                    // Save or update the task
                    lifecycleScope.launch {
                        delay(2000)
                        if (taskId != -1) {
                            val updatedTask = Task(id = taskId!!, name = taskName, category = category, deadline = deadlineMillis)
                            taskDao.updateTask(updatedTask)
                            Log.d("AddTaskActivity", "Task updated: $updatedTask")
                        } else {
                            val newTask = Task(name = taskName, category = category, deadline = deadlineMillis)
                            taskDao.insertTask(newTask)
                            Log.d("AddTaskActivity", "Task inserted: $newTask")
                        }

                        binding.progressBar.visibility = View.GONE

                        finish()
                    }
                }
            }
        }

        // Set up the Deadline button click listener
        binding.buttonSetDeadline.setOnClickListener {
            showDatePickerDialog()
        }
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDay ->
                val selectedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                binding.buttonSetDeadline.text = selectedDate
            },
            year,
            month,
            day
        )

        datePickerDialog.show()
    }

    private fun showWarningDialog() {
        AlertDialog.Builder(this)
            .setTitle("Warning")
            .setMessage("Please set a deadline for the task before saving.")
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

}
