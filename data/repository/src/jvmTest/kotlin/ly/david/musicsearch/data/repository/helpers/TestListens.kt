package ly.david.musicsearch.data.repository.helpers

import ly.david.musicsearch.data.listenbrainz.api.AdditionalInfo
import ly.david.musicsearch.data.listenbrainz.api.ListenBrainzArtist
import ly.david.musicsearch.data.listenbrainz.api.ListenBrainzListen
import ly.david.musicsearch.data.listenbrainz.api.ListensResponse
import ly.david.musicsearch.data.listenbrainz.api.MbidMapping
import ly.david.musicsearch.data.listenbrainz.api.Payload
import ly.david.musicsearch.data.listenbrainz.api.TrackMetadata

const val TEST_USERNAME = "user"

const val track1ListenedAtS = 1756100634L
const val track1ListenedAtMs = track1ListenedAtS * 1000
const val track2ListenedAtS = 1755100633L
const val track2ListenedAtMs = track2ListenedAtS * 1000
const val track3ListenedAtS = 1755100632L
const val track3ListenedAtMs = track3ListenedAtS * 1000
const val track4ListenedAtS = 1755100631L
const val track4ListenedAtMs = track4ListenedAtS * 1000
const val track5ListenedAtS = 1755100630L
const val track5ListenedAtMs = track5ListenedAtS * 1000
const val track6ListenedAtS = 1755100629L
const val track6ListenedAtMs = track6ListenedAtS * 1000

val testListens = ListensResponse(
    payload = Payload(
        latest_listen_ts = track1ListenedAtS,
        oldest_listen_ts = track4ListenedAtS,
        listens = listOf(
            // with mapping
            ListenBrainzListen(
                insertedAtS = 1755101240L,
                listenedAtS = track1ListenedAtS,
                recording_msid = "f5700f45-6003-40ee-9c01-3ea270c77cd3",
                user_name = TEST_USERNAME,
                track_metadata = TrackMetadata(
                    artist_name = "ano, Lilas",
                    track_name = "絶絶絶絶対聖域",
                    release_name = "絶絶絶絶対聖域",
                    additional_info = AdditionalInfo(
                        duration_ms = 213868L,
                        submission_client = "listenbrainz",
                        music_service = "spotify.com",
                        origin_url = "https://open.spotify.com/track/3n4p9wJEgt4szBc92wPwmu",
                        spotify_album_artist_ids = listOf(
                            "https://open.spotify.com/artist/7Il739Q5W4yJUYC3hfnX6z",
                        ),
                        spotify_album_id = "https://open.spotify.com/album/0qsnfQzcoZgycLGjJ9zKom",
                        spotify_artist_ids = listOf(
                            "https://open.spotify.com/artist/7Il739Q5W4yJUYC3hfnX6z",
                            "https://open.spotify.com/artist/1qM11R4ylJyQiPJ0DffE9z",
                        ),
                        spotify_id = "https://open.spotify.com/track/3n4p9wJEgt4szBc92wPwmu",
                    ),
                    mbid_mapping = MbidMapping(
                        recording_mbid = "57c4f7cb-99f1-4305-bf3e-9ea51cc243f0",
                        recording_name = "絶絶絶絶対聖域",
                        artists = listOf(
                            ListenBrainzArtist(
                                artist_credit_name = "ano",
                                artist_mbid = "ebb4513e-4aab-4ac9-a949-14e77bb7b836",
                                join_phrase = " feat. ",
                            ),
                            ListenBrainzArtist(
                                artist_credit_name = "幾田りら",
                                artist_mbid = "55e42264-ef27-49d8-93fd-29f930dc96e4",
                                join_phrase = "",
                            ),
                        ),
                        caa_id = 42143556739L,
                        caa_release_mbid = "71c9f176-e6e3-4610-807d-b8a11b870df3",
                        release_mbid = "837e8abc-01e9-4ef9-9a69-4a4e9d3455fa",
                    ),
                ),
            ),
            ListenBrainzListen(
                insertedAtS = 1755101240L,
                listenedAtS = track2ListenedAtS,
                recording_msid = "28f390ae-b7a3-4636-82bc-7d39a7348978",
                user_name = TEST_USERNAME,
                track_metadata = TrackMetadata(
                    artist_name = "高橋あず美, Lotus Juice, アトラスサウンドチーム, ATLUS GAME MUSIC",
                    track_name = "Color Your Night",
                    release_name = "Persona 3 Reload Original Soundtrack",
                    additional_info = AdditionalInfo(
                        duration_ms = 227240,
                        submission_client = "listenbrainz",
                        music_service = "spotify.com",
                        origin_url = "https://open.spotify.com/track/4pjFNyjGaoKgLTnndISP6V",
                        spotify_album_artist_ids = listOf(
                            "https://open.spotify.com/artist/4hFBhdNVZZuVk5FYThUwaN",
                            "https://open.spotify.com/artist/7tUDDR0lAc9PLMPHPfzaqI",
                        ),
                        spotify_album_id = "https://open.spotify.com/album/20Bf2RVERC5Bc2eo3vyvJv",
                        spotify_artist_ids = listOf(
                            "https://open.spotify.com/artist/4VeqFgWkP7P9eEGwzPuXcM",
                            "https://open.spotify.com/artist/0HM4KuHUJ5ww5DdOGi3FEf",
                            "https://open.spotify.com/artist/4hFBhdNVZZuVk5FYThUwaN",
                            "https://open.spotify.com/artist/7tUDDR0lAc9PLMPHPfzaqI",
                        ),
                        spotify_id = "https://open.spotify.com/track/4pjFNyjGaoKgLTnndISP6V",
                    ),
                    mbid_mapping = MbidMapping(
                        recording_mbid = "e68e22b0-241e-4a6a-b4bf-0cfa8b83fda1",
                        recording_name = "Color Your Night",
                        artists = listOf(
                            ListenBrainzArtist(
                                artist_credit_name = "Lotus Juice",
                                artist_mbid = "c731e592-2620-4f4c-859d-39e294b06b35",
                                join_phrase = " & ",
                            ),
                            ListenBrainzArtist(
                                artist_credit_name = "高橋あず美",
                                artist_mbid = "2bd16069-0d18-4925-a4c0-cf99344cca0b",
                                join_phrase = "",
                            ),
                        ),
                        caa_id = 40524230813,
                        caa_release_mbid = "0d516a93-061e-4a27-9cf7-f36e3a96f888",
                        release_mbid = "0d516a93-061e-4a27-9cf7-f36e3a96f888",
                    ),
                ),
            ),
            ListenBrainzListen(
                insertedAtS = 1755101240L,
                listenedAtS = track3ListenedAtS,
                recording_msid = "9e164036-5379-4bbd-8a9b-fb7b9e697993",
                user_name = TEST_USERNAME,
                track_metadata = TrackMetadata(
                    artist_name = "高橋あず美, Lotus Juice, アトラスサウンドチーム, ATLUS GAME MUSIC",
                    track_name = "Full Moon Full Life",
                    release_name = "Persona 3 Reload Original Soundtrack",
                    additional_info = AdditionalInfo(
                        duration_ms = 293493,
                        submission_client = "listenbrainz",
                        music_service = "spotify.com",
                        origin_url = "https://open.spotify.com/track/3Jl2LQmRwbXEF2lO1RTvxn",
                        spotify_album_artist_ids = listOf(
                            "https://open.spotify.com/artist/4hFBhdNVZZuVk5FYThUwaN",
                            "https://open.spotify.com/artist/7tUDDR0lAc9PLMPHPfzaqI",
                        ),
                        spotify_album_id = "https://open.spotify.com/album/20Bf2RVERC5Bc2eo3vyvJv",
                        spotify_artist_ids = listOf(
                            "https://open.spotify.com/artist/4VeqFgWkP7P9eEGwzPuXcM",
                            "https://open.spotify.com/artist/0HM4KuHUJ5ww5DdOGi3FEf",
                            "https://open.spotify.com/artist/4hFBhdNVZZuVk5FYThUwaN",
                            "https://open.spotify.com/artist/7tUDDR0lAc9PLMPHPfzaqI",
                        ),
                        spotify_id = "https://open.spotify.com/track/3Jl2LQmRwbXEF2lO1RTvxn",
                    ),
                    mbid_mapping = MbidMapping(
                        recording_mbid = "c4090c59-be0c-4a79-b76d-5e2669e0cd4c",
                        recording_name = "Full Moon Full Life",
                        artists = listOf(
                            ListenBrainzArtist(
                                artist_credit_name = "Lotus Juice",
                                artist_mbid = "c731e592-2620-4f4c-859d-39e294b06b35",
                                join_phrase = " & ",
                            ),
                            ListenBrainzArtist(
                                artist_credit_name = "高橋あず美",
                                artist_mbid = "2bd16069-0d18-4925-a4c0-cf99344cca0b",
                                join_phrase = "",
                            ),
                        ),
                        caa_id = 40524230813,
                        caa_release_mbid = "0d516a93-061e-4a27-9cf7-f36e3a96f888",
                        release_mbid = "0d516a93-061e-4a27-9cf7-f36e3a96f888",
                    ),
                ),
            ),
            // minimum without mapping
            ListenBrainzListen(
                insertedAtS = 1755101240L,
                listenedAtS = track4ListenedAtS,
                recording_msid = "e46e0ad5-6b2d-4ab1-aa68-acd29dd204f2",
                user_name = TEST_USERNAME,
                track_metadata = TrackMetadata(
                    artist_name = "Tsukuyomi",
                    track_name = "Absolute zero",
                ),
            ),
            ListenBrainzListen(
                insertedAtS = 1755101240L,
                listenedAtS = track5ListenedAtS,
                recording_msid = "10821143-ab67-4cfa-9ceb-c837bf8b4bdf",
                user_name = TEST_USERNAME,
                track_metadata = TrackMetadata(
                    artist_name = "いとうかなこ",
                    track_name = "スカイクラッドの観測者",
                    release_name = "ChaosAttractor",
                    additional_info = AdditionalInfo(
                        duration_ms = 275640,
                        submission_client = "listenbrainz",
                        music_service = "spotify.com",
                        origin_url = "https://open.spotify.com/track/3Y0W7Lxg1X4cbyvtmdCHzL",
                        spotify_album_artist_ids = listOf(
                            "https://open.spotify.com/artist/2d12dVIZQZk9CKhEsezaoN",
                        ),
                        spotify_album_id = "https://open.spotify.com/album/0yHtsepi9vEUIxuHq5ohz7",
                        spotify_artist_ids = listOf(
                            "https://open.spotify.com/artist/2d12dVIZQZk9CKhEsezaoN",
                        ),
                        spotify_id = "https://open.spotify.com/track/3Y0W7Lxg1X4cbyvtmdCHzL",
                    ),
                    mbid_mapping = MbidMapping(
                        recording_mbid = "6a8fc477-9b12-4001-9387-f5d936b05503",
                        recording_name = "スカイクラッドの観測者",
                        artists = listOf(
                            ListenBrainzArtist(
                                artist_credit_name = "いとうかなこ",
                                artist_mbid = "eee65fbc-ead0-4c01-b385-a6f563c418d3",
                                join_phrase = "",
                            ),
                        ),
                        caa_id = 12397242767,
                        caa_release_mbid = "2387c59b-62c4-4752-b1fa-64f126ed0c8c",
                        release_mbid = "2387c59b-62c4-4752-b1fa-64f126ed0c8c",
                    ),
                ),
            ),
            ListenBrainzListen(
                insertedAtS = 1755101240L,
                listenedAtS = track6ListenedAtS,
                recording_msid = "77f971a8-6748-4314-9513-dffbc0969724",
                user_name = TEST_USERNAME,
                track_metadata = TrackMetadata(
                    artist_name = "Roselia, いとうかなこ",
                    track_name = "スカイクラッドの観測者",
                    release_name = "バンドリ！ ガールズバンドパーティ！ カバーコレクションVol.8",
                    additional_info = AdditionalInfo(
                        duration_ms = 273866,
                        submission_client = "listenbrainz",
                        music_service = "spotify.com",
                        origin_url = "https://open.spotify.com/track/2sgtG25jRdtNyV2ALfnBUP",
                        spotify_album_artist_ids = listOf(
                            "https://open.spotify.com/artist/0LyfQWJT6nXafLPZqxe9Of",
                        ),
                        spotify_album_id = "https://open.spotify.com/album/0XItoAX0PgQGz9676FKNSh",
                        spotify_artist_ids = listOf(
                            "https://open.spotify.com/artist/1bljHdc9g7V7ZLkYuf4yfy",
                            "https://open.spotify.com/artist/2d12dVIZQZk9CKhEsezaoN",
                        ),
                        spotify_id = "https://open.spotify.com/track/2sgtG25jRdtNyV2ALfnBUP",
                    ),
                    mbid_mapping = MbidMapping(
                        recording_mbid = "cb10d0b9-26a5-4f84-93bb-ddcffa39c170",
                        recording_name = "スカイクラッドの観測者",
                        artists = listOf(
                            ListenBrainzArtist(
                                artist_credit_name = "Roselia",
                                artist_mbid = "adea3c3d-a84d-4f9e-ac0b-1ef71a8947a5",
                                join_phrase = "×",
                            ),
                            ListenBrainzArtist(
                                artist_credit_name = "いとうかなこ",
                                artist_mbid = "eee65fbc-ead0-4c01-b385-a6f563c418d3",
                                join_phrase = "",
                            ),
                        ),
                        caa_id = 36678276363,
                        caa_release_mbid = "06fecdc4-dbfa-484f-a03b-5da975fadf0e",
                        release_mbid = "06fecdc4-dbfa-484f-a03b-5da975fadf0e",
                    ),
                ),
            ),
        ),
    ),
)
