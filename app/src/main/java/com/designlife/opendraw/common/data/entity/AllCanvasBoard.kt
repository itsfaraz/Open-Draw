package com.designlife.opendraw.common.data.entity

import com.designlife.opendraw.common.utils.ServiceLocator

data class AllCanvasBoard(
    val boards : List<InternalCanvasBoard>
){

    companion object{
        fun fromJson(board : String) : AllCanvasBoard{
            val gson = ServiceLocator.provideGson()
            return gson.fromJson(board, AllCanvasBoard::class.java)
        }

    }
}
