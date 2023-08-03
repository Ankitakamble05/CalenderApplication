package com.example.calenderapplication.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import com.example.calenderapplication.calenderDagger.CalenderComponent
import com.example.calenderapplication.calenderDagger.CalenderModule
import com.example.calenderapplication.calenderDagger.DaggerCalenderComponent
import com.example.calenderapplication.ui.theme.CalenderApplicationTheme
import com.example.calenderapplication.viewmodel.TaskViewModel
import javax.inject.Inject

class MainActivity : ComponentActivity() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    val viewModel: TaskViewModel by viewModels { viewModelFactory }
    lateinit var calenderComponent: CalenderComponent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        calenderComponent = DaggerCalenderComponent.builder()
            .calenderModule(CalenderModule())
            .build()
        calenderComponent.inject(this)
        setContent {
            CalenderApplicationTheme() {
                CalenderApp(viewModel = viewModel)

            }
        }
    }
}
