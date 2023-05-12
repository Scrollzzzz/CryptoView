package com.scrollz.cryptoview.presentation.common

sealed class Status {
    object Normal: Status()
    object Loading: Status()
    object Error: Status()
}
