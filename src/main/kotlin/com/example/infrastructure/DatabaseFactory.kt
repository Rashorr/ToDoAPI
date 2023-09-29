package com.example.infrastructure

import com.example.models.Todos
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction



object DatabaseFactory  {

        fun init(dbConfig: DatabaseConfig) {
            val jdbcURL = dbConfig.url
            val database = Database.connect(jdbcURL, dbConfig.driverClass, dbConfig.username, dbConfig.password)
            transaction(database) {
                SchemaUtils.create(Todos)
            }
        }

        suspend fun <T> dbQuery(block: suspend () -> T): T =
            newSuspendedTransaction(Dispatchers.IO) { block() }

}




