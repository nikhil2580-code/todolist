package com.nikhilkhairnar.todolistapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CategoryAdapter(
    private val categories: List<String>,
    private val taskDao: TaskDao,
    private val onDeleteClick: (Task) -> Unit,
    private val onEditClick: (Task) -> Unit,
    private val onTaskCompleteChange: (Task, Boolean) -> Unit // Add this parameter
) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {
    private val categoryTasks = mutableMapOf<String, List<Task>>()
    private val expandedCategories = mutableSetOf<String>()

    inner class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewCategoryName: TextView = itemView.findViewById(R.id.textViewCategoryName)
        val recyclerViewTasks: RecyclerView = itemView.findViewById(R.id.recyclerViewTasks)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.category_item, parent, false)
        return CategoryViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = categories[position]
        holder.textViewCategoryName.text = category

        // Fetch tasks for the category and update RecyclerView
        taskDao.getTasksByCategory(category).observeForever { tasks ->
            categoryTasks[category] = tasks
            holder.recyclerViewTasks.adapter = TaskAdapter(tasks, onDeleteClick, onEditClick, onTaskCompleteChange) // Pass onTaskCompleteChange here
        }

        // Expand/collapse functionality
        holder.itemView.setOnClickListener {
            if (expandedCategories.contains(category)) {
                expandedCategories.remove(category)
                holder.recyclerViewTasks.visibility = View.GONE
            } else {
                expandedCategories.add(category)
                holder.recyclerViewTasks.visibility = View.VISIBLE
            }
        }

        // Set initial visibility based on expanded state
        holder.recyclerViewTasks.visibility =
            if (expandedCategories.contains(category)) View.VISIBLE else View.GONE
    }

    override fun getItemCount() = categories.size
}

