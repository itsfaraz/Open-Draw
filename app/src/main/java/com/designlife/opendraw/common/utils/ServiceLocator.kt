package com.designlife.opendraw.common.utils

import android.content.Context
import com.designlife.opendraw.common.data.database.OpenDrawDatabase
import com.designlife.opendraw.common.domain.repository.BoardRepository
import com.google.gson.Gson

object ServiceLocator {

    private var gson : Gson? = null
    private var boardRepository : BoardRepository? = null

    public fun provideBoardRepository(context: Context) : BoardRepository{
        return boardRepository ?: createBoardRepository(context)
    }

    fun createBoardRepository(context: Context): BoardRepository {
        val dao = OpenDrawDatabase.getDatabase(context).boardDao()
        boardRepository = BoardRepository(dao)
        return boardRepository!!
    }

    fun provideGson() : Gson{
        return gson ?: createGson()
    }

    private fun createGson(): Gson {
        gson = Gson()
        return gson!!
    }
}