package com.example.proyectoprueba.data.repository

import com.example.proyectoprueba.domain.model.AcademicGrade
import com.example.proyectoprueba.domain.repository.GradeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.UUID

class InMemoryGradeRepository : GradeRepository {
    private val gradesFlow = MutableStateFlow(
        listOf(
            AcademicGrade(UUID.randomUUID().toString(), "Parcial 1", "Matemáticas", 8.5),
            AcademicGrade(UUID.randomUUID().toString(), "Proyecto Final", "Programación Móvil", 9.0)
        )
    )

    override fun getGradesStream(): Flow<List<AcademicGrade>> = gradesFlow

    override suspend fun addGrade(activityName: String, subject: String, score: Double) {
        val newGrade = AcademicGrade(
            id = UUID.randomUUID().toString(),
            activityName = activityName,
            subject = subject,
            score = score
        )
        gradesFlow.value = gradesFlow.value + newGrade
    }
}