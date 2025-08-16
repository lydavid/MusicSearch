package ly.david.musicsearch.shared.feature.graph

import app.cash.turbine.test
import kotlinx.coroutines.test.runTest
import ly.david.musicsearch.shared.domain.artist.CollaboratingArtistAndEntity
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Before
import org.junit.Test

class ArtistCollaborationGraphSimulationTest {

    private lateinit var simulation: ArtistCollaborationGraphSimulation

    @Before
    fun setUp() {
        simulation = ArtistCollaborationGraphSimulation()
    }

    @Test
    fun validateDataSet() {
        assertEquals(21, collaborations.size)
        assertEquals(9, collaborations.distinctBy { it.artistId }.size)
        assertEquals(17, collaborations.distinctBy { it.entityId }.size)
    }

    @Test
    fun numberOfNodesAndLinksAreCorrect() = runTest {
        simulation.initialize(collaborations)

        simulation.uiState.test {
            val initialState = awaitItem()

            assertEquals(0, initialState.nodes.size)
            assertEquals(0, initialState.edges.size)

            simulation.step()
            val state = awaitItem()

            assertEquals(26, state.nodes.size)
            assertEquals(9, state.nodes.count { it.entity == MusicBrainzEntity.ARTIST })
            assertEquals(17, state.nodes.count { it.entity == MusicBrainzEntity.RECORDING })

            assertEquals(42, state.edges.size)

            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun stepShouldUpdateNodePositions() = runTest {
        simulation.initialize(collaborations)

        simulation.uiState.test {
            val initialState = awaitItem()

            assertEquals(0, initialState.nodes.size)
            assertEquals(0, initialState.edges.size)

            simulation.step()
            val state1 = awaitItem()

            simulation.step()
            val state2 = awaitItem()

            assertNotEquals(state1.nodes, state2.nodes)
            assertNotEquals(state1.edges, state2.edges)

            cancelAndConsumeRemainingEvents()
        }
    }
}

private val collaborations = listOf(
    CollaboratingArtistAndEntity(
        artistId = "6114677c-fa8f-4d87-960a-3f1169aaef89",
        artistName = "TK from 凛として時雨",
        entityId = "494ac693-6d6a-4411-ac41-d6e7c32c6e34",
        entityName = "melt",
        entity = MusicBrainzEntity.RECORDING,
    ),
    CollaboratingArtistAndEntity(
        artistId = "6114677c-fa8f-4d87-960a-3f1169aaef89",
        artistName = "TK from 凛として時雨",
        entityId = "51999312-1044-4f91-92b0-ed23974929ca",
        entityName = "melt",
        entity = MusicBrainzEntity.RECORDING,
    ),
    CollaboratingArtistAndEntity(
        artistId = "6114677c-fa8f-4d87-960a-3f1169aaef89",
        artistName = "TK from 凛として時雨",
        entityId = "e41f1853-0f2a-4ec8-a815-7af70087bb85",
        entityName = "melt",
        entity = MusicBrainzEntity.RECORDING,
    ),
    CollaboratingArtistAndEntity(
        artistId = "dfc6a151-3792-4695-8fda-f64723eaa788",
        artistName = "ヨルシカ",
        entityId = "58576a03-f302-4f68-9a97-113a6afc753f",
        entityName = "星めぐる詩",
        entity = MusicBrainzEntity.RECORDING,
    ),
    CollaboratingArtistAndEntity(
        artistId = "80b3cb83-b7a3-4f79-ad42-8325cefb3626",
        artistName = "キタニタツヤ",
        entityId = "9a70f454-1da3-4570-b592-33e5f8713d28",
        entityName = "ナイトルーティーン",
        entity = MusicBrainzEntity.RECORDING,
    ),
    CollaboratingArtistAndEntity(
        artistId = "dfc6a151-3792-4695-8fda-f64723eaa788",
        artistName = "ヨルシカ",
        entityId = "9a70f454-1da3-4570-b592-33e5f8713d28",
        entityName = "ナイトルーティーン",
        entity = MusicBrainzEntity.RECORDING,
    ),
    CollaboratingArtistAndEntity(
        artistId = "80b3cb83-b7a3-4f79-ad42-8325cefb3626",
        artistName = "キタニタツヤ",
        entityId = "d4a15774-05b7-4f58-97b1-2bdf4a10d8d5",
        entityName = "ナイトルーティーン",
        entity = MusicBrainzEntity.RECORDING,
    ),
    CollaboratingArtistAndEntity(
        artistId = "dfc6a151-3792-4695-8fda-f64723eaa788",
        artistName = "ヨルシカ",
        entityId = "d4a15774-05b7-4f58-97b1-2bdf4a10d8d5",
        entityName = "ナイトルーティーン",
        entity = MusicBrainzEntity.RECORDING,
    ),
    CollaboratingArtistAndEntity(
        artistId = "b3933fd6-fba9-44b1-992a-1e8128324ca1",
        artistName = "下村陽子",
        entityId = "028c2091-8371-42e3-acf3-82164552691e",
        entityName = "#時をめくる指 (instrumental)",
        entity = MusicBrainzEntity.RECORDING,
    ),
    CollaboratingArtistAndEntity(
        artistId = "dfc6a151-3792-4695-8fda-f64723eaa788",
        artistName = "ヨルシカ",
        entityId = "331c0c61-b16f-406b-b485-15f50225790b",
        entityName = "サンサーラ",
        entity = MusicBrainzEntity.RECORDING,
    ),
    CollaboratingArtistAndEntity(
        artistId = "cb191900-8ad8-46b9-b021-a093ee2b2f9b",
        artistName = "SawanoHiroyuki[nZk]",
        entityId = "494377fd-2367-46a1-acf3-7661e8dee00d",
        entityName = "B∀LK",
        entity = MusicBrainzEntity.RECORDING,
    ),
    CollaboratingArtistAndEntity(
        artistId = "16a3c7da-2951-4020-b1e3-4a10cecb7141",
        artistName = "Lanndo",
        entityId = "4c79ceea-5177-4fb7-b7c3-0a1e6785ee23",
        entityName = "宇宙の季節",
        entity = MusicBrainzEntity.RECORDING,
    ),
    CollaboratingArtistAndEntity(
        artistId = "66bdd1c9-d1c5-40b7-a487-5061fffbd87d",
        artistName = "Eve",
        entityId = "4c79ceea-5177-4fb7-b7c3-0a1e6785ee23",
        entityName = "宇宙の季節",
        entity = MusicBrainzEntity.RECORDING,
    ),
    CollaboratingArtistAndEntity(
        artistId = "b3933fd6-fba9-44b1-992a-1e8128324ca1",
        artistName = "下村陽子",
        entityId = "546560d5-dc85-45aa-9627-9a482c09471e",
        entityName = "#時をめくる指",
        entity = MusicBrainzEntity.RECORDING,
    ),
    CollaboratingArtistAndEntity(
        artistId = "16a3c7da-2951-4020-b1e3-4a10cecb7141",
        artistName = "Lanndo",
        entityId = "a5c71240-a1d4-4884-9663-2aba6d9d7165",
        entityName = "宇宙の季節",
        entity = MusicBrainzEntity.RECORDING,
    ),
    CollaboratingArtistAndEntity(
        artistId = "66bdd1c9-d1c5-40b7-a487-5061fffbd87d",
        artistName = "Eve",
        entityId = "a5c71240-a1d4-4884-9663-2aba6d9d7165",
        entityName = "宇宙の季節",
        entity = MusicBrainzEntity.RECORDING,
    ),
    CollaboratingArtistAndEntity(
        artistId = "cb3e092f-5eae-4bc1-9ffe-bbdc13dcd823",
        artistName = "MONDO GROSSO",
        entityId = "b2ede43d-ed7d-4d40-99bb-dd8c4fed9ded",
        entityName = "最後の心臓",
        entity = MusicBrainzEntity.RECORDING,
    ),
    CollaboratingArtistAndEntity(
        artistId = "26b8ea1c-fb9e-4378-84a0-d0eace285f7e",
        artistName = "SennaRin",
        entityId = "b80166fd-f19e-4bdf-82b9-22929f6775ca",
        entityName = "vous",
        entity = MusicBrainzEntity.RECORDING,
    ),
    CollaboratingArtistAndEntity(
        artistId = "dfc6a151-3792-4695-8fda-f64723eaa788",
        artistName = "ヨルシカ",
        entityId = "bd3cbe82-eb42-4e46-958a-522a7b22f750",
        entityName = "少年時代 (あの夏のルカver.)",
        entity = MusicBrainzEntity.RECORDING,
    ),
    CollaboratingArtistAndEntity(
        artistId = "66bdd1c9-d1c5-40b7-a487-5061fffbd87d",
        artistName = "Eve",
        entityId = "c01ee1fd-5229-47d8-b40b-0328505a2dff",
        entityName = "平行線",
        entity = MusicBrainzEntity.RECORDING,
    ),
    CollaboratingArtistAndEntity(
        artistId = "dfc6a151-3792-4695-8fda-f64723eaa788",
        artistName = "ヨルシカ",
        entityId = "f5a3b18a-e64d-44d5-b530-288ea09c849b",
        entityName = "Travelers",
        entity = MusicBrainzEntity.RECORDING,
    ),
)
