package com.example.modules

import com.example.repository.ToDoRepoInterface
import com.example.repository.ToDoRepositoryDB
import org.koin.dsl.module

val appModule = module {
    single<ToDoRepoInterface> {
        ToDoRepositoryDB()
    }
}
