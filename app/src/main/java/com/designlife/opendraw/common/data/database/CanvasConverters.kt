package com.designlife.opendraw.common.data.database

import androidx.room.TypeConverter
import com.designlife.opendraw.common.data.entity.InternalCanvasBoard
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json


class CanvasConverters {
    private val json = Json { encodeDefaults = true }

    @TypeConverter
    fun fromStrokesList(strokes: List<InternalCanvasBoard.Strokes>): String =
        json.encodeToString(strokes)

    @TypeConverter
    fun toStrokesList(data: String): List<InternalCanvasBoard.Strokes> =
        json.decodeFromString(data)

    @TypeConverter
    fun fromShapesList(shapes: List<InternalCanvasBoard.Shape>): String =
        json.encodeToString(shapes)

    @TypeConverter
    fun toShapesList(data: String): List<InternalCanvasBoard.Shape> =
        json.decodeFromString(data)

    @TypeConverter
    fun fromTextsList(texts: List<InternalCanvasBoard.Text>): String =
        json.encodeToString(texts)

    @TypeConverter
    fun toTextsList(data: String): List<InternalCanvasBoard.Text> =
        json.decodeFromString(data)

}