package com.happy.playground.util.typeadapter

import com.squareup.moshi.*
import java.io.IOException
import java.text.ParseException

class EnsuresLongAdapter : JsonAdapter<Long>() {

    @ToJson
    override fun toJson(writer: JsonWriter?, @EnsureLong value: Long?) {
        writer?.let {
            writer.value(value?.toString())
        }
    }
    @FromJson
    @EnsureLong
    override fun fromJson(reader: JsonReader?): Long? {
        reader?.let {
            return when (reader.peek()) {
                JsonReader.Token.STRING -> stringToLong(reader.nextString())
                JsonReader.Token.NUMBER -> reader.nextLong()
                JsonReader.Token.NULL -> reader.nextNull()
                else -> reader.nextNull()
            }
        }
        return null
    }

    private fun stringToLong(stringFromJson: String): Long? {
        try {
            return stringFromJson.toLong()
        } catch (e: ParseException) {
            e.printStackTrace()
            throw IOException("Error parsing long: $stringFromJson", e)
        }
    }
}