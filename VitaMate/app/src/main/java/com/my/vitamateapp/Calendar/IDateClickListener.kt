package com.my.vitamateapp.Calendar

import java.time.LocalDate

interface IDateClickListener {
    fun onClickDate(date: LocalDate)
}