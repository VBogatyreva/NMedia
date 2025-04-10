package ru.netology.nmedia

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters


@Database(entities = [PostEntity::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDb: RoomDatabase(){
    abstract fun postDao(): PostDao
}
