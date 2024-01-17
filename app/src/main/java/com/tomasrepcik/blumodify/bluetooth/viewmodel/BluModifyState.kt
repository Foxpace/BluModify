package com.tomasrepcik.blumodify.bluetooth.viewmodel

import com.tomasrepcik.blumodify.app.model.AppResult

sealed class BluModifyState {
    data object Loading: BluModifyState()
    data object TurnedOff: BluModifyState()
    data object TurnedOn: BluModifyState()
    data object MissingPermission: BluModifyState()
    data class ErrorOccurred(val error: AppResult.Error<Boolean>): BluModifyState()
}
