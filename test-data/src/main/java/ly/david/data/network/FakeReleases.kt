package ly.david.data.network

val fakeRelease = ReleaseMusicBrainzModel(
    id = "fakeRelease1",
    name = "Release Name",
    artistCredits = listOf(fakeArtistCredit, fakeArtistCredit2),
    releaseGroup = fakeReleaseGroup,
    releaseEvents = listOf(
        ReleaseEvent(
            area = fakeCountry,
            date = "2022-10-29"
        )
    ),
    media = listOf(fakeMedia),
    labelInfoList = listOf(
        LabelInfo(
            catalogNumber = "CAT 1",
            label = fakeLabel
        ),
        LabelInfo(
            label = fakeLabel2
        )
    )
)

val fakeReleaseWithCoverArt = ReleaseMusicBrainzModel(
    id = "fakeRelease2",
    name = "Release With Cover Art",
    artistCredits = listOf(fakeArtistCredit, fakeArtistCredit2),
    releaseGroup = fakeReleaseGroup,
    media = listOf(fakeMedia),
    coverArtArchive = CoverArtArchive(
        count = 1
    )
)

val remasterOf = RelationMusicBrainzModel(
    type = "blah",
    typeId = "48e327b5-2d04-4518-93f1-fed5f0f0fa3c",
    direction = Direction.FORWARD,
    targetType = MusicBrainzResource.RELEASE,
    release = fakeReleaseWithCoverArt
)

val fakeReleaseWithRelation = ReleaseMusicBrainzModel(
    id = "fakeRelease3",
    name = "Fake Release Relationship",
    artistCredits = listOf(fakeArtistCredit, fakeArtistCredit2),
    releaseGroup = fakeReleaseGroup,
    relations = listOf(remasterOf)
)

val fakeReleases = listOf(
    fakeRelease,
    fakeReleaseWithRelation,
    fakeReleaseWithCoverArt
)
