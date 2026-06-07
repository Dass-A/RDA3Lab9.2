package com.example.proyectoprueba.domain.usecase

import com.example.proyectoprueba.domain.model.AcademicGrade
import com.example.proyectoprueba.domain.repository.GradeRepository
import kotlinx.coroutines.flow.Flow

class GetGradesUseCase(private val repository: GradeRepository) {
    operator fun invoke(): Flow<List<AcademicGrade>> = repository.getGradesStream()
}