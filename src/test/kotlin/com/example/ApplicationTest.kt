package com.example


import com.example.models.ToDoSeachParams
import com.example.plugins.configureRouting
import com.example.modules.testModule
import com.example.repository.storageToDoRepository
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.defaultheaders.*
import io.ktor.server.testing.*
import kotlinx.coroutines.repackaged.net.bytebuddy.implementation.InvokeDynamic.lambda
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.ResultRow
import org.koin.ktor.plugin.Koin
import kotlin.test.Test
import kotlin.test.assertEquals


class ApplicationTest {

    private lateinit var repo: storageToDoRepository

    @Test
    fun editTheisDoneStaeFromaToDo() = testApplication {
        setupTest()
        val response = client.put("/todos/Test")
        assertEquals("ToDo was markes as done", response.bodyAsText())
        assertEquals(HttpStatusCode.OK, response.status)

    }

    @Test
    fun failedTesttoEditTheToDo() = testApplication {
        setupTest()
        val response = client.put("/todos/Test111")
        assertEquals("No ToDo with id Test111", response.bodyAsText())
        assertEquals(HttpStatusCode.NotFound, response.status)
    }

    @Test
    fun deleteAToDo() = testApplication {
        setupTest()
        val response = client.delete("/todos/Test")
        assertEquals("ToDo removed successfully", response.bodyAsText())
        assertEquals(HttpStatusCode.Accepted, response.status)
    }

    @Test
    fun failedTesttoDeleteAToDo() = testApplication {
        setupTest()
        val response = client.delete("/todos/Test111")
        assertEquals("No todo found with id Test111", response.bodyAsText())
        assertEquals(HttpStatusCode.NotFound, response.status)
    }


    
    @Test
    fun failedgetTodosOrPrio() = testApplication {
        setupTest()
        val response = client.get("/todos/all")
        assertEquals(HttpStatusCode.NotAcceptable, response.status)
    }

    @Test
    fun getToDoviaID() = testApplication {
        setupTest()
        val response = client.get("/todos/Test")
        assertEquals(HttpStatusCode.NotAcceptable, response.status)
    }


    private fun ApplicationTestBuilder.setupTest() {
        application {
            install(Koin) {
                modules(testModule)
            }
            install(DefaultHeaders) {
                header("X-Engine", "Ktor") // will send this header with each response
            }
            configureRouting()
        }
    }

}


/* @Test
    fun testPost() = testApplication {
        val response = client.post("/") {
            header(HttpHeaders.ContentType, ContentType.Application.FormUrlEncoded.toString())
            setBody(
                listOf(
                    "title" to "testtitle",
                    "description" to "testdescription",
                    "date" to "2023-09-21",
                    "id" to "12",
                    "isdone" to "false"
                ).formUrlEncode()
            )
        }
        assertEquals("ToDo stored correctly", response.bodyAsText())
        assertEquals(HttpStatusCode.Created, response.status)
    }

    @Test
    fun testPostfail() = testApplication {
        val response = client.post("/") {
            header(HttpHeaders.ContentType, ContentType.Application.FormUrlEncoded.toString())
            setBody(
                listOf(
                    "title" to "testtitle",
                    "description" to "testdescription",
                    "date" to "2023-09-21",
                    "id" to "12",
                    "isdone" to "false"
                ).formUrlEncode()
            )
        }
        assertEquals("Failed to add ToDo", response.bodyAsText())
        assertEquals(HttpStatusCode.BadRequest, response.status)
    }*/


/*   @Test
    fun getPriorityhigh()= testApplication {
        setupTest()
        val response = client.get("/todos/priority/high")
        assertEquals(HttpStatusCode.OK, response.status)
    }

    @Test
    fun getPrioritylow()= testApplication {
        setupTest()
        val response = client.get("/todos/priority/low")
        assertEquals(HttpStatusCode.OK, response.status)
    }

    @Test
    fun getnexthighestTodo() = testApplication {
        setupTest()
        val response = client.get("/todos/test/next")
        assertEquals(HttpStatusCode.OK,response.status)
    }

    @Test
    fun getallfromPriority() = testApplication {
        setupTest()
        val response = client.get("/todos/priority?priority=6")
        assertEquals(HttpStatusCode.NotFound, response.status)
        assertEquals("No ToDo with priority 6", response.bodyAsText())
    }*/
