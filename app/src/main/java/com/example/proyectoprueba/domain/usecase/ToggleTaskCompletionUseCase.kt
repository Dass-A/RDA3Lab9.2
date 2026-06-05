package com.example.proyectoprueba.domain.usecase

import com.example.proyectoprueba.domain.repository.AcademicTaskRepository

class ToggleTaskCompletionUseCase(private val repository: AcademicTaskRepository) {
    suspend operator fun invoke(taskId: String) {
        repository.toggleTaskCompletion(taskId)
    }
}
