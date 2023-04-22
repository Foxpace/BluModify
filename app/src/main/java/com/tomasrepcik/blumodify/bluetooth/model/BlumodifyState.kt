package com.tomasrepcik.blumodify.bluetooth.model

import com.tomasrepcik.blumodify.app.model.AppResult

sealed class BlumodifyState {
    object Loading: BlumodifyState()
    object TurnedOff: BlumodifyState()
    object TurnedOn: BlumodifyState()
    class ErrorOccurred(val error: AppResult.Error<Boolean>): BlumodifyState()
}
