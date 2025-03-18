package ly.david.data.test

import ly.david.musicsearch.data.musicbrainz.models.common.ArtistCreditMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.core.ReleaseGroupMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.relation.Direction
import ly.david.musicsearch.data.musicbrainz.models.relation.SerializableMusicBrainzEntity
import ly.david.musicsearch.data.musicbrainz.models.relation.RelationMusicBrainzModel

val hotSpaceReleaseGroup = ReleaseGroupMusicBrainzModel(
    id = "3918b90b-340e-3779-9d7e-ba1593653498",
    name = "Hot Space",
    firstReleaseDate = "1982-05-21",
)

val underPressureReleaseGroup = ReleaseGroupMusicBrainzModel(
    id = "bdaeec2d-94f1-46b5-91f3-340ec6939c66",
    name = "Under Pressure",
    artistCredits = listOf(
        ArtistCreditMusicBrainzModel(
            artist = queenArtistMusicBrainzModel,
            name = "Queen",
            joinPhrase = " & ",
        ),
        ArtistCreditMusicBrainzModel(
            artist = davidBowieArtistMusicBrainzModel,
            name = "David Bowie",
            joinPhrase = "",
        ),
    ),
    primaryType = "Single",
    relations = listOf(
        RelationMusicBrainzModel(
            type = "single from",
            typeId = "fcf680a9-6871-4519-8c4b-8c6549575b35",
            direction = Direction.FORWARD,
            targetType = SerializableMusicBrainzEntity.RELEASE_GROUP,
            releaseGroup = hotSpaceReleaseGroup,
        ),
    ),
)

val utaNoUtaReleaseGroupMusicBrainzModel = ReleaseGroupMusicBrainzModel(
    id = "22760f81-37ce-47ce-98b6-65f8a285f083",
    name = "ウタの歌 ONE PIECE FILM RED",
    primaryType = "Album",
    secondaryTypes = listOf(),
    disambiguation = "",
    artistCredits = listOf(adoArtistCreditMusicBrainzModel),
    firstReleaseDate = "2022-08-10",
)
