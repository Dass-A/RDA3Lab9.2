package com.example.proyectoprueba.ui.presentation.grades

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectoprueba.domain.usecase.AddGradeUseCase
import com.example.proyectoprueba.domain.usecase.GetGradesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

private const val GRADE_VM_TAG = "GradeViewModel"

class GradeViewModel(
    private val getGradesUseCase: GetGradesUseCase,
    private val addGradeUseCase: AddGradeUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<GradeUiState>(GradeUiState.Loading)
    val uiState: StateFlow<GradeUiState> = _uiState.asStateFlow()

    var currentScreen by mutableStateOf(GradeScreenType.LIST)
        private set

    var inputActivity by mutableStateOf("")
        private set
    var inputSubject by mutableStateOf("")
        private set
    var inputScore by mutableStateOf("")
        private set

    init {
        observeGrades()
    }

    private fun observeGrades() {
        viewModelScope.launch {
            try {
                getGradesUseCase().collect { gradeList ->
                    val avg = gradeList.map { it.score }.average().takeIf { !it.isNaN() } ?: 0.0
                    _uiState.value = GradeUiState.Success(gradeList, avg)
                }
            } catch (e: Exception) {
                _uiState.value = GradeUiState.Error("Error al cargar calificaciones: ${e.localizedMessage}")
            }
        }
    }

    fun onNavigateToForm() {
        inputActivity = ""; inputSubject = ""; inputScore = ""
        currentScreen = GradeScreenType.FORM
    }

    fun onNavigateToList() { currentScreen = GradeScreenType.LIST }

    fun onActivityChange(v: String) { inputActivity = v }
    fun onSubjectChange(v: String) { inputSubject = v }
    fun onScoreChange(v: String) { inputScore = v }

    fun onSaveGrade() {
        val scoreDouble = inputScore.toDoubleOrNull() ?: run {
            _uiState.value = GradeUiState.Error("La nota '${inputScore}' no es un número válido.")
            currentScreen = GradeScreenType.LIST
            return
        }
        viewModelScope.launch {
            try {
                addGradeUseCase(inputActivity, inputSubject, scoreDouble)
                currentScreen = GradeScreenType.LIST
            } catch (e: IllegalArgumentException) {
                Log.e(GRADE_VM_TAG, "Regla de dominio incumplida", e)
                _uiState.value = GradeUiState.Error(e.message ?: "Dato inválido")
                currentScreen = GradeScreenType.LIST
            } catch (e: Exception) {
                Log.e(GRADE_VM_TAG, "Error inesperado", e)
                _uiState.value = GradeUiState.Error("Error inesperado: ${e.localizedMessage}")
                currentScreen = GradeScreenType.LIST
            }
        }
    }
    fun onDismissError() {
        viewModelScope.launch {
            getGradesUseCase().collect { gradeList ->
                val avg = gradeList.map { it.score }.average().takeIf { !it.isNaN() } ?: 0.0
                _uiState.value = GradeUiState.Success(gradeList, avg)
            }
        }
    }
}