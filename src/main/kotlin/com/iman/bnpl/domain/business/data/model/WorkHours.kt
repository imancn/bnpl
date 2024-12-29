package com.iman.bnpl.domain.business.data.model

import com.iman.bnpl.actor.shared.model.dto.WorkHoursDto
import com.iman.bnpl.application.advice.UnprocessableException

data class WorkHours(
    var from: String, var to: String
) {
    
    constructor(workHoursDto: WorkHoursDto) : this(
        from = workHoursDto.from.convertTo24HoursFormat(), to = workHoursDto.to.convertTo24HoursFormat()
    )
    
    fun validateFormat(): WorkHours {
        val regex = Regex("""^([01]\d|2[0-3]):([0-5]\d)$""")
        return when {
            !regex.matches(from) -> throw UnprocessableException("Invalid time format for 'from': $from. Expected HH:mm format.")
            !regex.matches(to) -> throw UnprocessableException("Invalid time format for 'to': $to. Expected HH:mm format.")
            else -> this
        }
    }
}
