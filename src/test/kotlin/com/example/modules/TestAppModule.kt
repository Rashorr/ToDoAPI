package com.example.modules

import com.example.repository.ToDoRepoInterface
import com.example.repository.storageToDoRepository
import org.koin.dsl.module

val testModule = module {
    single<ToDoRepoInterface> {
        storageToDoRepository()
    }
}