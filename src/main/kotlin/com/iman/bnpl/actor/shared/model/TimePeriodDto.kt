package com.iman.bnpl.actor.shared.model

data class TimePeriodDto(val time: Int, val period: String) {
    fun convertTo24HoursFormat(): String {
        val adjustedTime = if (period.equals("PM", true) && time != 12) {
            time + 12
        } else if (period.equals("AM", true) && time == 12) {
            0
        } else {
            time
        }
        return "%02d:00".format(adjustedTime)
    }
}