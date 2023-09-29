package com.example.plugins

import com.example.repository.ToDoRepoInterface
import com.example.models.ToDo
import com.example.models.ToDoSeachParams
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
import org.koin.ktor.ext.inject


fun Application.configureRouting() {
    val repo by inject<ToDoRepoInterface>()

    routing {
        route("/todos") {


            get("{id?}") {
                val id = call.parameters["id"]
                if (id == null) {
                    call.respond(HttpStatusCode.BadRequest, "id has to be a string")
                    return@get
                }

                val todo = repo.get(id)
                if (todo == null) {
                    call.respond(HttpStatusCode.NotFound, "No ToDo with id $id")
                } else {
                    call.respond(HttpStatusCode.OK, todo)
                }
            }

            get {
                try {
                    val all = call.request.queryParameters["all"].toBoolean()
                    val isDone = call.request.queryParameters["isDone"].toBoolean()
                    val priority = call.request.queryParameters["priority"]?.toInt()
                    val next = call.request.queryParameters["next"].toBoolean()

                    val searchParams = ToDoSeachParams(isDone, priority, next, all)

                    val todos = repo.search(searchParams)

                    if (todos != null) {
                        if (todos.isEmpty()) {
                            call.respond(HttpStatusCode.NotFound, "No matching Todos found")
                        } else {
                            call.respond(HttpStatusCode.OK, todos)
                        }
                    }
                } catch (e: Exception) {
                    call.respond(HttpStatusCode.BadRequest, "Invalid query parameters")
                }
            }

            put("{id?}") {
                val id = call.parameters["id"].toString()
                val todo = repo.editTodo(id)
                if (todo) {
                    call.respondText("ToDo was markes as done", status = HttpStatusCode.OK)
                } else {
                    call.respond(HttpStatusCode.NotFound, "No ToDo with id $id")
                }
            }

            post {
                val todo = call.receive<ToDo>()
                repo.add(todo)
                call.respondText("ToDo stored correctly", status = HttpStatusCode.Created)
            }

            delete("{id?}") {
                val id = call.parameters["id"]
                if (id == null) {
                    call.respond(HttpStatusCode.BadRequest, "id has to be a string")
                    return@delete
                }

                val removed = repo.delete(id)
                if (removed) {
                    call.respondText("ToDo removed successfully", status = HttpStatusCode.Accepted)
                } else {
                    call.respond(HttpStatusCode.NotFound, "No todo found with id $id")
                }
            }

        }
    }
}

























































/*  put("/prio/{id?}") {
           val priority = call.request.queryParameters["priority"]?.toInt()
           val id = call.parameters["id"].toString()
           val todo = priority?.let { it1 -> repo.editPriority(id, it1) }
           if (todo == true) {
               call.respondText("Todo was adjusted to the priority $priority", status = HttpStatusCode.OK)
           } else {
               call.respond(HttpStatusCode.NotFound, "No ToDo with id $id")
           }
       }*/