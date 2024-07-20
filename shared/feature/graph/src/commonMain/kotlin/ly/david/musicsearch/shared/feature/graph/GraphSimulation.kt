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
import io.data2viz.math.pct
import io.data2viz.math.rad
import io.data2viz.viz.CircleNode
import io.data2viz.viz.LineNode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import ly.david.musicsearch.core.models.network.MusicBrainzEntity
import ly.david.musicsearch.shared.feature.graph.viz.line
import kotlin.math.atan
import kotlin.random.Random

internal data class Entity(
    val entity: MusicBrainzEntity,
    val radius: Double,
)

data class WindSimulationUiState(
    val links: List<LineNode> = listOf(),
    val winds: List<CircleNode> = listOf(),
)

class GraphSimulation {

    private val _uiState = MutableStateFlow(WindSimulationUiState())
    val uiState: StateFlow<WindSimulationUiState>
        get() = _uiState

    // creating the objects, only the top line is "fixed"
    private val entities = Array(1000) {
        Entity(
            entity = MusicBrainzEntity.entries.random(),
            radius = Random.nextDouble(
                5.0,
                15.0,
            ),
        )
    }.toList()

    private var forceLinks: ForceLink<Entity>? = null
    private val simulation: ForceSimulation<Entity> by lazy {
        forceSimulation {
            domainObjects = this@GraphSimulation.entities

            // If we set a decay, the simulation may stop before there are no overlapping nodes
//            intensityDecay = 0.pct

            forceCenter {
                center = Point(
                    200.0,
                    500.0,
                )
            }

            forceLinks = forceLink {
                linkGet = {
                    val links = mutableListOf<Link<Entity>>()
//                    val currentCol = index % singleCurtainWidth
//                    val wholeCol = index % curtainsWidth
//                    val row = index / curtainsWidth
//
//                    // only "link" the stitches, not the 3 nodes used for simulating the wind
//                    if (index < totalStitches) {
//                        // check if we had the right-next node
//                        if (currentCol != (singleCurtainWidth - 1) && wholeCol < curtainsWidth - 1) {
//                            links += Link(
//                                this,
//                                nodes[index + 1],
//                                spacingBetweenStitch,
//                            )
//                        }
//                        // check if we had the bottom-next node
//                        if (row < curtainsLength - 1) {
//                            links += Link(
//                                this,
//                                nodes[index + curtainsWidth],
//                                spacingBetweenStitch,
//                            )
//                        }
//                    }
                    // return the list of links
                    links
                }
//                iterations = linkForceIterations
            }

            forceCollision {
                radiusGet = { domain.radius + 1 }
                iterations = 1
            }
        }
    }

    fun run() {
        if (!simulation.isRunning()) return

        _uiState.update {
            val mutableLinks = it.links.toMutableList()

            // show the new coordinates of each links to visualize the wind effect
            forceLinks?.links?.forEachIndexed { index, link ->
                mutableLinks[index] = line {
                    x1 = link.source.x
                    x2 = link.target.x
                    y1 = link.source.y
                    y2 = link.target.y

                    val angle = (atan((y1 - y2) / (x1 - x2)) * 2).rad
                    strokeColor = Colors.hsl(
                        angle,
                        100.pct,
                        40.pct,
                    )
                }
            }

            val windNodes = simulation.nodes.map { node: ForceNode<Entity> ->
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

            it.copy(
                links = mutableLinks,
                windNodes,
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

    return baseColor.opacify(strength = 0.75)
}

private fun <D> ForceSimulation<D>.isRunning(): Boolean {
    return intensity > intensityMin
}
