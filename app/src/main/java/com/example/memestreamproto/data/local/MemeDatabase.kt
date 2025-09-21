package com.example.memestreamproto.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [MemeEntity::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class MemeDatabase : RoomDatabase() {
    abstract fun memeDao(): MemeDao

    companion object {
        @Volatile private var INSTANCE: MemeDatabase? = null

        fun get(ctx: Context): MemeDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    ctx.applicationContext,
                    MemeDatabase::class.java,
                    "memestream.db"
                ).fallbackToDestructiveMigration().build().also { INSTANCE = it }
            }
    }
}
