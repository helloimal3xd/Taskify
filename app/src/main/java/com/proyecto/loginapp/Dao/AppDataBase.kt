package com.proyecto.loginapp.Dao

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.proyecto.loginapp.CreateTaskActivity


@Database(entities = [Task::class], version = 1)
abstract class AppDataBase : RoomDatabase() {

    abstract fun taskDao(): taskDao

    companion object {
        @Volatile
        private var instance: AppDataBase? = null

        fun getDatabase(context: CreateTaskActivity): AppDataBase {
            return instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDataBase::class.java,
                    "app_database"
                ).build().also {
                    instance = it
                }
            }
        }
    }
}