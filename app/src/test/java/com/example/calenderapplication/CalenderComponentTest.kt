package com.example.calenderapplication

import com.example.calenderapplication.calenderDagger.CalenderModule
import com.example.calenderapplication.model.TaskApiService
import com.example.calenderapplication.viewmodel.TaskViewModel
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.AdditionalMatchers.eq
import org.mockito.Mockito
import retrofit2.Retrofit


class CalenderModuleTest {

    private lateinit var calendarModule: CalenderModule

    @Before
    fun setup() {
        calendarModule = CalenderModule()
    }

    @Test
    fun provideBaseUrl_shouldReturnCorrectBaseUrl() {

        val expectedBaseUrl = "http://dev.frndapp.in:8080/"

        val baseUrl = calendarModule.provideBaseUrl()

        assertEquals(expectedBaseUrl, baseUrl)
    }

    @Test
    fun provideRetrofit_shouldReturnRetrofitInstance() {

        val baseUrl = "http://dev.frndapp.in:8080/"

        val retrofit = calendarModule.provideRetrofit(baseUrl)

        assertEquals(baseUrl, retrofit.baseUrl().toString())
    }


}


