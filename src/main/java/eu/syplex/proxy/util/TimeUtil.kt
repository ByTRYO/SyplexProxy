package eu.syplex.proxy.util

import java.sql.Timestamp
import java.util.concurrent.TimeUnit
import kotlin.math.abs

object TimeUtil {

    fun checkTimeElapsed(expires: Timestamp?): Boolean {
        if (expires == null) return false
        return expires.time <= System.currentTimeMillis()
    }

    fun createReadableTimeString(timestamp: Timestamp): String {
        var restTime = ""
        val diff = abs((timestamp.time - System.currentTimeMillis()) / 1000)

        val days = (diff / 86400).toInt()
        val hours = ((diff / 3600) % 24).toInt()
        val minutes = ((diff / 60) % 60).toInt()
        val seconds = (diff % 60).toInt()

        if (days > 0) restTime += if (days > 1) "$days Tage" else " 1 Tag"
        if (hours > 0) restTime += if (hours > 1) " $hours Stunden" else " 1 Stunde"
        if (minutes > 0) restTime += if (minutes > 1) " $minutes Minuten" else " 1 Minute"
        if (seconds > 0) restTime += if (seconds > 1) " $seconds Sekunden" else " 1 Sekunde"

        return restTime
    }

    fun getDurationString(date: Timestamp, expires: Timestamp?): String {
        if (expires == null) return "PERMANENT"

        val diff = abs(expires.time - date.time)
        val days = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS)

        if (days == 1L) return "1 Tag"
        return "$days Tage"
    }

}