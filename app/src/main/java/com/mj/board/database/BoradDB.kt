package com.mj.board.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [BoardEntity::class], version = 1)
abstract class BoradDB: RoomDatabase(){
    abstract fun boardDao(): BoardDao

    companion object {

        private var instance: BoradDB? = null

        fun getInstance(context: Context): BoradDB? {
            if (instance == null) {
                synchronized(BoradDB::class.java) {
                    instance = Room.databaseBuilder(
                        context,
                        BoradDB::class.java, "board_db")
                        .fallbackToDestructiveMigration()
                        .build()
                }
            }

            return instance
        }
    }
}