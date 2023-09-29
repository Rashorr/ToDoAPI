package com.example.models

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.*
import java.time.LocalDate
import java.util.*

@Serializable
data class ToDo(val title: String,
                val description: String,
                val date: String = LocalDate.now().toString(), val id: String = UUID.randomUUID().toString(), var isDone: Boolean, var priority: Int)

object Todos : Table() {
    val id = varchar("id", 128)
    val title = varchar("title", 128)
    val description = varchar("description",128)
    val date = varchar("date",128)
    var isDone = bool("isDone")
    var priority = integer("priority")

    override val primaryKey = PrimaryKey(id)
}
val testdata = mutableListOf<ToDo>(ToDo("Test","Test","Test","Test",false, 5))




