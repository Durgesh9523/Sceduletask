package com.example.schedultask

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Requirement::class, Candidate::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun requirementDetailDao(): RequirementDao
    abstract fun candidateDetailDao(): CandidateDao
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}