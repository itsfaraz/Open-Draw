package com.designlife.opendraw.home.presentation.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import com.designlife.opendraw.home.data.entity.CanvasBoard
import com.designlife.opendraw.home.domain.converters.ColorConverter
import com.designlife.opendraw.home.domain.usecase.HomeUseCase

class HomeViewModel(

) : ViewModel() {

    private val _boardList = mutableStateListOf<CanvasBoard>()
    val boardList = _boardList


    private val _searchState = mutableStateOf("")
    val searchState = _searchState

    private val _boardTitle = mutableStateOf("")
    val boardTitle = _boardTitle


    private val _bottomBarLayoutState = mutableStateOf(false)
    val bottomBarLayoutState = _bottomBarLayoutState

    init {
        for (index in 1..19){
            _boardList.add(CanvasBoard(
                id = index.toLong(),
                createdAt = System.currentTimeMillis(),
                lastModified = System.currentTimeMillis(),
                lastSnapshot = "",
                boardTitle = "Random Sample Title : $index",
                category = "$index Lorem Ipsum",
                color = ColorConverter.serializeColor(if (index%2==0) Color.Yellow else Color.Magenta),
                coverImage = null
            ))
        }

    }


    fun onEvent(event : HomeUseCase){
        when(event){
            is HomeUseCase.SearchStateChangeEvent -> {
                _searchState.value = event.state
            }
            is HomeUseCase.AddBoardEvent -> {
                _bottomBarLayoutState.value = true
            }
            is HomeUseCase.OnSearchClickEvent -> {
                _searchState.value = ""
            }
            is HomeUseCase.BoardTitleChangeEvent -> {
                _boardTitle.value = event.state
            }
            is HomeUseCase.CreateBoardEvent -> {
                _bottomBarLayoutState.value = false
            }
        }
    }


}