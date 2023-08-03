package com.example.calenderapplication.calenderDagger

import com.example.calenderapplication.view.MainActivity
import dagger.Component
import javax.inject.Singleton


@Singleton
@Component(modules = [CalenderModule::class, ViewModelFactoryModule::class])
interface CalenderComponent {
    fun inject(activity: MainActivity)
}






