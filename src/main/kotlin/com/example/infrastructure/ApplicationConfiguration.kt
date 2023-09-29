package com.example.infrastructure

import com.typesafe.config.ConfigFactory
import io.ktor.server.application.*


class ApplicationConfiguration {

    lateinit var databaseConfig: DatabaseConfig

}

data class DatabaseConfig(
    val driverClass: String,
    val url: String,
    val username: String,
    val password: String,
)

fun Application.setupApplicationConfiguration(): ApplicationConfiguration {
    val appConfig = ApplicationConfiguration()
    val config = ConfigFactory.load()


    //Database
    val databaseObject = config.getConfig("ktor").getConfig("database")
    val driverClass = config.getConfig("ktor").getConfig("database").getString("driverClass")
    val url = databaseObject.getString("jdbcUrl")
    val username = databaseObject.getString("username")
    val password = databaseObject.getString("password")
    appConfig.databaseConfig = DatabaseConfig(driverClass, url,username,password)


    return appConfig
}


