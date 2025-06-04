package ly.david.data.test

import ly.david.musicsearch.data.musicbrainz.models.common.LifeSpanMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.core.EventMusicBrainzNetworkModel

val tsoAtMasseyHallEventMusicBrainzModel = EventMusicBrainzNetworkModel(
    id = "e636f98e-c214-4131-8ca9-2efd7c85197b",
    name = "Toronto Symphony Orchestra at Massey Hall",
    disambiguation = "",
    type = "Concert",
    lifeSpan = LifeSpanMusicBrainzModel(
        begin = "1981-09-12",
        end = "1981-09-12",
        ended = true,
    ),
    cancelled = false,
)

val kissAtScotiabankArenaEventMusicBrainzModel = EventMusicBrainzNetworkModel(
    id = "1fc7c24a-92f1-4b6b-86ff-1b59a0cd4d53",
    name = "KISS at Scotiabank Arena",
    disambiguation = "",
    type = "Concert",
    lifeSpan = LifeSpanMusicBrainzModel(
        begin = "2019-03-20",
        end = "2019-03-20",
        ended = true,
    ),
    cancelled = false,
    time = "18:30",
)

val kissAtBudokanEventMusicBrainzModel = EventMusicBrainzNetworkModel(
    id = "17e85876-40ea-423b-867c-80af2f0cdfe3",
    name = "KISS at Nippon Budokan",
    type = "Concert",
    lifeSpan = LifeSpanMusicBrainzModel(
        begin = "1977-04-01",
        end = "1977-04-01",
        ended = true,
    ),
    cancelled = false,
)

val aimerAtBudokanEventMusicBrainzModel = EventMusicBrainzNetworkModel(
    id = "34f8a930-beb2-441b-b0d7-03c84f92f1ea",
    name = "Aimer Live in 武道館 ”blanc et noir\"",
    type = "Concert",
    lifeSpan = LifeSpanMusicBrainzModel(
        begin = "2017-08-29",
        end = "2017-08-29",
        ended = true,
    ),
    cancelled = false,
    time = "18:00",
)
