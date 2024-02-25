package ly.david.musicsearch.shared.feature.licenses

internal sealed interface LicensesUiEvent {
    data object NavigateUp : LicensesUiEvent
}
