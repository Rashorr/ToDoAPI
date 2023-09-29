package com.example.repository

import com.example.models.ToDo
import com.example.models.ToDoSeachParams
import com.example.models.testdata
import org.jetbrains.exposed.sql.SortOrder


class storageToDoRepository : ToDoRepoInterface{

    val firstindex = 0


    val deletetestdate = mutableListOf<ToDo>()



    override suspend fun getAll(): List<ToDo> {
        return testdata

    }

    override suspend fun get(id: String): ToDo? {
        if(testdata[0].title == "Test")
            return ToDo("Test","Test","Test","1",false, 4)
        else
            return ToDo("Test","Test","Test","2",false, 4)
    }

    override suspend fun getAllFalseOrDone(isdone: Boolean): List<ToDo> {
       val testdatafalseordone = mutableListOf<ToDo>()
        for (i in 0 until testdata.size){
            if (testdata[i].isDone == isdone)
                testdatafalseordone.add(testdata[i])
        }

        return testdatafalseordone
    }

    override suspend fun add(todo: ToDo): ToDo? {
        testdata.add(todo)
        return todo
    }

    override suspend fun delete(id: String): Boolean {
        deletetestdate.add(ToDo("Test","Test","Test","Test",false, 1))
        for (i in 0 until deletetestdate.size){
            if(deletetestdate[i].id == id) {
                deletetestdate.removeAt(i)
                return true
            } else {
                return false
            }
        }
        return true
    }

    override suspend fun editTodo(id: String): Boolean {
        for (i in 0 until testdata.size){
            if (testdata[i].id == id){
                testdata[i].isDone = true
            }else {
                return false
            }
        }
        return true
    }

    override suspend fun getPriority(priority: Int): List<ToDo> {
        val testallpriority = mutableListOf<ToDo>()
        for (i in 0 until testdata.size){
            if (testdata[i].priority == priority)
                testallpriority.add(testdata[i])
        }

        return testallpriority

    }

    override suspend fun editPriority(id: String, priority: Int): Boolean {
        for (i in 0 until testdata.size){
            if (testdata[i].id == id){
                testdata[i].priority = priority
            }else {
                return false
            }
        }
        return true
    }

    override suspend fun getPriorityHigh(): List<ToDo> {
       return testdata.sortedBy { SortOrder.DESC }
    }

    override suspend fun getPriorityLow(): List<ToDo> {
        return testdata.sortedBy { SortOrder.ASC }
    }

    override suspend fun getNextHighToDo(): ToDo {
        return testdata.maxBy { it.priority }
    }

    override suspend fun search(searchValues: ToDoSeachParams): List<ToDo>? {
        TODO("Not yet implemented")
    }
}