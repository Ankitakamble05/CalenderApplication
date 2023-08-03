package com.example.calenderapplication.view

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.calenderapplication.model.TaskModel
import com.example.calenderapplication.viewmodel.TaskViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate

@Composable
fun CalenderApp(viewModel: TaskViewModel) {
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    var taskList by remember { mutableStateOf(emptyList<TaskModel>()) }
    var isTaskDialogVisible by remember { mutableStateOf(false) }
    val navController = rememberNavController()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {
        Column(
            modifier = Modifier
                .padding(6.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CustomDatePicker(
                initialDate = selectedDate,
                onDateSelected = { date -> selectedDate = date },
                onTaskCreateClick = { isTaskDialogVisible = true }
            )

            if (isTaskDialogVisible) {
                TaskDialog(
                    viewModel = viewModel,
                    onSave = {
                        taskList = taskList + it
                        isTaskDialogVisible = false
                    },
                    onCancel = {
                        isTaskDialogVisible = false
                    }
                )
            }
            Log.d("Ankita","GEt1:" + taskList.size.toString())
            // Task list button
            Button(
                onClick = {
                    viewModel.viewModelScope.launch {
                        viewModel.getTasks(){ response ->
                            response?.let { taskDetailsResponse ->
                                taskList = taskDetailsResponse.task_details.values.toList()
                            }
                        }
                        Log.d("Ankita","GEt2:" + taskList.size.toString())
                    }
                    navController.navigate("taskList") },
                modifier = Modifier.padding(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Magenta)
            ) {
                Text(text = "View Task List", color = Color.White)
            }
        }
    }

    NavHost(navController = navController, startDestination = "calendar") {
        composable("calendar") {
        }

        composable("taskList") {
            TaskListScreen(viewModel = viewModel, taskList = taskList, navController = navController) {
                println("Task clicked: ${it.name}")
            }
        }
    }



}




@Composable
fun CustomDatePicker(
    initialDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
    onTaskCreateClick: () -> Unit
) {
    var selectedDate by remember { mutableStateOf(initialDate) }
    var month by remember { mutableStateOf(selectedDate.monthValue) }
    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .wrapContentSize()
                .padding(25.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            IconButton(
                onClick = {
                    month = if (month == 1) 12 else month - 1
                    selectedDate = selectedDate.minusMonths(1)
                    onDateSelected(selectedDate)
                },
                modifier = Modifier.background(Color.Magenta, CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Previous Month",
                    tint = Color.White
                )
            }

            BasicTextField(
                modifier = Modifier.padding(6.dp),
                value = "${selectedDate.month.toString().padStart(2, '0')} , ${selectedDate.year}",
                onValueChange = {},
                singleLine = true,
                readOnly = true,
                textStyle = TextStyle(
                    textAlign = TextAlign.Center,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                ),
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(onDone = {
                    focusManager.clearFocus()
                })
            )

            IconButton(
                onClick = {
                    month = if (month == 12) 1 else month + 1
                    selectedDate = selectedDate.plusMonths(1)
                    onDateSelected(selectedDate)
                },
                modifier = Modifier.background(Color.Magenta, CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = "Next Month",
                    tint = Color.White
                )
            }
        }

        // Weekday header row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            for (weekday in WeekDays.values()) {
                Text(
                    text = weekday.name,
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    textAlign = TextAlign.Center,
                    color = Color.Gray
                )
            }
        }

        val daysInMonth = selectedDate.month.length(selectedDate.isLeapYear)
        val firstDayOfWeek = selectedDate.withDayOfMonth(1).dayOfWeek.value % 7
        val days = (1..daysInMonth).map { it.toString() }
        val emptyDays = (1..firstDayOfWeek).map { "" }
        val allDays = emptyDays + days

        LazyVerticalGrid(
            columns = GridCells.Fixed(7),
            modifier = Modifier.padding(horizontal = 16.dp),
            contentPadding = PaddingValues(top = 16.dp, bottom = 8.dp)
        ) {
            items(allDays.size) { index ->
                DayCell(
                    day = allDays[index],
                    isSelected = index >= firstDayOfWeek && index < firstDayOfWeek + daysInMonth,
                    today = (selectedDate == LocalDate.now() && LocalDate.now().dayOfMonth == index - 1),
                    onDateSelected = {
                        if (index >= firstDayOfWeek && index < firstDayOfWeek + daysInMonth) {
                            val selectedDay = it.toInt()
                            selectedDate = selectedDate.withDayOfMonth(selectedDay)
                            onDateSelected(selectedDate)
                        }
                    },
                    onTaskCreateClick = onTaskCreateClick
                )
            }
        }
    }
}

@Composable
fun DayCell(
    day: String,
    isSelected: Boolean,
    today: Boolean,
    onDateSelected: (String) -> Unit,
    onTaskCreateClick: (() -> Unit)?
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .clickable {
                onDateSelected(day)
            }
            .background(if (today) Color.Magenta else Color.Transparent, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = day,
            style = TextStyle(
                fontSize = 18.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                color = if (today) Color.White else if (isSelected) Color.Black else Color.Gray
            ),
            textAlign = TextAlign.Center
        )
        if (isSelected) {
            onTaskCreateClick?.let {
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .height(48.dp)
                        .background(Color.Transparent)
                        .clickable(onClick = it)
                ) {
                }
            }
        }
    }
}




enum class WeekDays {
    SUN, MON, TUE, WED, THU, FRI, SAT
}


@Composable
fun TaskListScreen(viewModel: TaskViewModel, taskList: List<TaskModel>, navController: NavController, onTaskClick: (TaskModel) -> Unit) {
    Surface(modifier = Modifier.fillMaxSize(), color = Color.White) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = { navController.popBackStack() },
                modifier = Modifier.padding(16.dp),
                colors = ButtonDefaults.buttonColors(Color.Magenta)
            ) {
                Text(text = "Back", color = Color.White)
            }
            Text(
                text = "Task List",
                style = TextStyle(
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            LazyColumn {
                items(taskList) { task ->
                    TaskListItem(task = task, onItemClick = { onTaskClick(task) }, onDeleteTask = {
                        var tasksMap = mapOf<Int, TaskModel>()
                        viewModel.viewModelScope.launch {
                            viewModel.getTasks(){ response ->
                                response?.let { taskDetailsResponse ->
                                     tasksMap = taskDetailsResponse.task_details
                                }
                                tasksMap.entries.find { it.value == task }?.key?.let { it1 -> viewModel.deleteTask(taskId = it1) }
                            }


                        }
                    })
                }
            }
        }
    }
}

@Composable
fun TaskListItem(task: TaskModel, onItemClick: () -> Unit, onDeleteTask: (TaskModel) -> Unit){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable(onClick = onItemClick),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Text(
            text = task.name,
            style = TextStyle(
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            ),
            color = Color.Black
        )
        Text(
            text = task.description,
            style = TextStyle(
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            ),
            color = Color.LightGray
        )
        IconButton(onClick = { onDeleteTask(task) }) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Delete Task",
                tint = Color.Red
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskDialog(
    viewModel: TaskViewModel,
    onSave: (TaskModel) -> Unit,
    onCancel: () -> Unit
) {
    var taskName by remember { mutableStateOf("") }
    var taskDescription by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onCancel,
        title = { Text("Create Task") },
        text = {
            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(bottom = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Task Icon",
                        tint = Color.Magenta
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    TextField(
                        value = taskName,
                        onValueChange = { taskName = it },
                        label = { Text("Task Name") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                TextField(
                    value = taskDescription,
                    onValueChange = { taskDescription = it },
                    label = { Text("Task Description") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(onClick = {
                val newTask = TaskModel(
                    name = taskName,
                    description = taskDescription
                )
                viewModel.viewModelScope.launch {
                    viewModel.sendTasks(newTask)
                    onSave(newTask)
                }
            }) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onCancel) {
                Text("Cancel")
            }
        }
    )
}


