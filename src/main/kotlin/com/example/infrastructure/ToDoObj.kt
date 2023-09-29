package com.example.infrastructure

import org.jetbrains.exposed.sql.Table

object ToDoObj: Table() {
    fun insert(placeholder: () -> Unit) {

    }

    val id = varchar("id", 128)
    val name = varchar("name", 128)
    val description = varchar("description", 128)
    val date = varchar("date", 128)

    override val primaryKey = PrimaryKey(id)
}