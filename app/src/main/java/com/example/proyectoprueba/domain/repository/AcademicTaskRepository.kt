package com.example.proyectoprueba.domain.repository

import com.example.proyectoprueba.domain.model.AcademicTask
import kotlinx.coroutines.flow.Flow
interface AcademicTaskRepository {
    fun getTasksStream(): Flow<List<AcademicTask>>
    suspend fun addTask(title: String)
    suspend fun toggleTaskCompletion(taskId: String)
}
