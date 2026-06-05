package com.example.proyectoprueba

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.example.proyectoprueba.data.repository.InMemoryTaskRepository
import com.example.proyectoprueba.domain.usecase.AddTaskUseCase
import com.example.proyectoprueba.domain.usecase.GetTasksUseCase
import com.example.proyectoprueba.domain.usecase.ToggleTaskCompletionUseCase
import com.example.proyectoprueba.ui.presentation.tasks.AcademicTaskApp
import com.example.proyectoprueba.ui.presentation.tasks.AcademicTaskViewModel
import com.example.proyectoprueba.ui.presentation.tasks.AcademicTaskViewModelFactory
import com.example.proyectoprueba.ui.theme.ProyectoPruebaTheme

class MainActivity : ComponentActivity() {
    private val viewModel by viewModels<AcademicTaskViewModel> {
        val repository = InMemoryTaskRepository()
        AcademicTaskViewModelFactory(
            getTasksUseCase = GetTasksUseCase(repository),
            addTaskUseCase = AddTaskUseCase(repository),
            toggleTaskCompletionUseCase = ToggleTaskCompletionUseCase(repository)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            ProyectoPruebaTheme {
                AcademicTaskApp(viewModel = viewModel)
            }
        }
    }
}
