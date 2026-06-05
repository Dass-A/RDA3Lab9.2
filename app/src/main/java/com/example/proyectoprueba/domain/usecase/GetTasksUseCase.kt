package com.example.proyectoprueba.domain.usecase

import com.example.proyectoprueba.domain.model.AcademicTask
import com.example.proyectoprueba.domain.repository.AcademicTaskRepository
import kotlinx.coroutines.flow.Flow

class GetTasksUseCase(private val repository: AcademicTaskRepository) {
    operator fun invoke(): Flow<List<AcademicTask>> {
        return repository.getTasksStream()
    }
}
