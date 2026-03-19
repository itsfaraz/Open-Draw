package com.designlife.opendraw.home.data.entity

data class CanvasBoard(
    val id : Long,
    val createdAt : Long,
    val lastModified : Long,
    val boardTitle : String,
    val category : String,
    val color : String,
    val fromImport : Boolean,
    val coverImage : ByteArray? = null
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CanvasBoard

        if (id != other.id) return false
        if (fromImport != other.fromImport) return false
        if (boardTitle != other.boardTitle) return false
        if (category != other.category) return false
        if (color != other.color) return false
        if (!coverImage.contentEquals(other.coverImage)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + boardTitle.hashCode()
        result = 32 * result + fromImport.hashCode()
        result = 33 * result + category.hashCode()
        result = 34 * result + color.hashCode()
        result = 35 * result + (coverImage?.contentHashCode() ?: 0)
        return result
    }
}
