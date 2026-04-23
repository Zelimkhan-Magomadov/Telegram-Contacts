package zelimkhan.magomadov.contactsrevive.feature.importing.ui.importing

sealed interface ImportingEvent {
    data object UnsupportedFormat : ImportingEvent
}