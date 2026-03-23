package com.example.todoappv2.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.todoappv2.R
import com.example.todoappv2.dashboard.components.CustomPullRefreshIndicator
import com.example.todoappv2.dashboard.components.QuickActionButtons
import com.example.todoappv2.dashboard.components.TodayOverviewCard
import com.example.todoappv2.dashboard.components.UpcomingTasksSection

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onAddTaskClick: () -> Unit,
    onAddSubjectClick:() -> Unit,
    onTaskClick:(Long)-> Unit,
){
    val isRefreshing by viewModel.isRefreshing.collectAsState()
    val state by viewModel.uiState.collectAsState()
    val refreshState = rememberPullToRefreshState()
    
    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = { viewModel.refresh() },
        state = refreshState,
        indicator = {}
    ) {
        CustomPullRefreshIndicator(
            isRefreshing = isRefreshing,
            Modifier
                .align(Alignment.TopCenter)
                .zIndex(1f)
                .offset(y = 16.dp)
        )
        
        if (state.isLoading){
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator()
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = stringResource(R.string.loading_tasks),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        } else {
            LazyColumn (
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ){
                item {
                    GreetingHeader(userName = "AL_Amin")
                }

                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }

                item {
                    TodayOverviewCard(
                        todayCount = state.todayTasks.size,
                        overDueCount = state.overDueTasks.size
                    )
                }
                
                item {
                    Spacer(modifier = Modifier.height(32.dp))
                }

                item {
                    QuickActionButtons(
                        onAddTask = onAddTaskClick,
                        onAddSubject = onAddSubjectClick
                    )
                }
                
                item {
                    Spacer(modifier = Modifier.height(32.dp))
                }
                
                item {
                    Text(
                        text = stringResource(R.string.upcoming_tasks), 
                        style = MaterialTheme.typography.titleMedium,
                        modifierf = Modifier.padding(bottom = 8.dp)
                    )
                }
                
                item {
                    UpcomingTasksSection(
                        tasks = state.upComingTasks,
                        onTaskClick = onTaskClick
                    )
                }
            }
        }
    }
}
