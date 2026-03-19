package com.designlife.opendraw.common.domain.repository

import com.designlife.opendraw.common.data.database.dao.CanvasBoardDao
import com.designlife.opendraw.common.data.entity.InternalCanvasBoard
import kotlinx.coroutines.flow.Flow

class BoardRepository(
    private val boardDao: CanvasBoardDao
) {

    suspend fun insertBoard(internalCanvasBoard: InternalCanvasBoard) : Long{
        return boardDao.insertBoard(internalCanvasBoard)
    }

    suspend fun insertAllBoard(boards: List<InternalCanvasBoard>){
        return boardDao.insertAllBoards(boards)
    }

    suspend fun getAllBoards() : Flow<List<InternalCanvasBoard>>{
        return boardDao.getAllBoards()
    }

    suspend fun getBoardById(boardId : Long) : InternalCanvasBoard?{
        return boardDao.getBoardById(boardId)
    }

    suspend fun deleteBoardById(boardId : Long) {
        boardDao.deleteBoardById(boardId)
    }

    suspend fun updateBoard(internalCanvasBoard: InternalCanvasBoard) : Long {
        return boardDao.insertBoard(internalCanvasBoard)
    }
}