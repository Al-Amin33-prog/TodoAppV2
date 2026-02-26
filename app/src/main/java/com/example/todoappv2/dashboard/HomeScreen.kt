package com.example.todoappv2.dashboard

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

import com.example.todoappv2.dashboard.components.QuickActionButtons
import com.example.todoappv2.dashboard.components.TodayOverviewCard
import com.example.todoappv2.dashboard.components.UpcomingTaskItem

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onAddTaskClick: () -> Unit,
    onAddSubjectClick:() -> Unit,
    onTaskClick:(Long)-> Unit,

){

    val isRefreshing by viewModel.isRefreshing.collectAsState()
    val state by viewModel.uiState.collectAsState()
    val refreshState = rememberPullToRefreshState()
    PullToRefreshBox(isRefreshing = isRefreshing,
        onRefresh = {viewModel.refresh()},
        state = refreshState) {
        if (state.isLoading){
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }


        }else{
            LazyColumn (modifier = Modifier
                .fillMaxSize()
                .padding(18.dp)){
                item {
                    TodayOverviewCard(
                        todayCount = state.todayTasks.size,
                        overDueCount = state.overDueTasks.size
                    )
                }
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }

                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }
                item {
                    QuickActionButtons(
                        onAddTask = onAddTaskClick,
                        onAddSubject = onAddSubjectClick
                    )
                }
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }
                item {
                    Text("Upcoming Tasks", style = MaterialTheme.typography.titleMedium)
                }
                items(state.upComingTasks){task ->
                    UpcomingTaskItem(
                        task= task,
                        onClick = onTaskClick)

                }

            }
        }

    }

    }

