package com.designlife.opendraw.common.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.designlife.opendraw.common.data.database.dao.CanvasBoardDao
import com.designlife.opendraw.common.data.entity.InternalCanvasBoard

@Database(entities = [InternalCanvasBoard::class], version = 1)
@TypeConverters(CanvasConverters::class)
abstract class OpenDrawDatabase : RoomDatabase() {

    abstract fun boardDao(): CanvasBoardDao

    companion object{
        public val DB_NAME = "Open_Draw_Storage"
        public val BACKUP_EN_TODO = "Canvas_Board.db"
        @Volatile
        private var INSTANCE : OpenDrawDatabase? = null

        fun getDatabase(context : Context) : OpenDrawDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null)
                return tempInstance
            synchronized(this){
                val instance = Room.databaseBuilder(
                    context,
                    OpenDrawDatabase::class.java,
                    DB_NAME
                ).build()
                INSTANCE = instance
                return instance
            }
        }

    }

}