package com.example.userriletion.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class History(
    val gejala: String,
    val jenis_gangguan: String,
    val solusi: String,
    val imageUrl: String
) : Parcelable
