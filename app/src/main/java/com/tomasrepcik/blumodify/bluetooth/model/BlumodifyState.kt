package com.tomasrepcik.blumodify.bluetooth.model

import com.tomasrepcik.blumodify.app.model.AppResult
import java.util.*

sealed class BlumodifyState {
    object Loading: BlumodifyState()
    object NothingToTrack: BlumodifyState()
    class TurnedOff(val uuid: UUID): BlumodifyState()
    class TurnedOn(val uuid: UUID): BlumodifyState()
    class ErrorOccurred(val error: AppResult.Error<Boolean>): BlumodifyState()
}
