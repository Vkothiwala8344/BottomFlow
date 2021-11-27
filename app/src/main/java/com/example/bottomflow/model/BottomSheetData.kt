package com.example.bottomflow.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class BottomSheetData(
    val title: Int,
    val subtitle: Int,
    val showAccept: Boolean = false,
    val showDecline: Boolean = false
) : Parcelable