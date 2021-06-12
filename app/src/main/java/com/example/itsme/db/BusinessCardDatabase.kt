package com.example.itsme.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import java.util.concurrent.Executors

@Database(entities = [BusinessCard::class, BusinessCardElement::class], version = 1)
abstract class BusinessCardDatabase : RoomDatabase() {

    abstract fun businessCardDAO(): BusinessCardDAO

    object STATIC {
        @Volatile
        var INSTANCE: BusinessCardDatabase? = null
        val NUMBER_OF_THREADS = 4
        val databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS)


        fun getDatabase(context: Context): BusinessCardDatabase {
            if (INSTANCE == null) {
                synchronized(BusinessCardDatabase::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(
                            context.applicationContext,
                            BusinessCardDatabase::class.java,
                            "item_database"
                        ).build()
                    }
                }
            }
            return INSTANCE!!
        }
    }
}