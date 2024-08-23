package com.nikhilkhairnar.todolistapp

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    var isComplete: Boolean = false,
    val deadline: Long? = null,
    val category: String
)
