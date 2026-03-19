package com.designlife.opendraw.common.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.designlife.opendraw.common.data.entity.InternalCanvasBoard
import kotlinx.coroutines.flow.Flow

@Dao
interface CanvasBoardDao {
    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBoard(board: InternalCanvasBoard) : Long

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllBoards(notes: List<InternalCanvasBoard>)

    @Transaction
    @Query("SELECT * FROM CanvasBoard")
    fun getAllBoards() : Flow<List<InternalCanvasBoard>>

    @Transaction
    @Query("SELECT * FROM CanvasBoard WHERE boardId=:boardId")
    suspend fun getBoardById(boardId : Long) : InternalCanvasBoard

    @Transaction
    @Query("DELETE FROM CanvasBoard WHERE boardId=:boardId")
    suspend fun deleteBoardById(boardId : Long)
}