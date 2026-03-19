package com.designlife.opendraw.board.presentation.viewmodel

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.util.fastForEach
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.designlife.opendraw.board.data.DrawingState
import com.designlife.opendraw.board.data.PathData
import com.designlife.opendraw.board.domain.enums.CanvasActionType
import com.designlife.opendraw.board.domain.enums.ShapeType
import com.designlife.opendraw.board.domain.enums.ToolTipType
import com.designlife.opendraw.board.domain.usecase.DrawingAction
import com.designlife.opendraw.common.data.entity.InternalCanvasBoard
import com.designlife.opendraw.common.domain.repository.BoardRepository
import com.designlife.opendraw.common.utils.AppUtils
import com.designlife.opendraw.common.utils.FileStorage
import com.designlife.opendraw.common.utils.toWeightedAverageColor
import com.designlife.opendraw.ui.theme.HighlighterColor1
import com.designlife.opendraw.ui.theme.HighlighterColor2
import com.designlife.opendraw.ui.theme.HighlighterColor3
import com.designlife.opendraw.ui.theme.PrimaryColorHome1
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class BoardViewModel(
    private val boardRepository: BoardRepository,
) : ViewModel(){

    private val _boardTitle = mutableStateOf("")
    val boardTitle = _boardTitle
    private val _boardId = mutableStateOf(System.currentTimeMillis().hashCode().toLong())
    private val _boardCreatedAt = mutableStateOf(0L)
    private val _boardCategory = mutableStateOf("Unknown")
    private val _exportedData = mutableStateOf("")
    val exportedData = _exportedData
    private val _fileName = mutableStateOf("Whiteboard_${AppUtils.getFormattedTimestamp(System.currentTimeMillis())}.json")
    val fileName = _fileName


    private val _state = MutableStateFlow(DrawingState())
    val state = _state.asStateFlow()

    private val _canvasActionType = mutableStateOf(CanvasActionType.DRAW_UNDEFINED)
    val canvasActionType = _canvasActionType
    private val _selectedToolTip = mutableStateOf(ToolTipType.IN_ACTIVE)
    val selectedToolTip = _selectedToolTip
    private val _selectedShape = mutableStateOf(ShapeType.UN_DEFINED)
    val selectedShape = _selectedShape

    val _highlighterColor : Color = listOf(HighlighterColor1, HighlighterColor2, HighlighterColor3).toWeightedAverageColor()

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
            is DrawingAction.OnToolTipChange -> {
                _selectedToolTip.value = action.type
                onToolTipChange(action.type)
            }
            is DrawingAction.OnShapeSelected -> {
                _selectedShape.value = action.type
            }
            is DrawingAction.OnCanvasActionTypeSelected -> {
                _canvasActionType.value = action.type
            }
            is DrawingAction.OnSaveBoard -> {
                onCanvasSave()
            }
            is DrawingAction.OnExportBoard -> {
                onExport()
            }
            is DrawingAction.OnImportBoard -> {
//                onImport()
            }
            is DrawingAction.OnCanvasBoardResume -> {
                _boardId.value = action.boardId
                onResumeCanvas(action.boardId)
                Log.d("FLOW","BoardViewModel :: onAction :: DrawingAction.OnCanvasBoardResume :: onResumeCanvas")
            }
            is DrawingAction.OnCanvasBoardCreate -> {
                _boardId.value = System.currentTimeMillis().hashCode().toLong()
                _boardTitle.value = action.title
                _boardCreatedAt.value = action.createdAt
            }
        }
    }

    private fun onResumeCanvas(boardId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            boardRepository.getBoardById(boardId)?.let { canvasBoard ->
                Log.d("FLOW","BoardViewModel :: onResumeCanvas :: id ${canvasBoard.boardId}")
                _boardTitle.value = canvasBoard.boardTitle
                _boardCreatedAt.value = canvasBoard.createdAt
                _boardCategory.value = canvasBoard.category
                val pathDataList : List<PathData> = InternalCanvasBoard.fromStroke(boardId,canvasBoard.strokes)

                pathDataList?.let { pathData ->
                    _state.update {
                        it.copy(paths = pathData)
                    }

//                    it.fastForEach { pathData ->
//                        recoverDrawingCanvas(pathData)
//                    }
                }
            }

        }
    }

    private fun recoverDrawingCanvas(pathData: PathData){
        Log.d("FLOW","BoardViewModel :: recoverDrawingCanvas :: path data ${pathData.hashCode()}")
        onBrushSelected(size = pathData.penSize, color = pathData.color)
        pathData.path.fastForEach {
            onDraw(offset = it)
        }
    }
    private fun onExport() {
        viewModelScope.launch(Dispatchers.IO) {
            val strokes = mutableListOf<InternalCanvasBoard.Strokes>()
            _state.value.paths.fastForEach { pathData ->
                Log.i("FLOW", "onExport: Stroke Color = ${pathData.color}, Pen Size = ${pathData.penSize}")
                strokes.add(PathData.toStroke(pathData))
            }
            val internalCanvasBoard = InternalCanvasBoard(
                boardId = _boardId.value,
                boardTitle = _boardTitle.value,
                category = "Testing",
                strokes = strokes,
                createdAt = _boardCreatedAt.value,
                lastModifiedAt = System.currentTimeMillis(),
                shapes = emptyList(),
                texts = emptyList()
            )
            val data = InternalCanvasBoard.toJson(internalCanvasBoard)
            _exportedData.value = data
        }
    }

    private fun onCanvasSave() {
        val strokes = mutableListOf<InternalCanvasBoard.Strokes>()
        _state.value.paths.fastForEach { pathData ->
            strokes.add(PathData.toStroke(pathData))
        }
        val internalCanvasBoard = InternalCanvasBoard(
            boardId = _boardId.value,
            createdAt = _boardCreatedAt.value,
            lastModifiedAt = System.currentTimeMillis(),
            boardTitle = _boardTitle.value,
            category = _boardCategory.value,
            strokes = strokes,
        )
        viewModelScope.launch(Dispatchers.IO) {
            boardRepository.insertBoard(internalCanvasBoard)
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
        onBrushSelected(25f,_highlighterColor)
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
                id = System.currentTimeMillis(),
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