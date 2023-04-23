package com.tomasrepcik.blumodify.home.events

sealed class HomeScreenEvent {
    object OnLaunch: HomeScreenEvent()
    object OnMainButtonClickEvent: HomeScreenEvent()
}
