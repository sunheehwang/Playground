package com.happy.playground.util.typeadapter

import com.happy.playground.util.extensions.ifNull
import com.squareup.moshi.*
import java.io.IOException
import java.text.ParseException

class TimestampAdapter(private val dateConverter: ISO8601Converter) : JsonAdapter<Long>() {
    private val options = JsonReader.Options.of("\$date")

    @ToJson
    override fun toJson(writer: JsonWriter, @ISO8610Date value: Long?) {
        value?.let {
            writer.value(dateConverter.fromTimestamp(it))
        }.ifNull {
            writer.nullValue()
        }
    }


    @FromJson
    @ISO8610Date
    override fun fromJson(reader: JsonReader): Long? {
        var timestamp: Long? = null

        when (reader.peek()) {
            JsonReader.Token.BEGIN_OBJECT -> {
                reader.beginObject()
                if (reader.hasNext()) {
                    when (reader.selectName(options)) {
                        0 -> {
                            timestamp = if (reader.peek() == JsonReader.Token.NULL) {
                                reader.nextNull<Long>()
                            } else {
                                reader.nextLong()
                            }
                        }
                        -1 -> {
                            reader.nextName()
                            reader.skipValue()
                        }
                    }
                }
                reader.endObject()
            }
            JsonReader.Token.STRING -> {
                val result = reader.nextString()
                if (!result.isNullOrBlank()) {
                    try {
                        timestamp = dateConverter.toTimestamp(result)
                    } catch (e: ParseException) {
                        e.printStackTrace()
                        throw IOException("Error parsing date: $result", e)
                    }
                }
            }
            else -> reader.skipValue()
        }

        return timestamp
    }

}