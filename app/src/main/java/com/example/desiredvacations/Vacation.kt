package com.example.desiredvacations

import android.media.Image

data class Vacation (
    val vacationId: Int,
    val name: String,
    val location: String,
    val hotel: String = "Not selected",
    val neededMoney: String = "Not selected",
    val description: String = "Not selected",
//    val imageName: Image
)