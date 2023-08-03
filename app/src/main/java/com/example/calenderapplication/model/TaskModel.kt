package com.example.calenderapplication.model

import com.google.gson.annotations.SerializedName


data class StoreCalendarTaskRequest(
    @SerializedName("user_id") val user_id: Int,
    @SerializedName("task") val task: TaskModel
)

data class GetCalendarTaskListRequest(
    @SerializedName("user_id") val user_id: Int
)

data class DeleteCalendarTaskRequest(
    @SerializedName("user_id") val user_id: Int,
    @SerializedName("task_id") val task_id: Int
)

data class TaskModel(
    @SerializedName("title") val name: String,
    @SerializedName("description") val description: String
)

data class TaskDetailsResponse(
    val task_details: Map<Int, TaskModel>
)


