package com.example.proyectoprueba.ui.presentation.tasks

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.proyectoprueba.domain.usecase.AddTaskUseCase
import com.example.proyectoprueba.domain.usecase.GetTasksUseCase
import com.example.proyectoprueba.domain.usecase.ToggleTaskCompletionUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

private const val TASK_VIEW_MODEL_TAG = "AcademicTaskViewModel"

class AcademicTaskViewModel(
    private val getTasksUseCase: GetTasksUseCase,
    private val addTaskUseCase: AddTaskUseCase,
    private val toggleTaskCompletionUseCase: ToggleTaskCompletionUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow<AcademicTaskUiState>(AcademicTaskUiState.Loading)
    val uiState: StateFlow<AcademicTaskUiState> = _uiState.asStateFlow()

    var currentScreen by mutableStateOf(ScreenType.LIST)
        private set

    var newTaskTitle by mutableStateOf("")
        private set

    init {
        observeTasks()
    }

    private fun observeTasks() {
        viewModelScope.launch {
            try {
                getTasksUseCase().collect { taskList ->
                    _uiState.value = AcademicTaskUiState.Success(taskList)
                }
            } catch (e: Exception) {
                _uiState.value = AcademicTaskUiState.Error(
                    "Error critico al cargar el listado: ${e.localizedMessage}"
                )
            }
        }
    }

    fun onTaskCheckedChange(taskId: String) {
        viewModelScope.launch {
            try {
                toggleTaskCompletionUseCase(taskId)
            } catch (e: Exception) {
                _uiState.value = AcademicTaskUiState.Error(
                    "No se pudo actualizar el estado de la tarea."
                )
            }
        }
    }

    fun onNavigateToCreate() {
        newTaskTitle = ""
        currentScreen = ScreenType.CREATE
    }

    fun onNavigateToList() {
        currentScreen = ScreenType.LIST
    }

    fun onTaskTitleChange(newTitle: String) {
        newTaskTitle = newTitle
    }

    fun onSaveTask() {
        if (newTaskTitle.isBlank()) return

        viewModelScope.launch {
            try {
                addTaskUseCase(newTaskTitle)
                currentScreen = ScreenType.LIST
                newTaskTitle = ""
            } catch (e: IllegalArgumentException) {
                Log.e(TASK_VIEW_MODEL_TAG, "Regla de dominio incumplida", e)
                _uiState.value = AcademicTaskUiState.Error(e.message ?: "Dato invalido")
                currentScreen = ScreenType.LIST
            } catch (e: Exception) {
                Log.e(TASK_VIEW_MODEL_TAG, "Error inesperado al guardar la tarea", e)
                _uiState.value = AcademicTaskUiState.Error("Error al guardar: ${e.localizedMessage}")
                currentScreen = ScreenType.LIST
            }
        }
    }
}

class AcademicTaskViewModelFactory(
    private val getTasksUseCase: GetTasksUseCase,
    private val addTaskUseCase: AddTaskUseCase,
    private val toggleTaskCompletionUseCase: ToggleTaskCompletionUseCase
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AcademicTaskViewModel::class.java)) {
            return AcademicTaskViewModel(
                getTasksUseCase,
                addTaskUseCase,
                toggleTaskCompletionUseCase
            ) as T
        }

        throw IllegalArgumentException("ViewModel desconocido: ${modelClass.name}")
    }
}
