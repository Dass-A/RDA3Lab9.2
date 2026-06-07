package com.example.proyectoprueba.domain.repository

import com.example.proyectoprueba.domain.model.AcademicGrade
import kotlinx.coroutines.flow.Flow

interface GradeRepository {
    fun getGradesStream(): Flow<List<AcademicGrade>>
    suspend fun addGrade(activityName: String, subject: String, score: Double)
}