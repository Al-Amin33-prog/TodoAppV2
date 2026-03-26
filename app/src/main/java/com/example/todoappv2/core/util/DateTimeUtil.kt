package com.example.todoappv2.core.util

import java.text.SimpleDateFormat
import java.util.Locale
import java.util.Calendar

fun formatDueDateLabel(dueDate: Long?): String?{
    if (dueDate == null) return null
    val now = Calendar.getInstance()
    val due = Calendar.getInstance().apply { timeInMillis = dueDate }
    return when {
        isSameDay(now,due) ->"Today"
        isTomorrow(now,due) -> "Tomorrow"
        due.before(now) -> "Overdue"
        else -> SimpleDateFormat("MMM dd", Locale.getDefault()).format(due.time)
    }
}
fun isSameDay(c1: Calendar, c2: Calendar): Boolean{
    return c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR) &&
            c1.get(Calendar.DAY_OF_YEAR) ==
            c2.get(Calendar.DAY_OF_YEAR)
}
fun isTomorrow(today: Calendar,other: Calendar): Boolean{
    val tomorrow = today.clone() as Calendar
    tomorrow.add(Calendar.DAY_OF_YEAR,1)
    return isSameDay(tomorrow,other)
}