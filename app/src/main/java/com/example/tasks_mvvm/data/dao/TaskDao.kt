package com.example.tasks_mvvm.data.dao

import com.example.tasks_mvvm.data.model.Task

object TaskDao {
    private val tasks = mutableListOf<Task>()

    fun getAllTasks(): List<Task> = tasks

    fun addTask(task: Task){
        tasks.add(task)
    }

    fun getTask(id: Long) : Task {
        return tasks.stream().filter{item -> item.id == id}.findFirst().orElse(null)
    }
}