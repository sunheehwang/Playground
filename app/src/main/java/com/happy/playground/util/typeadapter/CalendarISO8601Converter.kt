package com.happy.playground.util.typeadapter

import com.happy.playground.util.TimberLogger
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/*class CalendarISO8601Converter : ISO8601Converter {
    override fun fromTimestamp(timestamp: Long?): String? {
        timestamp?.let {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = timestamp
            val df = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            TimberLogger.debug("df = $calendar")
            return df.format(calendar.time)
        }
        return null
    }

    override fun toTimestamp(date: String?): Long? {
        date?.let {
            val df = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            var timestamp: Long = -1
            try {
                timestamp = df.parse(date).time
            } catch (e: ParseException) {
                e.printStackTrace()
            }

            return timestamp
        }

        return null
    }

}*/

class CalendarISO8601Converter : ISO8601Converter {
    override fun fromTimestamp(timestamp: Long?): String? {
        timestamp?.let {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = timestamp
            val df = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            return df.format(calendar.time)
        }
        return null
    }

    override fun toTimestamp(date: String?): Long? {
        date?.let {
            val df = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            var timestamp: Long = -1
            try {
                timestamp = df.parse(date).time
            } catch (e: ParseException) {
                e.printStackTrace()
            }

            return timestamp
        }
        return null
    }
}



