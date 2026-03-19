package com.designlife.opendraw.board.data

import android.util.Log
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import com.designlife.opendraw.common.data.entity.InternalCanvasBoard
import com.designlife.opendraw.home.domain.converters.ColorConverter

data class PathData(
    val id : Long,
    val penSize : Float,
    val color : Color,
    val path : List<Offset>
){
    companion object{
        fun toStroke(pathData: PathData) : InternalCanvasBoard.Strokes{
            Log.i("FLOW", "PathData :: toStroke: Path Data For Id ${pathData.id}")
            return InternalCanvasBoard.Strokes(
                points = toOffsetList(pathData.path),
                color = toColor(pathData.color),
                width = pathData.penSize
            )
        }

        private fun toOffsetList(points: List<Offset>) : List<Pair<Float,Float>>{
            return points.map { offset -> Pair(offset.x,offset.y)}
        }

        private fun toColor(color: Color) : String{
            Log.i("FLOW", "PathData :: toStroke :: toColor : ${color}")
            Log.i("FLOW", "PathData :: toStroke :: Color change Value : ${color.value.toInt()}")
            return ColorConverter.serializeColor(color)
        }
    }
}