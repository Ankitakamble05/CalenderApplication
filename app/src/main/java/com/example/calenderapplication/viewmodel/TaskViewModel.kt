package com.example.calenderapplication.viewmodel

import androidx.lifecycle.ViewModel
import com.example.calenderapplication.model.DeleteCalendarTaskRequest
import com.example.calenderapplication.model.GetCalendarTaskListRequest
import com.example.calenderapplication.model.StoreCalendarTaskRequest
import com.example.calenderapplication.model.TaskApiService
import com.example.calenderapplication.model.TaskDetailsResponse
import com.example.calenderapplication.model.TaskModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TaskViewModel( private val webService: TaskApiService)  : ViewModel()
{
    val userId = 100;

     fun getTasks(successCallback: (response: TaskDetailsResponse?) -> Unit) {
        val request = GetCalendarTaskListRequest(userId)
        webService.getTasks(request).enqueue(object : Callback<TaskDetailsResponse> {
            override fun onResponse(call: Call<TaskDetailsResponse>, response: Response<TaskDetailsResponse>) {
                if (response.isSuccessful) {
                    val taskDetailsResponse = response.body()
                    if (taskDetailsResponse != null) {
                        successCallback(response.body())
                    } else {
                        // Handle empty response or other issues
                    }
                } else {
                    // Handle error response
                }
            }

            override fun onFailure(call: Call<TaskDetailsResponse>, t: Throwable) {
                // Handle failure
            }
        })
    }

     fun sendTasks(task: TaskModel) {
        val request = StoreCalendarTaskRequest(userId, task)
        webService.sendTask(request).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    val taskDetailsResponse = response.body()
                    if (taskDetailsResponse != null) {

                    } else {
                        // Handle empty response or other issues
                    }
                } else {
                    // Handle error response
                }
            }
            override fun onFailure(call: Call<Void>, t: Throwable) {

            }
        })
    }

    fun deleteTask(taskId:Int) {
        webService.deleteTask(DeleteCalendarTaskRequest(userId, taskId)).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {

                } else {

                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {

            }
        })
    }
}








