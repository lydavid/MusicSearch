package ly.david.musicsearch.shared.feature.graph

import app.cash.turbine.test
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import ly.david.musicsearch.core.models.artist.CollaboratingArtistAndRecording
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ArtistCollaborationGraphSimulationTest {

    private lateinit var simulation: ArtistCollaborationGraphSimulation

    @BeforeEach
    fun setUp() {
        simulation = ArtistCollaborationGraphSimulation()
    }

    @Test
    fun a() = runTest {
        flowOf("one").test {
            Assertions.assertEquals(
                "one",
                awaitItem()
            )
            awaitComplete()
        }
//        val collaborations = listOf(
//            CollaboratingArtistAndRecording(
//                "artist1",
//                "Artist 1",
//                "recording1",
//                "Recording 1"
//            ),
//            CollaboratingArtistAndRecording(
//                "artist1",
//                "Artist 1",
//                "recording2",
//                "Recording 2"
//            ),
//            CollaboratingArtistAndRecording(
//                "artist2",
//                "Artist 2",
//                "recording1",
//                "Recording 1"
//            )
//        )
//
//        simulation.initialize(collaborations)
//
//        simulation.uiState.test {
//            val initialState = awaitItem()
//            assertEquals(5, initialState.nodes.size) // 2 artists + 3 recordings
//            assertEquals(3, initialState.links.size) // 3 artist-recording links
//            cancelAndConsumeRemainingEvents()
//        }
    }

    @Test
    fun nodeRadiiShouldBeCalculatedCorrectly() = runTest {
        val collaborations = listOf(
            CollaboratingArtistAndRecording(
                "artist1",
                "Artist 1",
                "recording1",
                "Recording 1"
            ),
            CollaboratingArtistAndRecording(
                "artist1",
                "Artist 1",
                "recording2",
                "Recording 2"
            ),
            CollaboratingArtistAndRecording(
                "artist2",
                "Artist 2",
                "recording1",
                "Recording 1"
            )
        )

        simulation.initialize(collaborations)

        simulation.uiState.test {
            val initialState = awaitItem()
            val artist1Node = initialState.nodes.find { it.id == "artist1" }
            val artist2Node = initialState.nodes.find { it.id == "artist2" }
            val recording1Node = initialState.nodes.find { it.id == "recording1" }

            Assertions.assertNotNull(artist1Node)
            Assertions.assertNotNull(artist2Node)
            Assertions.assertNotNull(recording1Node)

            // artist1 appears twice, so its radius should be larger
            Assertions.assertTrue(artist1Node!!.radius > artist2Node!!.radius)
            // recording1 appears twice, so its radius should be larger than the MIN_RADIUS
            Assertions.assertTrue(recording1Node!!.radius > 10.0)

            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun stepShouldUpdateNodePositions() = runTest {
        val collaborations = listOf(
            CollaboratingArtistAndRecording(
                "artist1",
                "Artist 1",
                "recording1",
                "Recording 1"
            )
        )

        simulation.initialize(collaborations)

        simulation.uiState.test {
            val initialState = awaitItem()
            val initialPositions = initialState.nodes.map {
                it.id to Pair(
                    it.x,
                    it.y
                )
            }.toMap()

            simulation.step()

            val updatedState = awaitItem()
            val updatedPositions = updatedState.nodes.map {
                it.id to Pair(
                    it.x,
                    it.y
                )
            }.toMap()

            // Check that at least one node has moved
            Assertions.assertTrue(initialPositions.any { (id, pos) ->
                pos != updatedPositions[id]
            })

            cancelAndConsumeRemainingEvents()
        }
    }

//    @Test
//    fun `simulation should eventually stop running`() {
//        val collaborations = listOf(
//            CollaboratingArtistAndRecording("artist1", "Artist 1", "recording1", "Recording 1"),
//            CollaboratingArtistAndRecording("artist2", "Artist 2", "recording2", "Recording 2")
//        )
//
//        simulation.initialize(collaborations)
//
//        var steps = 0
//        val maxSteps = 1000 // Arbitrary large number to prevent infinite loop
//
//        while (steps < maxSteps) {
//            simulation.step()
//            steps++
//
//            // Use reflection to access the private simulation field and check if it's still running
//            val simulationField = ArtistCollaborationGraphSimulation::class.java.getDeclaredField("simulation")
//            simulationField.isAccessible = true
//            val forceSimulation = simulationField.get(simulation) as ForceSimulation<*>
//
//            if (!forceSimulation.isRunning()) {
//                break
//            }
//        }
//
//        assertTrue(steps < maxSteps, "Simulation did not stop within the expected number of steps")
//    }
}