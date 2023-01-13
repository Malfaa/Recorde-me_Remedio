package com.malfaa.recorde_me_remedio.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.malfaa.recorde_me_remedio.utils.Constantes

@Database(entities = [Remedio::class], version = 3)
abstract class RemedioDatabase : RoomDatabase() {

    abstract val dao: RemedioDao

    companion object {
        @Volatile
        private var INSTANCE: RemedioDatabase?= null

        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE ${Constantes.TABLE_NAME} ADD COLUMN 'linguagem' TEXT NOT NULL default 'português'")
            }
        }
        val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE ${Constantes.TABLE_NAME} ADD COLUMN 'proximoDia' INTEGER NOT NULL default '0'")
            }
        }

        fun getInstance(context: Context): RemedioDatabase{
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        RemedioDatabase::class.java,
                        Constantes.TABLE_NAME
                    )
                        .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
                        .build()

                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}