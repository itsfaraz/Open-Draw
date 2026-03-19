package com.designlife.opendraw.board

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.findNavController
import com.designlife.opendraw.board.domain.enums.CanvasActionType
import com.designlife.opendraw.board.domain.enums.ToolTipType
import com.designlife.opendraw.board.domain.usecase.DrawingAction
import com.designlife.opendraw.board.presentation.component.DrawingCanvas
import com.designlife.opendraw.board.presentation.component.ThreeDotComponent
import com.designlife.opendraw.board.presentation.component.ToolTipComponent
import com.designlife.opendraw.board.presentation.viewmodel.BoardViewModel
import com.designlife.opendraw.board.presentation.viewmodel.BoardViewModelFactory
import com.designlife.opendraw.common.utils.ServiceLocator
import com.designlife.opendraw.ui.theme.PrimaryColorHome1
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class BoardFragment : Fragment() {

    lateinit var viewModel: BoardViewModel
    private lateinit var createFileLauncher: ActivityResultLauncher<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val boardId : Long = arguments?.getLong("boardId") ?: -1L
        val boardTitle : String = arguments?.getString("boardTitle") ?: "Untitled Board"
        val boardCreatedAt : Long = arguments?.getLong("createdAt") ?: 0L
        Log.d("FLOW","BoardFragment :: onCreate : Board Id = ${boardId}, Board Title = ${boardTitle}, Board Created = ${boardCreatedAt}")

        val boardRepository = ServiceLocator.provideBoardRepository(requireContext())
        val factory = BoardViewModelFactory(boardRepository)
        viewModel = ViewModelProvider(this,factory)[BoardViewModel::class.java]
        if (boardId != 404L){
//            prefill canvas data
            viewModel.onAction(DrawingAction.OnCanvasBoardResume(boardId))
            Log.d("FLOW","BoardFragment :: onCreate : DrawingAction.OnCanvasBoardResume")
        }else{
            viewModel.onAction(DrawingAction.OnCanvasBoardCreate(404L,boardTitle,boardCreatedAt))
            Log.d("FLOW","BoardFragment :: onCreate : DrawingAction.OnCanvasBoardCreate")
        }

        createFileLauncher =
            registerForActivityResult(ActivityResultContracts.CreateDocument("application/json")) { uri ->
                uri?.let {
                    requireContext().contentResolver.openOutputStream(it)?.use { stream ->
                        // Write content prepared in ViewModel or elsewhere
                        val content = viewModel.exportedData.value
                        Log.d("FLOW","BoardFragment :: registerForActivityResult : Data : ${viewModel.exportedData.value}")
                        stream.write(content.toByteArray())
                    }
                }
            }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeView(requireContext()).apply {
            setContent {

                val selectedToolTip = viewModel.selectedToolTip.value
                val selectedShape = viewModel.selectedShape.value
                val canvasActionType = viewModel.canvasActionType.value
                val state by viewModel.state.collectAsStateWithLifecycle()

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = PrimaryColorHome1.value),
                    contentAlignment = Alignment.TopStart
                ) {
                    DrawingCanvas(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight()
                            .background(color = PrimaryColorHome1.value),
                        isHighlighterSelected = canvasActionType == CanvasActionType.DRAW_HIGHLIGHTER,
                        paths = state.paths,
                        currentPath = state.currentPath,
                        onAction = viewModel::onAction,
                    )

                    Column(
                        modifier = Modifier.wrapContentSize().background(color = Color.Transparent),
                        horizontalAlignment = Alignment.Start
                    ) {
                        ToolTipComponent(
                            selectedToolTip = selectedToolTip,
                            selectedShape = selectedShape,
                            onTextEvent = {
                                if (selectedToolTip == ToolTipType.BOARD_TEXT){
                                    viewModel.onAction(DrawingAction.OnToolTipChange(ToolTipType.IN_ACTIVE))
                                }else{
                                    viewModel.onAction(DrawingAction.OnToolTipChange(ToolTipType.BOARD_TEXT))
                                }
                            },
                            onBrushEvent = {
                                if (selectedToolTip == ToolTipType.BOARD_BRUSH){
                                    viewModel.onAction(DrawingAction.OnToolTipChange(ToolTipType.IN_ACTIVE))
                                }else{
                                    viewModel.onAction(DrawingAction.OnToolTipChange(ToolTipType.BOARD_BRUSH))
                                }
                            },
                            onHighlighterEvent = {
                                if (selectedToolTip == ToolTipType.BOARD_HIGHLIGHTER){
                                    viewModel.onAction(DrawingAction.OnToolTipChange(ToolTipType.IN_ACTIVE))
                                }else{
                                    viewModel.onAction(DrawingAction.OnToolTipChange(ToolTipType.BOARD_HIGHLIGHTER))
                                }
                            },
                            onEraserEvent = {
                                if (selectedToolTip == ToolTipType.BOARD_ERASER){
                                    viewModel.onAction(DrawingAction.OnToolTipChange(ToolTipType.IN_ACTIVE))
                                }else{
                                    viewModel.onAction(DrawingAction.OnToolTipChange(ToolTipType.BOARD_ERASER))
                                }
                            },
                            onShapesEvent = {
                                if (selectedToolTip == ToolTipType.BOARD_SHAPE){
                                    viewModel.onAction(DrawingAction.OnToolTipChange(ToolTipType.IN_ACTIVE))
                                }else{
                                    viewModel.onAction(DrawingAction.OnToolTipChange(ToolTipType.BOARD_SHAPE))
                                }
                            },
                            onNotesEvent = {
                                if (selectedToolTip == ToolTipType.BOARD_NOTE){
                                    viewModel.onAction(DrawingAction.OnToolTipChange(ToolTipType.IN_ACTIVE))
                                }else{
                                    viewModel.onAction(DrawingAction.OnToolTipChange(ToolTipType.BOARD_NOTE))
                                }
                            },
                            onInsertEvent = {
                                viewModel.onAction(DrawingAction.OnToolTipChange(ToolTipType.BOARD_INSERT))
                            },
                            onRedoEvent = {
                                viewModel.onAction(DrawingAction.OnToolTipChange(ToolTipType.BOARD_UNDO))
                            },
                            onUndoEvent = {
                                viewModel.onAction(DrawingAction.OnToolTipChange(ToolTipType.BOARD_REDO))
                            },
                            onBrushSelected = { size,color -> viewModel.onAction(DrawingAction.OnBrushSelected(size,color)) },
                            onEraserSelected = {size ->  viewModel.onAction(DrawingAction.OnEraserSelected(size))},
                            onShapeSelected = {type -> viewModel.onAction(DrawingAction.OnShapeSelected(type)) },
                            onNoteColorSelected = {color -> },
                            onCanvasActionTypeEvent = {canvasActionType -> viewModel.onAction(DrawingAction.OnCanvasActionTypeSelected(canvasActionType)) }
                        )
                    }
                }

                Box(contentAlignment = Alignment.TopEnd) {
                    ThreeDotComponent(
                        onSaveEvent = {
                            viewModel.onAction(DrawingAction.OnSaveBoard)
                            findNavController().popBackStack()
                        },
                        onExportEvent = {
                            viewModel.onAction(DrawingAction.OnExportBoard)
                            createFileLauncher.launch(viewModel.fileName.value)
                        }
                    )
                }
            }
        }
    }
}