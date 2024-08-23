package com.nikhilkhairnar.todolistapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.nikhilkhairnar.todolistapp.databinding.ActivityMainBinding
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var taskAdapter: TaskAdapter
    private lateinit var taskDao: TaskDao
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val database = TaskDatabase.getDatabase(this)
        taskDao = database.taskDao()

        // Set up RecyclerView for categories
        binding.recyclerViewCategories.layoutManager = LinearLayoutManager(this)
        taskDao.getAllCategories().observe(this, Observer { categories ->
            categories?.let {
                categoryAdapter = CategoryAdapter(it, taskDao, { task ->
                    deleteTask(task)  // onDeleteClick lambda
                }, { task ->
                    editTask(task)  // onEditClick
                }, { task, isComplete ->
                    updateTaskCompletion(task, isComplete) // Pass onTaskCompleteChange lambda
                })
                binding.recyclerViewCategories.adapter = categoryAdapter
            }
        })

        // Set up RecyclerView for all tasks
        binding.recyclerViewTasksItem.layoutManager = LinearLayoutManager(this)
        taskDao.getAllTasks().observe(this, Observer { tasks ->
            tasks?.let {
                taskAdapter = TaskAdapter(it, { task ->
                    deleteTask(task)
                }, { task ->
                    editTask(task)
                }, { task, isComplete ->
                    updateTaskCompletion(task, isComplete)
                })
                binding.recyclerViewTasksItem.adapter = taskAdapter
            }
        })

        // Floating Action Button to add a new task
        binding.fabAddTask.setOnClickListener {
            val intent = Intent(this, AddTaskActivity::class.java)
            startActivity(intent)
        }
    }

    private fun updateTaskCompletion(task: Task, isComplete: Boolean) {
        lifecycleScope.launch {
            task.isComplete = isComplete
            taskDao.updateTask(task)
        }
    }

    private fun deleteTask(task: Task) {
        lifecycleScope.launch {
            taskDao.deleteTask(task)
        }
    }

    private fun editTask(task: Task) {
        val intent = Intent(this, AddTaskActivity::class.java).apply {
            putExtra("taskId", task.id)
        }
        startActivity(intent)
    }
}
