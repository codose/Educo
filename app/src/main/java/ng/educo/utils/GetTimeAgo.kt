package ng.educo.utils

import java.text.SimpleDateFormat
import java.util.*


private const val SECOND_MILLIS = 1000
private const val MINUTE_MILLIS = 60 * SECOND_MILLIS
private const val HOUR_MILLIS = 60 * MINUTE_MILLIS
private const val DAY_MILLIS = 24 * HOUR_MILLIS

private val sdf1 = SimpleDateFormat("LLLL dd, YYYY HH:mm")

private val sdf = SimpleDateFormat("HH:mm")


private fun currentDate(): Date {
    val calendar: Calendar = Calendar.getInstance()
    return calendar.time
}

fun getTimeAgo(date: Date): String? {
    var time: Long = date.time
    if (time < 1000000000000L) {
        time *= 1000
    }
    val now: Long = currentDate().time
    if (time > now || time <= 0) {
        return "in the future"
    }
    val diff = now - time
    return when {
        diff < MINUTE_MILLIS -> {
            "moments ago"
        }
        diff < 2 * MINUTE_MILLIS -> {
            "a minute ago"
        }
        diff < 60 * MINUTE_MILLIS -> {
            " ${diff / MINUTE_MILLIS} minutes ago"
        }
        diff < 2 * HOUR_MILLIS -> {
            "an hour ago"
        }
        diff < 24 * HOUR_MILLIS -> {
            "${diff / HOUR_MILLIS} hours ago"
        }
        diff < 48 * HOUR_MILLIS -> {
            "yesterday"
        }
        else -> {
            "${diff / DAY_MILLIS} days ago"
        }
    }
}

fun getChatTime(date: Date): String? {
    var time: Long = date.time
    if (time < 1000000000000L) {
        time *= 1000
    }
    val now: Long = currentDate().time
    val diff = now - time
    return when {
        diff < 24 * HOUR_MILLIS -> {
            if(currentDate().day == date.day){
                sdf.format(date)
            }else{
                sdf1.format(date)
            }
        }
        else -> {
            sdf1.format(date)
        }
    }
}