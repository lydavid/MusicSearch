package ly.david.musicsearch.shared.feature.graph

import io.data2viz.color.Colors
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
import ly.david.musicsearch.shared.feature.graph.viz.line
import kotlin.math.atan
import kotlin.random.Random

internal data class Node(
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
    private val nodes = Array(1000) {
        Node(
            radius = Random.nextDouble(5.0, 15.0)
        )
    }.toList()

    private var forceLinks: ForceLink<Node>? = null
    private val simulation: ForceSimulation<Node> by lazy {
        forceSimulation {
            // If we set a decay, the simulation may stop before there are no overlapping nodes
            intensityDecay = 0.pct

            forceCenter {
                center = Point(
                    200.0,
                    500.0,
                )
            }

            forceLinks = forceLink {
                linkGet = {
                    val links = mutableListOf<Link<Node>>()
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
//                strength = collisionForceStrength
                iterations = 1
            }

            domainObjects = this@GraphSimulation.nodes
        }
    }

    fun run() {
        _uiState.update {
            val mutableLinks = it.links.toMutableList()

            // force move the "wind" particles
//            (totalStitches..totalStitches + 2).forEach { windIndex ->
//                val windNode = simulation.nodes[windIndex]
//                windNode.position += movement
//                if (windNode.x > vizWidth) {
//                    windNode.x = -50.0
//                    windNode.y = randPos()
//                }
//            }

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

            val windNodes = simulation.nodes.map { node: ForceNode<Node> ->
                CircleNode(
                    CircleGeom(
                        x = node.x,
                        y = node.y,
                        radius = node.domain.radius,
                    ),
                ).apply {
                    fill = Colors.rgb(
                        red = 50,
                        green = 123,
                        blue = 50,
                        alpha = 50.pct,
                    )
                }
            }

            it.copy(
                links = mutableLinks,
                windNodes,
            )
        }
    }
}
