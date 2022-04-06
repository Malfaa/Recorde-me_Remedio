package com.malfaa.lembrete.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.malfaa.lembrete.repository.ItemRepository
import com.malfaa.lembrete.room.entidade.ItemEntidade

@Database(entities = [ItemEntidade::class], version = 1, exportSchema = false)
abstract class LDatabase: RoomDatabase(){
    //abstract fun meuDao(): LDao
    abstract fun repository(): ItemRepository

    private class DatabaseCallback : RoomDatabase.Callback()

    companion object{
        @Volatile
        private var INSTANCE: LDatabase? = null

        fun recebaDatabase(context: Context): LDatabase {
            return (INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    LDatabase::class.java,
                    "database"
                ).addCallback(DatabaseCallback()).build()
                INSTANCE = instance
                instance
            })
        }
    }
}
