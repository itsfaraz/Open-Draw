package com.designlife.opendraw.board.presentation.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.util.fastForEach
import com.designlife.opendraw.board.data.PathData
import com.designlife.opendraw.board.domain.usecase.DrawingAction
import com.designlife.opendraw.ui.theme.PrimaryColorHome1
import kotlin.apply
import kotlin.collections.first
import kotlin.collections.isNotEmpty
import kotlin.collections.lastIndex
import kotlin.let
import kotlin.math.abs

@Composable
fun DrawingCanvas(
    isHighlighterSelected : Boolean,
    paths : List<PathData>,
    currentPath : PathData?,
    onAction: (action : DrawingAction) -> Unit,
    modifier: Modifier = Modifier
) {
    Canvas(
        modifier = modifier
            .clipToBounds()
            .background(PrimaryColorHome1.value)
            .pointerInput(true){
                detectDragGestures (
                    onDragStart = {onAction(DrawingAction.OnNewPathStart)},
                    onDragEnd = {onAction(DrawingAction.OnPathEnd)},
                    onDrag = { change, dragAmount -> onAction(DrawingAction.OnDraw(change.position))},
                    onDragCancel = { onAction(DrawingAction.OnPathEnd) }
                )
            },

    ){
        paths.fastForEach { pathData ->
            drawPath(
                path = pathData.path,
                color = pathData.color,
                thickness = pathData.penSize
            )
        }
        currentPath?.let {
            if (isHighlighterSelected){
                drawGradientPath(
                    path = it.path,
                    thickness = it.penSize
                )
            }else{
                drawPath(
                    path = it.path,
                    color = it.color,
                    thickness = it.penSize
                )
            }

        }
    }

}


internal fun DrawScope.drawPath(
    path : List<Offset>,
    color: Color,
    thickness : Float = 10F
){
    val smoothedPath = Path().apply {
        if (path.isNotEmpty()){
            moveTo(path.first().x,path.first().y)
            val smoothness = 5

            for (i in 1..path.lastIndex){
                val from = path[i - 1]
                val to = path[i]
                val dx = abs(from.x - to.x)
                val dy = abs((from.y - to.y))
                if (dx >= smoothness || dy >= smoothness){
                    quadraticTo(
                        x1 = (from.x + to.x) / 2f,
                        y1 = (from.y + to.y) / 2f,
                        x2 = to.x,
                        y2 = to.y
                    )
                }
            }
        }
    }

    drawPath(
        path = smoothedPath,
        color = color,
        style = Stroke(
            width = thickness,
            cap = StrokeCap.Round,
            join = StrokeJoin.Round
        )
    )
}

internal fun DrawScope.drawGradientPath(
    path : List<Offset>,
    thickness : Float = 10F
){
    val smoothedPath = Path().apply {
        if (path.isNotEmpty()){
            moveTo(path.first().x,path.first().y)
            val smoothness = 5

            for (i in 1..path.lastIndex){
                val from = path[i - 1]
                val to = path[i]
                val dx = abs(from.x - to.x)
                val dy = abs((from.y - to.y))
                if (dx >= smoothness || dy >= smoothness){
                    quadraticTo(
                        x1 = (from.x + to.x) / 2f,
                        y1 = (from.y + to.y) / 2f,
                        x2 = to.x,
                        y2 = to.y
                    )
                }
            }
        }
    }
    val gradientBrush = Brush.linearGradient(
        colors = listOf(Color.Magenta, Color.Blue, Color.Yellow, Color.DarkGray, Color.Cyan),
        start = Offset(0f, 0f),
        end = Offset(size.width, size.height),
    )

    drawPath(
        path = smoothedPath,
        brush = gradientBrush,
        style = Stroke(
            width = thickness,
            cap = StrokeCap.Round,
            join = StrokeJoin.Round
        )
    )
}