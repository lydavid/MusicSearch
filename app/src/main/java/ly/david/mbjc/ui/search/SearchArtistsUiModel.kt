package ly.david.mbjc.ui.search

import ly.david.mbjc.data.UiArtist

// TODO: could generalize to hold data for any search types
sealed class SearchArtistsUiModel {
    class Data(val uiArtist: UiArtist): SearchArtistsUiModel()
    object EndOfList: SearchArtistsUiModel()
}
