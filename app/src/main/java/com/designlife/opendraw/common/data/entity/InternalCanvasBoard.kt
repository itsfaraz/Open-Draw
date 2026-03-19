package com.designlife.opendraw.common.data.entity

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.fromColorLong
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.designlife.opendraw.board.data.PathData
import com.designlife.opendraw.board.domain.enums.ShapeType
import com.designlife.opendraw.common.utils.ServiceLocator
import com.designlife.opendraw.home.domain.converters.ColorConverter
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "CanvasBoard")
data class InternalCanvasBoard(
    @PrimaryKey(autoGenerate = true)
    @Serializable
    val boardId: Long = 0L,
    val createdAt : Long = 0L ,
    val lastModifiedAt : Long = 0L,
    val boardTitle : String = "",
    val category : String = "",
    @Serializable
    val strokes: List<Strokes> = emptyList(),
    @Serializable
    val shapes: List<Shape> = emptyList(),
    @Serializable
    val texts: List<Text> = emptyList(),
) {
    @Serializable
    data class Strokes(
        @Serializable
        val points: List<Pair<Float,Float>>,
        @Serializable
        val color: String,
        @Serializable
        val width: Float
    )

    @Serializable
    data class Shape(
        @Serializable
        val type: String,
        @Serializable
        val topLeft: Pair<Float,Float>,
        @Serializable
        val bottomRight: Pair<Float,Float>,
        @Serializable
        val color: String = ""
    ){
        fun getShapeEnum() : ShapeType{
            return if (type.equals(ShapeType.CIRCLE.name)){ ShapeType.CIRCLE }
            else if (type.equals(ShapeType.SQUARE.name)){
                ShapeType.SQUARE
            }
            else if (type.equals(ShapeType.RECTANGLE.name)){
                ShapeType.RECTANGLE
            }
            else if (type.equals(ShapeType.STAR.name)){
                ShapeType.STAR
            }
            else if (type.equals(ShapeType.ELLIPSE.name)){
                ShapeType.ELLIPSE
            }
            else if (type.equals(ShapeType.POLYGON.name)){
                ShapeType.POLYGON
            }
            else if (type.equals(ShapeType.LINE.name)){
                ShapeType.LINE
            }else ShapeType.UN_DEFINED
        }
    }

    @Serializable
    data class Text(
        @Serializable
        val text: String,
        @Serializable
        val position:  Pair<Float,Float>,
        @Serializable
        val color: String,
        @Serializable
        val size: Float
    )

    companion object{
        fun toJson(board : InternalCanvasBoard) : String{
            val gson = ServiceLocator.provideGson()
            return gson.toJson(board, InternalCanvasBoard::class.java)
        }

        fun fromJson(board : String) : InternalCanvasBoard{
            val gson = ServiceLocator.provideGson()
            return gson.fromJson(board, InternalCanvasBoard::class.java)
        }

        fun fromStroke(boardId : Long,strokes: List<InternalCanvasBoard.Strokes>) : List<PathData>{
            return strokes.map { stroke ->
                PathData(
                    id = boardId,
                    penSize = stroke.width,
                    color = toColor(stroke.color),
                    path = toPathList(stroke.points)
                )
            }
        }

        private fun toPathList(points: List<Pair<Float,Float>>) : List<Offset>{
            return points.map { pair -> Offset(pair.first,pair.second)}
        }

        private fun toColor(color: String) : Color{
            return ColorConverter.deserializeColor(color)
        }

    }
}


