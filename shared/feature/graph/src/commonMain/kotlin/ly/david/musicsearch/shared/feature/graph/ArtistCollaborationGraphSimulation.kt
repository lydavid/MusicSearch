package ly.david.musicsearch.shared.feature.graph

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import ly.david.musicsearch.shared.domain.artist.CollaboratingArtistAndRecording
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
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
) {
    override fun toString(): String {
        return "GraphNode(id=\"${id}\",name=\"${name}\",entity=MusicBrainzEntity.$entity," +
            "radius=$radius,x=$x,y=$y)"
    }
}

data class GraphEdge(
    val x0: Double = 0.0,
    val y0: Double = 0.0,
    val x1: Double = 0.0,
    val y1: Double = 0.0,
)

data class GraphSimulationUiState(
    val edges: ImmutableList<GraphEdge> = persistentListOf(),
    val nodes: ImmutableList<GraphNode> = persistentListOf(),
)

private data class ArtistRecording(
    val artistId: String,
    val recordingId: String,
)

// https://developer.android.com/develop/ui/compose/accessibility/key-steps#minimum-target-sizes
private const val MIN_RADIUS = 24.0 - 1.0

private const val LINK_DISTANCE = 250.0

private const val MANY_BODY_STRENGTH = -60.0

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
        val artistRecordingLinks: List<ArtistRecording> = collaboratingArtistAndRecordings
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

            forceNBody {
                strengthGet = {
                    MANY_BODY_STRENGTH
                }
            }

            forceLinks = forceLink {
                linkGet = {
                    when (this.domain.entity) {
                        MusicBrainzEntity.RECORDING -> artistRecordingLinks.filter { it.recordingId == this.domain.id }
                        MusicBrainzEntity.ARTIST -> artistRecordingLinks.filter { it.artistId == this.domain.id }
                        else -> emptyList()
                    }.mapNotNull { artistRecording ->
                        val sourceId = if (this.domain.entity == MusicBrainzEntity.RECORDING) {
                            artistRecording.artistId
                        } else {
                            this.domain.id
                        }
                        val targetId = if (this.domain.entity == MusicBrainzEntity.RECORDING) {
                            this.domain.id
                        } else {
                            artistRecording.recordingId
                        }

                        val source = nodes.find { it.domain.id == sourceId }
                        val target = nodes.find { it.domain.id == targetId }

                        if (source != null && target != null) {
                            Link(
                                source = source,
                                target = target,
                                distance = LINK_DISTANCE,
                            )
                        } else {
                            null
                        }
                    }
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

    fun step(): Boolean {
        if (!simulation.isRunning()) return false
        simulation.step(1)

        _uiState.update { uiState ->
            val edges = forceLinks?.links?.map { link ->
                GraphEdge(
                    x0 = link.source.x,
                    x1 = link.target.x,
                    y0 = link.source.y,
                    y1 = link.target.y,
                )
            }.orEmpty()

            val nodes = simulation.nodes.map { node: ForceNode<GraphNode> ->
                node.domain.copy(
                    x = node.x,
                    y = node.y,
                )
            }

            uiState.copy(
                edges = edges.toPersistentList(),
                nodes = nodes.toPersistentList(),
            )
        }
        return true
    }
}

private fun <D> ForceSimulation<D>.isRunning(): Boolean {
    return intensity > intensityMin
}
