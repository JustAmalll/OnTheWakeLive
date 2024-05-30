package core.presentation

sealed interface MainEvent {
    data object OnMainScreenAppeared : MainEvent
    data object ToggleNavigationBarVisibility: MainEvent
}