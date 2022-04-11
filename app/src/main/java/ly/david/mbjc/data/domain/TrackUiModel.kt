package ly.david.mbjc.data.domain

import ly.david.mbjc.data.Track

data class TrackUiModel(
    override val id: String,
    override val position: Int,
    override val number: String,
    override val title: String,
    override val length: Int?
) : UiModel(), Track
