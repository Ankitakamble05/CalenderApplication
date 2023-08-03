package com.example.calenderapplication.calenderDagger

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.calenderapplication.model.TaskApiService
import com.example.calenderapplication.model.TaskApiServiceImpl
import com.example.calenderapplication.viewmodel.TaskViewModel
import dagger.Binds
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject
import javax.inject.Singleton

const val BASE_URL = "http://dev.frndapp.in:8080/"

@Module
class CalenderModule {
    @Provides
    fun provideBaseUrl(): String = BASE_URL

    @Provides
    @Singleton
    fun provideRetrofit(baseUrl: String): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideTaskApiService(retrofit: Retrofit): TaskApiService {
        return return TaskApiServiceImpl(retrofit)
    }

    @Provides
    fun provideTaskViewModel(taskApiService: TaskApiService): TaskViewModel {
        return TaskViewModel(taskApiService)
    }



}

@Module
abstract class ViewModelFactoryModule {
    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}

class ViewModelFactory @Inject constructor(
    private val taskViewModel: TaskViewModel
) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TaskViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return taskViewModel as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}



