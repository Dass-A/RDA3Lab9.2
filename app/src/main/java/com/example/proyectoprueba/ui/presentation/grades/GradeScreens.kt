package com.example.proyectoprueba.ui.presentation.grades

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun GradeApp(viewModel: GradeViewModel) {
    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        when (viewModel.currentScreen) {
            GradeScreenType.LIST -> GradeListScreen(viewModel)
            GradeScreenType.FORM -> GradeFormScreen(viewModel)
        }
    }
}

@Composable
fun GradeListScreen(viewModel: GradeViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { viewModel.onNavigateToForm() }) {
                Text("+", style = MaterialTheme.typography.titleLarge)
            }
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
            when (val state = uiState) {
                is GradeUiState.Loading -> CircularProgressIndicator()
                is GradeUiState.Success -> {
                    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                        item {
                            val cardColor = when {
                                state.average >= 7.0 -> MaterialTheme.colorScheme.primaryContainer
                                state.average >= 5.0 -> MaterialTheme.colorScheme.tertiaryContainer
                                else -> MaterialTheme.colorScheme.errorContainer
                            }
                            val textColor = when {
                                state.average >= 7.0 -> MaterialTheme.colorScheme.primary
                                state.average >= 5.0 -> MaterialTheme.colorScheme.tertiary
                                else -> MaterialTheme.colorScheme.error
                            }
                            Card(
                                modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
                                colors = CardDefaults.cardColors(containerColor = cardColor)
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text("Promedio General Acumulado", style = MaterialTheme.typography.titleMedium)
                                    Text(
                                        "%.2f / 10.00".format(state.average),
                                        style = MaterialTheme.typography.headlineLarge,
                                        color = textColor
                                    )
                                }
                            }
                        }
                        items(items = state.grades, key = { it.id }) { grade ->
                            Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                                Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(grade.activityName, style = MaterialTheme.typography.bodyLarge)
                                        Text(grade.subject, style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    }
                                    Text("%.1f".format(grade.score), style = MaterialTheme.typography.titleLarge,
                                        color = MaterialTheme.colorScheme.primary)
                                }
                            }
                        }
                    }
                }
                is GradeUiState.Error -> {
                    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(24.dp)) {
                        Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text("Error de validación", style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.onErrorContainer)
                                Spacer(Modifier.height(8.dp))
                                Text(state.message, color = MaterialTheme.colorScheme.onErrorContainer)
                            }
                        }
                        Spacer(Modifier.height(16.dp))
                        Button(onClick = {
                            viewModel.onDismissError()
                            viewModel.onNavigateToForm()
                        }) { Text("Reintentar") }
                    }
                }
            }
        }
    }
}

@Composable
fun GradeFormScreen(viewModel: GradeViewModel) {
    val allFilled = viewModel.inputActivity.isNotBlank() &&
            viewModel.inputSubject.isNotBlank() &&
            viewModel.inputScore.isNotBlank()
    Scaffold { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(24.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text("Registrar Calificación", style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 24.dp))
            OutlinedTextField(value = viewModel.inputActivity,
                onValueChange = { viewModel.onActivityChange(it) },
                label = { Text("Nombre de la actividad") }, modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(12.dp))
            OutlinedTextField(value = viewModel.inputSubject,
                onValueChange = { viewModel.onSubjectChange(it) },
                label = { Text("Asignatura") }, modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(12.dp))
            OutlinedTextField(value = viewModel.inputScore,
                onValueChange = { viewModel.onScoreChange(it) },
                label = { Text("Nota (0.0 – 10.0)") }, modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(24.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                OutlinedButton(onClick = { viewModel.onNavigateToList() }) { Text("Cancelar") }
                Button(onClick = { viewModel.onSaveGrade() }, enabled = allFilled) { Text("Registrar") }
            }
        }
    }
}