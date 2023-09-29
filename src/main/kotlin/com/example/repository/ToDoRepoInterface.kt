package com.example.repository

import com.example.models.ToDo
import com.example.models.ToDoSeachParams

interface ToDoRepoInterface {

    suspend fun getAll(): List<ToDo>
    suspend fun get(id: String): ToDo?
    suspend fun getAllFalseOrDone(isdone : Boolean): List<ToDo>
    suspend fun add(todo: ToDo): ToDo?
    suspend fun delete(id: String): Boolean
    suspend fun editTodo(id: String): Boolean
    suspend fun getPriority(priority: Int): List<ToDo>
    suspend fun editPriority(id: String,priority: Int): Boolean
    suspend fun getPriorityHigh(): List<ToDo>
    suspend fun getPriorityLow(): List<ToDo>
    suspend fun getNextHighToDo(): ToDo?
    suspend fun search(searchValues: ToDoSeachParams): List<ToDo>?



}