package com.iman.bnpl.actor.shared.model.dto

import com.iman.bnpl.domain.business.data.model.WorkHours

data class WorkHoursDto(val from: TimePeriodDto, val to: TimePeriodDto) {
    constructor(workHours: WorkHours): this(
        from = workHours.from.convertTo12HoursFormat(),
        to = workHours.to.convertTo12HoursFormat(),
    )
}

private fun String.convertTo12HoursFormat(): TimePeriodDto {
    val parts = this.split(":")
    val hour = parts[0].toInt()
    val period = if (hour < 12) "AM" else "PM"
    val adjustedHour = when {
        hour == 0 -> 12
        hour > 12 -> hour - 12
        else -> hour
    }
    return TimePeriodDto(adjustedHour, period)
}