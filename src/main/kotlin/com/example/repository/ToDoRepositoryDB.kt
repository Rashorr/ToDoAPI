package com.example.repository

import com.example.infrastructure.DatabaseFactory.dbQuery
import com.example.models.ToDo
import com.example.models.ToDoSeachParams
import com.example.models.Todos
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import java.time.LocalDateTime
import java.util.*


class ToDoRepositoryDB : ToDoRepoInterface {

    override suspend fun getAll(): List<ToDo> = dbQuery {
        Todos.selectAll().map(::resultRowToTodo)
    }

    override suspend fun getPriorityHigh(): List<ToDo> = dbQuery {
        Todos.selectAll().orderBy(Todos.priority, SortOrder.DESC).map(::resultRowToTodo)

    }

    override suspend fun getPriorityLow(): List<ToDo> = dbQuery {
        Todos.selectAll().orderBy(Todos.priority, SortOrder.ASC).map(::resultRowToTodo)

    }

    override suspend fun getNextHighToDo(): ToDo? = dbQuery {
        val maxPriority = Todos.slice(Todos.priority.max()).select { Todos.isDone eq false }.singleOrNull()
            ?.getOrNull(Todos.priority.max())

        maxPriority?.let { max ->
            Todos.select { Todos.isDone eq false and (Todos.priority eq maxPriority) }
                .map(::resultRowToTodo)
                .singleOrNull()
        }
    }

    override suspend fun search(searchValues: ToDoSeachParams): List<ToDo>? = dbQuery {
        if(searchValues.all){
            return@dbQuery Todos.selectAll().map(::resultRowToTodo)

        }

        var query = Todos.selectAll()

        if(searchValues.isDone != null) {
            query = query.andWhere { Todos.isDone eq searchValues.isDone }
        }

        if(searchValues.priority != null){
            query = query.andWhere { Todos.priority eq searchValues.priority }
        }

        if(searchValues.nextToDo) {
            val highestPriorityTodo = Todos.select { Todos.isDone eq false  }
                .orderBy(Todos.priority to SortOrder.DESC)
                .limit(1)
                .firstOrNull()
           if(highestPriorityTodo != null) {
               return@dbQuery listOf(
                   ToDo(
                       highestPriorityTodo[Todos.title],
                       highestPriorityTodo[Todos.description],
                       highestPriorityTodo[Todos.id],
                       highestPriorityTodo[Todos.date],
                       highestPriorityTodo[Todos.isDone],
                       highestPriorityTodo[Todos.priority]
                   )
               )
           }
        }
        query.map(::resultRowToTodo)
    }

    override suspend fun get(id: String): ToDo? = dbQuery {
        Todos.selectAll()
        Todos
            .select { Todos.id eq id }
            .map(::resultRowToTodo)
            .singleOrNull()
    }

    override suspend fun getAllFalseOrDone(isdone: Boolean): List<ToDo> = dbQuery {
        Todos
            .select { Todos.isDone eq isdone }
            .map(::resultRowToTodo)
            .toList()
    }

    override suspend fun add(todo: ToDo): ToDo? = dbQuery {
        val insertStatement = Todos.insert {
            it[Todos.title] = todo.title
            it[Todos.description] = todo.description
            it[Todos.id] = UUID.randomUUID().toString()
            it[Todos.date] = LocalDateTime.now().toString()
            it[Todos.isDone] = false
            it[Todos.priority] = todo.priority
        }
        insertStatement.resultedValues?.singleOrNull()?.let(::resultRowToTodo)
    }

    override suspend fun delete(id: String): Boolean = dbQuery {
        Todos.deleteWhere { Todos.id eq id } > 0
    }

    override suspend fun editTodo(id: String): Boolean = dbQuery{
        Todos.update({ Todos.id eq id }) {
            it[Todos.isDone] = true
        } > 0
    }

    override suspend fun getPriority(priority: Int): List<ToDo> = dbQuery {
        Todos
            .select { Todos.priority eq priority }
            .map(::resultRowToTodo)
            .toList()
    }

    override suspend fun editPriority(id: String, priority: Int): Boolean = dbQuery {
        Todos.update({ Todos.id eq id }) {
            it[Todos.priority] = priority
        } > 0
    }

    private fun resultRowToTodo(row: ResultRow) = ToDo(
        id = row[Todos.id],
        description = row[Todos.description],
        date = row[Todos.date],
        title = row[Todos.title],
        isDone = row[Todos.isDone],
        priority = row[Todos.priority]
    )
}

