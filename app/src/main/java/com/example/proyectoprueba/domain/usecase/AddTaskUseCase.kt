package com.example.proyectoprueba.domain.usecase

import com.example.proyectoprueba.domain.repository.AcademicTaskRepository

class AddTaskUseCase(private val repository: AcademicTaskRepository) {
    suspend operator fun invoke(title: String) {
        val cleanTitle = title.trim()

        if (cleanTitle.isBlank()) {
            throw IllegalArgumentException("El titulo de la tarea academica no puede estar vacio.")
        }

        if (cleanTitle.length < 5) {
            throw IllegalArgumentException("La regla de dominio exige un minimo de 5 caracteres.")
        }

        repository.addTask(cleanTitle)
    }
}
