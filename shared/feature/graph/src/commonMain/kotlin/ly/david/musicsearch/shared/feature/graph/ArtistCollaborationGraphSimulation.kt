package ly.david.musicsearch.shared.feature.graph

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import ly.david.musicsearch.core.models.artist.CollaboratingArtistAndRecording
import ly.david.musicsearch.core.models.network.MusicBrainzEntity
import ly.david.musicsearch.shared.feature.graph.viz.core.geom.Point
import ly.david.musicsearch.shared.feature.graph.viz.force.ForceLink
import ly.david.musicsearch.shared.feature.graph.viz.force.ForceNode
import ly.david.musicsearch.shared.feature.graph.viz.force.ForceSimulation
import ly.david.musicsearch.shared.feature.graph.viz.force.Link
import ly.david.musicsearch.shared.feature.graph.viz.force.forceSimulation

data class GraphNode(
    val id: String,
    val name: String,
    val entity: MusicBrainzEntity,
    val radius: Double,
    val x: Double = 0.0,
    val y: Double = 0.0,
)

data class GraphLink(
    val x1: Double = 0.0,
    val y1: Double = 0.0,
    val x2: Double = 0.0,
    val y2: Double = 0.0,
)

data class GraphSimulationUiState(
    val links: List<GraphLink> = listOf(),
    val nodes: List<GraphNode> = listOf(),
)

private data class ArtistRecording(
    val artistId: String,
    val recordingId: String,
)

private const val MIN_RADIUS = 10.0
private const val LINK_DISTANCE = 250.0

// private const val MANY_BODY_STRENGTH = -30.0
private const val COLLISION_DISTANCE = 30.0

class ArtistCollaborationGraphSimulation {

    private val _uiState = MutableStateFlow(GraphSimulationUiState())
    val uiState: StateFlow<GraphSimulationUiState>
        get() = _uiState

    private var forceLinks: ForceLink<GraphNode>? = null
    private lateinit var simulation: ForceSimulation<GraphNode>

    fun initialize(
        collaboratingArtistAndRecordings: List<CollaboratingArtistAndRecording>,
    ) {
        val artistRecordingLinks = collaboratingArtistAndRecordings
            .map { ArtistRecording(artistId = it.artistId, recordingId = it.recordingId) }
            .distinct()

        simulation = forceSimulation {
            domainObjects = generateGraphNodes(collaboratingArtistAndRecordings)

            forceCenter {
                center = Point(
                    0.0,
                    0.0,
                )
            }

//            forceNBody {
//                strengthGet = {
//                    MANY_BODY_STRENGTH
//                }
//            }

            forceLinks = forceLink {
                linkGet = {
                    val node = this.domain
                    val artistLinks = if (node.entity == MusicBrainzEntity.RECORDING) {
                        artistRecordingLinks
                            .filter { it.recordingId == node.id }
                    } else {
                        listOf()
                    }
                    val recordingLinks = if (node.entity == MusicBrainzEntity.ARTIST) {
                        artistRecordingLinks
                            .filter { it.artistId == node.id }
                    } else {
                        listOf()
                    }

                    val links = (artistLinks + recordingLinks)
                        .mapNotNull { (artistId, recordingId) ->
                            val source = nodes.find { it.domain.id == artistId } ?: return@mapNotNull null
                            val target = nodes.find { it.domain.id == recordingId } ?: return@mapNotNull null
                            Link(
                                source = source,
                                target = target,
                                distance = LINK_DISTANCE,
                            )
                        }
                    links
                }
            }

            forceCollision {
                radiusGet = { domain.radius + COLLISION_DISTANCE }
                iterations = 1
            }
        }
    }

    private fun generateGraphNodes(collaborations: List<CollaboratingArtistAndRecording>): List<GraphNode> {
        val artistFrequency = collaborations.groupingBy { it.artistId }.eachCount()
        val recordingFrequency = collaborations.groupingBy { it.recordingId }.eachCount()

        val artistNodes = collaborations
            .map { it.artistId to it.artistName }
            .distinctBy { it.first }
            .map { (id, name) ->
                GraphNode(
                    id = id,
                    name = name,
                    entity = MusicBrainzEntity.ARTIST,
                    radius = calculateRadius(artistFrequency[id] ?: 1),
                )
            }

        val recordingNodes = collaborations
            .map { it.recordingId to it.recordingName }
            .distinctBy { it.first }
            .map { (id, name) ->
                GraphNode(
                    id = id,
                    name = name,
                    entity = MusicBrainzEntity.RECORDING,
                    radius = calculateRadius(recordingFrequency[id] ?: 1),
                )
            }
        return artistNodes + recordingNodes
    }

    private fun calculateRadius(frequency: Int): Double {
        return MIN_RADIUS + frequency
    }

    fun step() {
        if (!simulation.isRunning()) return
        simulation.step(1)

        _uiState.update { uiState ->
            val links = forceLinks?.links?.map { link ->
                GraphLink(
                    x1 = link.source.x,
                    x2 = link.target.x,
                    y1 = link.source.y,
                    y2 = link.target.y,
                )
            }.orEmpty()

            val nodes = simulation.nodes.map { node: ForceNode<GraphNode> ->
                node.domain.copy(
                    x = node.x,
                    y = node.y,
                )
            }

            uiState.copy(
                links = links,
                nodes = nodes,
            )
        }
    }
}

private fun <D> ForceSimulation<D>.isRunning(): Boolean {
    return intensity > intensityMin
}
