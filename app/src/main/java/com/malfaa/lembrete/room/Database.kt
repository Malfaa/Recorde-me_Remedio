package com.malfaa.lembrete.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.malfaa.lembrete.room.entidade.ItemEntidade

@Database(entities = [ItemEntidade::class], version = 1, exportSchema = false)
abstract class Database: RoomDatabase(){
    abstract fun meuDao(): Dao

    private class DatabaseCallback : RoomDatabase.Callback()

    companion object{
        @Volatile
        private var INSTANCE: com.malfaa.lembrete.room.Database? = null

        fun recebaDatabase(context: Context): com.malfaa.lembrete.room.Database {
            return (INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    com.malfaa.lembrete.room.Database::class.java,
                    "database"
                ).addCallback(DatabaseCallback()).build()
                INSTANCE = instance
                instance
            })
        }
    }
}
