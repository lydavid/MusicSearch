package ly.david.mbjc.ui.search

import ly.david.mbjc.data.UiArtist

sealed class SearchArtistsUiModel {
    class Data(val uiArtist: UiArtist): SearchArtistsUiModel()
    object EndOfList: SearchArtistsUiModel()
}
