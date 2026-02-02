package com.countries.ui.list

sealed interface BottomStatus {

    data object Online : BottomStatus
    data object NoInternet : BottomStatus
}