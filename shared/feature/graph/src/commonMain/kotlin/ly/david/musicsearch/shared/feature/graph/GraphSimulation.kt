package ly.david.musicsearch.shared.feature.graph

import io.data2viz.color.Color
import io.data2viz.color.Colors
import io.data2viz.color.col
import io.data2viz.force.ForceLink
import io.data2viz.force.ForceNode
import io.data2viz.force.ForceSimulation
import io.data2viz.force.Link
import io.data2viz.force.forceSimulation
import io.data2viz.geom.CircleGeom
import io.data2viz.geom.Point
import io.data2viz.viz.CircleNode
import io.data2viz.viz.LineNode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import ly.david.musicsearch.core.models.network.MusicBrainzEntity
import ly.david.musicsearch.shared.feature.graph.viz.line
import kotlin.random.Random

internal data class Entity(
    val entity: MusicBrainzEntity,
    val radius: Double,
)

data class GraphSimulationUiState(
    val links: List<LineNode> = listOf(),
    val nodes: List<CircleNode> = listOf(),
)

class GraphSimulation {

    private val _uiState = MutableStateFlow(GraphSimulationUiState())
    val uiState: StateFlow<GraphSimulationUiState>
        get() = _uiState

    private val entities = Array(1000) {
        Entity(
            entity = MusicBrainzEntity.entries.random(),
            radius = 10.0,
//            radius = Random.nextDouble(
//                5.0,
//                15.0,
//            ),
        )
    }.toList()

    private var forceLinks: ForceLink<Entity>? = null
    private lateinit var simulation: ForceSimulation<Entity>

    fun initialize() {
        simulation = forceSimulation {
            domainObjects = this@GraphSimulation.entities

            // If we set a decay, the simulation may stop before there are no overlapping nodes
//            intensityDecay = 0.pct

            // TODO: should be centered based on screen's dimensions
            //  either pass it from JC
            //  or move this to JC land
            forceCenter {
                center = Point(
                    200.0,
                    500.0,
                )
            }

            forceLinks = forceLink {
                linkGet = {
                    val links = mutableListOf<Link<Entity>>()

                    if (nodes.isNotEmpty()) {
                        links += Link(
                            source = this,
                            target = nodes[0],
                            distance = Random.nextDouble(30.0, 200.0),
                        )
                    }
                    links
                }
            }

            forceCollision {
                radiusGet = { domain.radius + 1 }
                iterations = 1
            }
        }
    }

    fun step() {
        if (!simulation.isRunning()) return

        _uiState.update { uiState ->
            val links = forceLinks?.links?.map { link ->
                line {
                    x1 = link.source.x
                    x2 = link.target.x
                    y1 = link.source.y
                    y2 = link.target.y

                    strokeColor = Colors.Web.grey
                }
            }.orEmpty()

            val nodes = simulation.nodes.map { node: ForceNode<Entity> ->
                CircleNode(
                    CircleGeom(
                        x = node.x,
                        y = node.y,
                        radius = node.domain.radius,
                    ),
                ).apply {
                    fill = node.domain.entity.getNodeColor()
                }
            }

            uiState.copy(
                links = links,
                nodes = nodes,
            )
        }
    }
}

private fun MusicBrainzEntity.getNodeColor(): Color {
    val baseColor = when (this) {
        MusicBrainzEntity.AREA -> "#4CAF50".col
        MusicBrainzEntity.ARTIST -> "#FF5722".col
        MusicBrainzEntity.COLLECTION -> "#9C27B0".col
        MusicBrainzEntity.EVENT -> "#FFC107".col
        MusicBrainzEntity.GENRE -> "#2196F3".col
        MusicBrainzEntity.INSTRUMENT -> "#795548".col
        MusicBrainzEntity.LABEL -> "#F44336".col
        MusicBrainzEntity.PLACE -> "#009688".col
        MusicBrainzEntity.RECORDING -> "#E91E63".col
        MusicBrainzEntity.RELEASE -> "#3F51B5".col
        MusicBrainzEntity.RELEASE_GROUP -> "#8BC34A".col
        MusicBrainzEntity.SERIES -> "#FF9800".col
        MusicBrainzEntity.WORK -> "#607D8B".col
        MusicBrainzEntity.URL -> "#00BCD4".col
    }

    return baseColor//.opacify(strength = 0.75)
}

private fun <D> ForceSimulation<D>.isRunning(): Boolean {
    return intensity > intensityMin
}
