package com.designlife.opendraw.board.presentation.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import com.designlife.opendraw.board.data.DrawingState
import com.designlife.opendraw.board.data.PathData
import com.designlife.opendraw.board.domain.enums.ToolTipType
import com.designlife.opendraw.board.domain.usecase.DrawingAction
import com.designlife.opendraw.ui.theme.HighlighterColor
import com.designlife.opendraw.ui.theme.PrimaryColorHome1
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class BoardViewModel() : ViewModel(){

    private val _state = MutableStateFlow(DrawingState())
    val state = _state.asStateFlow()


    private val _selectedToolTip = mutableStateOf(ToolTipType.IN_ACTIVE)
    val selectedToolTip = _selectedToolTip



    private val _penSize = mutableStateOf(3F)

    fun onAction(action : DrawingAction){
        when(action){
            DrawingAction.OnNewPathStart -> {
                onNewPathStart()
            }
            DrawingAction.OnPathEnd -> {
                onPathEnd()
            }
            DrawingAction.OnClearCanvas -> {
                clearCanvas()
            }
            is DrawingAction.OnDraw -> {
                onDraw(action.offset)
            }
            is DrawingAction.OnBrushSelected -> {
                onBrushSelected(action.size,action.color)
            }
            is DrawingAction.OnEraserSelected -> {
                onEraserSelected(action.size)
            }
            is DrawingAction.OnToolTipChange-> {
                _selectedToolTip.value = action.type
                onToolTipChange(action.type)
            }
        }
    }

    private fun onToolTipChange(type: ToolTipType) {
        when(type){
            ToolTipType.IN_ACTIVE -> {}
            ToolTipType.BOARD_TEXT -> {}
            ToolTipType.BOARD_HIGHLIGHTER-> {
                onHighlighterSelected()
            }
            ToolTipType.BOARD_INSERT -> {}
            ToolTipType.BOARD_NOTE -> {}
            ToolTipType.BOARD_UNDO -> {}
            ToolTipType.BOARD_REDO -> {}
            else -> {}
        }
    }

    private fun onHighlighterSelected() {
        onBrushSelected(25f,HighlighterColor)
    }

    private fun onEraserSelected(size: Float) {
        onBrushSelected(size, PrimaryColorHome1.value)
    }

    private fun onBrushSelected(size : Float, color: Color) {
        _state.update { it.copy(
            selectedColor = color
        ) }
        _penSize.value = size
    }

    private fun onPathEnd() {
        val currentPathData = state.value.currentPath ?: return
        _state.update { it.copy(
            currentPath = null,
            paths = it.paths + currentPathData
        ) }
    }

    private fun onNewPathStart() {
        _state.update { it.copy(
            currentPath = PathData(
                id = System.currentTimeMillis().toString(),
                color = it.selectedColor,
                path = emptyList(),
                penSize = _penSize.value
            )
        ) }
    }

    private fun onDraw(offset: Offset) {
        val currentPathData = state.value.currentPath ?: return
        _state.update { it.copy(
            currentPath = currentPathData.copy(
                path = currentPathData.path + offset
            )
        ) }
    }

    private fun clearCanvas() {
        _state.update { it.copy(
            currentPath = null,
            paths = emptyList()
        ) }
    }

}