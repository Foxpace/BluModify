package com.tomasrepcik.blumodify.bluetooth.viewmodel

import com.tomasrepcik.blumodify.app.model.AppResult

sealed class BlumodifyState {
    object Loading: BlumodifyState()
    object TurnedOff: BlumodifyState()
    object TurnedOn: BlumodifyState()
    object MissingPermission: BlumodifyState()
    class ErrorOccurred(val error: AppResult.Error<Boolean>): BlumodifyState()
}
