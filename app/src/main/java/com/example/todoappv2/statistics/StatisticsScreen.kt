package com.example.todoappv2.statistics

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.todoappv2.data.repository.AppRepository
import com.example.todoappv2.statistics.components.piechart.PieChartCard
import com.example.todoappv2.statistics.components.productivity.ProductivityCard
import com.example.todoappv2.statistics.components.stats.StatsSummaryCards
import com.example.todoappv2.statistics.components.barchart.WeeklyBarChart

@Composable
fun StatisticScreen(
    repository: AppRepository
){
    val viewModel = remember {
        StatisticsViewModel(repository)
    }
    val state = viewModel.uiState.collectAsState().value
    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
        contentPadding = PaddingValues(16.dp)
    ) {
        item {
            ProductivityCard(
                productivity = state.productivity
            )
            Spacer(modifier  = Modifier.height(16.dp))
            PieChartCard(
                completed = state.completedTask,
                pending = state.pendingTasks,
                overdue = state.overdueTasks
            )
            Spacer(modifier = Modifier.height(16.dp))
            WeeklyBarChart(
                values = state.weeklyCompleted
            )
            Spacer(modifier = Modifier.height(16.dp))
            StatsSummaryCards(
                total = state.totalTasks,
                completed = state.completedTask,
                pending = state.pendingTasks
            )
            Spacer(modifier = Modifier.height(32.dp))
        }
    }

}
