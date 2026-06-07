package com.example.proyectoprueba.domain.usecase

import com.example.proyectoprueba.domain.repository.GradeRepository

class AddGradeUseCase(private val repository: GradeRepository) {
    suspend operator fun invoke(activityName: String, subject: String, score: Double) {
        if (activityName.isBlank() || subject.isBlank()) {
            throw IllegalArgumentException("Los campos no pueden estar vacíos.")
        }
        if (score < 0.0 || score > 10.0) {
            throw IllegalArgumentException("La nota debe estar entre 0.0 y 10.0. Valor recibido: $score")
        }
        repository.addGrade(activityName, subject, score)
    }
}