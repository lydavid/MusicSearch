package ly.david.musicsearch.shared.feature.details.artist

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import ly.david.musicsearch.shared.domain.LifeSpanUiModel
import ly.david.musicsearch.shared.domain.artist.ArtistDetailsModel
import ly.david.musicsearch.shared.domain.artist.MembersAndGroups
import ly.david.musicsearch.shared.domain.listitem.AreaListItemModel
import ly.david.musicsearch.shared.domain.listitem.RelationListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.wikimedia.WikipediaExtract
import ly.david.musicsearch.ui.core.theme.PreviewTheme

// region Previews
//@PreviewLightDark
//@Composable
//internal fun PreviewArtistDetailsUi() {
//    PreviewTheme {
//        Surface {
//            ArtistDetailsUi(
//                artist = ArtistDetailsModel(
//                    id = "b10bbbfc-cf9e-42e0-be17-e2c3e1d2600d",
//                    name = "The Beatles",
//                    type = "Group",
//                    lifeSpan = LifeSpanUiModel(
//                        begin = "1960",
//                        end = "1970-04-10",
//                        ended = true,
//                    ),
//                    sortName = "Beatles, The",
//                    areaListItemModel = AreaListItemModel(
//                        id = "8a754a16-0027-3a29-b6d7-2b40ea0481ed",
//                        name = "United Kingdom",
//                        countryCodes = listOf("GB"),
//                    ),
//                    imageUrl = "https://i.scdn.co/image/ab6761610000e5ebe9348cc01ff5d55971b22433",
//                    wikipediaExtract = WikipediaExtract(
//                        extract = "The Beatles were an English rock band formed in Liverpool in 1960, comprising John Lennon, Paul McCartney, George Harrison and Ringo Starr. They are widely regarded as the most influential band of all time and were integral to the development of 1960s counterculture and the recognition of popular music as an art form. Rooted in skiffle, beat and 1950s rock 'n' roll, their sound incorporated elements of classical music and traditional pop in innovative ways. The band also explored music styles ranging from folk and Indian music to psychedelia and hard rock. As pioneers in recording, songwriting and artistic presentation, the Beatles revolutionized many aspects of the music industry and were often publicized as leaders of the era's youth and sociocultural movements.\n" +
//                            "Led by primary songwriters Lennon and McCartney, the Beatles evolved from Lennon's previous group, the Quarrymen, and built their reputation by playing clubs in Liverpool and Hamburg, Germany, over three years from 1960, initially with Stuart Sutcliffe playing bass. The core trio of Lennon, McCartney and Harrison, together since 1958, went through a succession of drummers, including Pete Best, before inviting Starr to join them in 1962. Manager Brian Epstein moulded them into a professional act, and producer George Martin guided and developed their recordings, greatly expanding their domestic success after they signed with EMI Records and achieved their first hit, \"Love Me Do\", in late 1962. As their popularity grew into the intense fan frenzy dubbed \"Beatlemania\", the band acquired the nickname \"the Fab Four\". Epstein, Martin or other members of the band's entourage were sometimes informally referred to as a \"fifth Beatle\".\n" +
//                            "By early 1964, the Beatles were international stars and had achieved unprecedented levels of critical and commercial success. They became a leading force in Britain's cultural resurgence, ushering in the British Invasion of the United States pop market. They soon made their film debut with A Hard Day's Night (1964). A growing desire to refine their studio efforts, coupled with the challenging nature of their concert tours, led to the band's retirement from live performances in 1966. During this time, they produced albums of greater sophistication, including Rubber Soul (1965), Revolver (1966) and Sgt. Pepper's Lonely Hearts Club Band (1967). They enjoyed further commercial success with The Beatles (also known as \"the White Album\", 1968) and Abbey Road (1969). The success of these records heralded the album era, as albums became the dominant form of record use over singles. These records also increased public interest in psychedelic drugs and Eastern spirituality and furthered advancements in electronic music, album art and music videos. In 1968, they founded Apple Corps, a multi-armed multimedia corporation that continues to oversee projects related to the band's legacy. After the group's break-up in 1970, all principal former members enjoyed success as solo artists, and some partial reunions occurred. Lennon was murdered in 1980, and Harrison died of lung cancer in 2001. McCartney and Starr remain musically active.\n" +
//                            "The Beatles are the best-selling music act of all time, with estimated sales of 600 million units worldwide. They are the most successful act in the history of the US Billboard charts, holding the record for most number-one albums on the UK Albums Chart (15), most number-one hits on the US Billboard Hot 100 chart (20), and most singles sold in the UK (21.9 million). The band received many accolades, including seven Grammy Awards, four Brit Awards, an Academy Award (for Best Original Song Score for the 1970 documentary film Let It Be) and fifteen Ivor Novello Awards. They were inducted into the Rock and Roll Hall of Fame in their first year of eligibility, 1988, and each principal member was individually inducted between 1994 and 2015. In 2004 and 2011, the group topped Rolling Stone's lists of the greatest artists in history. Time magazine named them among the 20th century's 100 most important people.",
//                        wikipediaUrl = "https://en.wikipedia.org/wiki/The_Beatles",
//                    ),
//                    membersAndGroups =
//                    MembersAndGroups(
//                        previousMembersOfGroup = listOf(
//                            RelationListItemModel(
//                                id = "49a51491-650e-44b3-8085-2f07ac2986dd_11",
//                                linkedEntityId = "49a51491-650e-44b3-8085-2f07ac2986dd",
//                                label = "member of band",
//                                name = "Stuart Sutcliffe",
//                                disambiguation = null,
//                                attributes = "bass guitar",
//                                additionalInfo = "(1960-01 to 1961)",
//                                linkedEntity = MusicBrainzEntity.ARTIST,
//                                visited = false,
//                                isForwardDirection = false,
//                                lifeSpan = LifeSpanUiModel(begin = "1960-01", end = "1961", ended = true),
//                            ),
//                            RelationListItemModel(
//                                id = "f7d30d7d-9976-4d31-9907-19f3c30a206d_12",
//                                linkedEntityId = "f7d30d7d-9976-4d31-9907-19f3c30a206d",
//                                label = "member of band",
//                                name = "Tommy Moore",
//                                disambiguation = "English drummer, early 60s",
//                                attributes = "drums (drum set)",
//                                additionalInfo = "(1960-05 to 1960-06)",
//                                linkedEntity = MusicBrainzEntity.ARTIST,
//                                visited = false,
//                                isForwardDirection = false,
//                                lifeSpan = LifeSpanUiModel(begin = "1960-05", end = "1960-06", ended = true),
//                            ),
//                            RelationListItemModel(
//                                id = "6a0e143b-61bb-414f-99c8-96681be286a1_13",
//                                linkedEntityId = "6a0e143b-61bb-414f-99c8-96681be286a1",
//                                label = "member of band",
//                                name = "Norman Chapman",
//                                disambiguation = "English drummer, early 60s",
//                                attributes = "drums (drum set)",
//                                additionalInfo = "(1960-06)",
//                                linkedEntity = MusicBrainzEntity.ARTIST,
//                                visited = false,
//                                isForwardDirection = false,
//                                lifeSpan = LifeSpanUiModel(begin = "1960-06", end = "1960-06", ended = true),
//                            ),
//                            RelationListItemModel(
//                                id = "0d4ab0f9-bbda-4ab1-ae2c-f772ffcfbea9_14",
//                                linkedEntityId = "0d4ab0f9-bbda-4ab1-ae2c-f772ffcfbea9",
//                                label = "member of band",
//                                name = "Pete Best",
//                                disambiguation = "original drummer in The Beatles",
//                                attributes = "drums (drum set)",
//                                additionalInfo = "(1960-08-12 to 1962-08-16)",
//                                linkedEntity = MusicBrainzEntity.ARTIST,
//                                visited = false,
//                                isForwardDirection = false,
//                                lifeSpan = LifeSpanUiModel(begin = "1960-08-12", end = "1962-08-16", ended = true),
//                            ),
//                            RelationListItemModel(
//                                id = "2629082c-19b4-42ae-b2e1-d6025ead67a0_15",
//                                linkedEntityId = "2629082c-19b4-42ae-b2e1-d6025ead67a0",
//                                label = "member of band",
//                                name = "Chas Newby",
//                                disambiguation = "bassist in The Quarrymen",
//                                attributes = "bass guitar",
//                                additionalInfo = "(1960-12 to 1961-01)",
//                                linkedEntity = MusicBrainzEntity.ARTIST,
//                                visited = false,
//                                isForwardDirection = false,
//                                lifeSpan = LifeSpanUiModel(begin = "1960-12", end = "1961-01", ended = true),
//                            ),
//                            RelationListItemModel(
//                                id = "4d5447d7-c61c-4120-ba1b-d7f471d385b9_16",
//                                linkedEntityId = "4d5447d7-c61c-4120-ba1b-d7f471d385b9",
//                                label = "member of band",
//                                name = "John Lennon",
//                                disambiguation = "The Beatles",
//                                attributes = "guitar, lead vocals, original",
//                                additionalInfo = "(1960 to 1969-09)",
//                                linkedEntity = MusicBrainzEntity.ARTIST,
//                                visited = false,
//                                isForwardDirection = false,
//                                lifeSpan = LifeSpanUiModel(begin = "1960", end = "1969-09", ended = true),
//                            ),
//                            RelationListItemModel(
//                                id = "42a8f507-8412-4611-854f-926571049fa0_17",
//                                linkedEntityId = "42a8f507-8412-4611-854f-926571049fa0",
//                                label = "member of band",
//                                name = "George Harrison",
//                                disambiguation = "The Beatles",
//                                attributes = "guitar, lead vocals, original",
//                                additionalInfo = "(1960 to 1970-04-10)",
//                                linkedEntity = MusicBrainzEntity.ARTIST,
//                                visited = false,
//                                isForwardDirection = false,
//                                lifeSpan = LifeSpanUiModel(begin = "1960", end = "1970-04-10", ended = true),
//                            ),
//                            RelationListItemModel(
//                                id = "ba550d0e-adac-4864-b88b-407cab5e76af_18",
//                                linkedEntityId = "ba550d0e-adac-4864-b88b-407cab5e76af",
//                                label = "member of band",
//                                name = "Paul McCartney",
//                                disambiguation = "The Beatles",
//                                attributes = "bass guitar, lead vocals, original",
//                                additionalInfo = "(1960 to 1970-04-10)",
//                                linkedEntity = MusicBrainzEntity.ARTIST,
//                                visited = false,
//                                isForwardDirection = false,
//                                lifeSpan = LifeSpanUiModel(begin = "1960", end = "1970-04-10", ended = true),
//                            ),
//                            RelationListItemModel(
//                                id = "300c4c73-33ac-4255-9d57-4e32627f5e13_19",
//                                linkedEntityId = "300c4c73-33ac-4255-9d57-4e32627f5e13",
//                                label = "member of band",
//                                name = "Ringo Starr",
//                                disambiguation = "The Beatles",
//                                attributes = "drums (drum set)",
//                                additionalInfo = "(1962-08 to 1970-04-10)",
//                                linkedEntity = MusicBrainzEntity.ARTIST,
//                                visited = false,
//                                isForwardDirection = false,
//                                lifeSpan = LifeSpanUiModel(begin = "1962-08", end = "1970-04-10", ended = true),
//                            ),
//                        )
//                    )
//                ),
//            )
//        }
//    }
//}
//
//@PreviewLightDark
//@Composable
//internal fun PreviewArtistDetailsUiWithWikipediaUrlButNoExtract() {
//    PreviewTheme {
//        Surface {
//            ArtistDetailsUi(
//                artist = ArtistDetailsModel(
//                    id = "89ad4ac3-39f7-470e-963a-56509c546377",
//                    name = "Various Artists",
//                    type = "Other",
//                    sortName = "Various Artists",
//                    imageUrl = "https://i.scdn.co/image/ab6761610000e5ebe9348cc01ff5d55971b22433",
//                    wikipediaExtract = WikipediaExtract(
//                        wikipediaUrl = "https://en.wikipedia.org/wiki/Various_artists",
//                    ),
//                ),
//            )
//        }
//    }
//}
// endregion
