package com.example.proyectoprueba

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.ViewModelProvider
import com.example.proyectoprueba.data.repository.InMemoryGradeRepository
import com.example.proyectoprueba.domain.usecase.AddGradeUseCase
import com.example.proyectoprueba.domain.usecase.GetGradesUseCase
import com.example.proyectoprueba.ui.presentation.grades.GradeApp
import com.example.proyectoprueba.ui.presentation.grades.GradeViewModel
import com.example.proyectoprueba.ui.theme.ProyectoPruebaTheme

class MainActivity : ComponentActivity() {
    private lateinit var viewModel: GradeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val repository = InMemoryGradeRepository()
        val factory = object : ViewModelProvider.Factory {
            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return GradeViewModel(
                    GetGradesUseCase(repository),
                    AddGradeUseCase(repository)
                ) as T
            }
        }
        viewModel = ViewModelProvider(this, factory)[GradeViewModel::class.java]

        setContent {
            ProyectoPruebaTheme {
                GradeApp(viewModel = viewModel)
            }
        }
    }
}