package com.designlife.opendraw.common.utils.serializer

import androidx.compose.ui.geometry.Offset
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable
private data class OffsetSurrogate(val x: Float, val y: Float)

object OffsetSerializer : KSerializer<Offset> {
    override val descriptor = OffsetSurrogate.serializer().descriptor

    override fun deserialize(decoder: Decoder): Offset {
        return decoder.decodeSerializableValue(OffsetSurrogate.serializer())
            .let { Offset(it.x, it.y) }
    }

    override fun serialize(
        encoder: Encoder,
        value: Offset
    ) {
        encoder.encodeSerializableValue(
            OffsetSurrogate.serializer(),
            OffsetSurrogate(value.x, value.y)
        )
    }
}