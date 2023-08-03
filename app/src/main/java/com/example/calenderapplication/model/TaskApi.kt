package com.example.calenderapplication.model

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.POST
import javax.inject.Inject

interface TaskApiService {
    // Declare the methods without implementation details here
    fun sendTask(request: StoreCalendarTaskRequest): Call<Void>
    fun getTasks(request: GetCalendarTaskListRequest): Call<TaskDetailsResponse>
    fun deleteTask(request: DeleteCalendarTaskRequest): Call<Void>
}

class TaskApiServiceImpl @Inject constructor(retrofit: Retrofit) : TaskApiService {
    private val api: TaskApi = retrofit.create(TaskApi::class.java)

    override fun sendTask(request: StoreCalendarTaskRequest): Call<Void> {
        return api.storeCalendarTask(request = request)
    }

    override fun getTasks(request: GetCalendarTaskListRequest): Call<TaskDetailsResponse> {
        return api.getCalendarTaskList(request = request)
    }

    override fun deleteTask(request: DeleteCalendarTaskRequest): Call<Void> {
        return api.deleteCalendarTask(request)
    }
}

interface TaskApi {
    @POST("api/storeCalendarTask")
    fun storeCalendarTask(@Body request: StoreCalendarTaskRequest): Call<Void>

    @POST("api/getCalendarTaskLists")
    fun getCalendarTaskList(@Body request: GetCalendarTaskListRequest): Call<TaskDetailsResponse>

    @POST("api/deleteCalendarTask")
    fun deleteCalendarTask(@Body request: DeleteCalendarTaskRequest): Call<Void>
}