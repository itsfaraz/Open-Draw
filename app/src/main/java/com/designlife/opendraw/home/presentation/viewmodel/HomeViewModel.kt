package com.designlife.opendraw.home.presentation.viewmodel

import android.content.Context
import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.util.fastForEach
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.designlife.opendraw.common.data.entity.InternalCanvasBoard
import com.designlife.opendraw.common.domain.repository.BoardRepository
import com.designlife.opendraw.common.utils.FileStorage
import com.designlife.opendraw.home.data.entity.CanvasBoard
import com.designlife.opendraw.home.domain.converters.ColorConverter
import com.designlife.opendraw.home.domain.usecase.HomeUseCase
import com.designlife.opendraw.ui.theme.randomColorString
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class HomeViewModel(
    private val boardRepository: BoardRepository
) : ViewModel() {


    // true :- Light Theme
    private val _systemColorTheme = mutableStateOf(true)

    private val _boardList = mutableStateListOf<CanvasBoard>()
    val boardList = _boardList


    private val _searchState = mutableStateOf("")
    val searchState = _searchState

    private val _boardTitle = mutableStateOf("")
    val boardTitle = _boardTitle


    private val _bottomBarLayoutState = mutableStateOf(false)
    val bottomBarLayoutState = _bottomBarLayoutState

    init {
//        for (index in 1..19){
//            _boardList.add(CanvasBoard(
//                id = index.toLong(),
//                createdAt = System.currentTimeMillis(),
//                lastModified = System.currentTimeMillis(),
//                lastSnapshot = "",
//                boardTitle = "Random Sample Title : $index",
//                category = "$index Lorem Ipsum",
//                color = ColorConverter.serializeColor(if (index%2==0) Color.Yellow else Color.Magenta),
//                coverImage = null
//            ))
//        }
        viewModelScope.launch(Dispatchers.IO) {
            boardRepository.getAllBoards().collectLatest { canvasBoards ->
                canvasBoards.fastForEach { board ->
                    _boardList.add(getBoardMapping(board))
                }

            }
        }
    }

    fun getBoardMapping(board : InternalCanvasBoard) : CanvasBoard{
        return CanvasBoard(
            id = board.boardId,
            createdAt = board.createdAt,
            lastModified = board.lastModifiedAt,
            boardTitle = board.boardTitle,
            category = board.category,
            color = randomColorString(),
            fromImport = false,
            coverImage = null
        )
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
            is HomeUseCase.OnImportEvent -> {
                onImport(event.json)
            }
            is HomeUseCase.OnDarkModeToggle -> {
                toggleSystemColoring()
            }
        }
    }
    private fun onImport(json : String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                if (json.isEmpty()) {
                    Log.d("FLOW", "Json is empty")
                    return@launch
                }

                val canvasBoard = InternalCanvasBoard.fromJson(json)
                boardRepository.updateBoard(canvasBoard)

            } catch (e: Exception) {
                Log.e("FLOW", "onImport failed: ${e.message}", e)
            }
        }
    }


    private fun toggleSystemColoring() {
        if (_systemColorTheme.value){
//            turnDarkTheme()
        }else{
//            turnLightTheme()
        }
        _systemColorTheme.value != _systemColorTheme.value
    }


}