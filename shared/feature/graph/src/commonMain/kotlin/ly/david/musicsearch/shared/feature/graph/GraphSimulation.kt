package ly.david.musicsearch.shared.feature.graph

import androidx.compose.runtime.mutableStateListOf
import io.data2viz.color.Colors
import io.data2viz.force.ForceLink
import io.data2viz.force.ForceSimulation
import io.data2viz.force.Link
import io.data2viz.force.forceSimulation
import io.data2viz.geom.CircleGeom
import io.data2viz.geom.Point
import io.data2viz.geom.Vector
import io.data2viz.geom.point
import io.data2viz.math.deg
import io.data2viz.math.pct
import io.data2viz.math.rad
import io.data2viz.random.RandomDistribution
import io.data2viz.viz.CircleNode
import io.data2viz.viz.LineNode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import ly.david.musicsearch.shared.feature.graph.viz.line
import kotlin.math.atan

// credit to Pierre Mariac. based off of https://play.data2viz.io/sketches/PnLoKL/edit/

private val frictionRate = 4.pct // friction rate
private val defaultIntensity = 70.pct // default fixed intensity (no intensity decay)

private const val linkForceIterations = 7 // the more iterations = the more "rigid" the curtains

private val collisionForceStrength = 10.pct // less strength = wind pass through the "curtains"

private const val windRadius = 80.0 // size of the "wind" circles for collision
private const val windSpeed = 4 // speed of the "wind"
private val windAngle = 0.deg // angle of the "wind"

private val gravityValue = 0.16.pct // higher values = heavier "curtains"

private const val singleCurtainWidth = 1 // width of a curtain
private const val curtainsNumber = 30 // # of curtains drawn
private const val curtainsLength = 32 // length of a curtain
private const val spacingBetweenStitch = 3.0 // size between nodes
private const val curtainsWidth = curtainsNumber * singleCurtainWidth
private const val totalStitches = curtainsWidth * curtainsLength

const val vizWidth = 900.0
const val vizHeight = 600.0

// our domain object, storing a default starting position and if it is "fixed" or not
internal data class Stitch(
    val position: Point,
    val fixed: Boolean = false,
)

data class WindSimulationUiState(
    val links: List<LineNode> = listOf(),
    val winds: List<CircleNode> = listOf(),
)

class GraphSimulation {

    private val _uiState = MutableStateFlow(WindSimulationUiState())
    val uiState: StateFlow<WindSimulationUiState>
        get() = _uiState

    private val movement = Vector(
        windSpeed * windAngle.cos,
        windSpeed * windAngle.sin,
    )
    private val randPos = RandomDistribution.uniform(
        320.0,
        600.0,
    )

    // creating the objects, only the top line is "fixed"
    private val stitches = (0 until totalStitches).map {
        val col = it % curtainsWidth
        val row = it / curtainsWidth
        Stitch(
            position = point(
                x = 64.0 + (col * spacingBetweenStitch),
                y = 140.0 + (row * spacingBetweenStitch) - col,
            ),
            fixed = it < curtainsWidth,
        )
    }.toMutableList().apply {

        // adding 3 more nodes to the simulation, these nodes are used to simulate the wind
        // these nodes will have a very different behavior
        add(
            Stitch(
                point(
                    0,
                    350,
                ),
                false,
            ),
        )
        add(
            Stitch(
                point(
                    -450,
                    600,
                ),
                false,
            ),
        )
        add(
            Stitch(
                point(
                    -200,
                    400,
                ),
                false,
            ),
        )
    }

    private var forceLinks: ForceLink<Stitch>? = null
    private val simulation: ForceSimulation<Stitch> =
        forceSimulation {
            friction = frictionRate
            intensity = defaultIntensity
            intensityDecay = 0.pct

            // if the Stitch is "fixed", we use its current position has a fixed one (node won't move)
            initForceNode = {
                position = domain.position
                fixedX = if (domain.fixed) domain.position.x else null
                fixedY = if (domain.fixed) domain.position.y else null
            }

            // the force that creates links between the nodes
            // each node is linked to the next one on the right and next one below
            forceLinks = forceLink {
                linkGet = {
                    val links = mutableListOf<Link<Stitch>>()
                    val currentCol = index % singleCurtainWidth
                    val wholeCol = index % curtainsWidth
                    val row = index / curtainsWidth

                    // only "link" the stitches, not the 3 nodes used for simulating the wind
                    if (index < totalStitches) {
                        // check if we had the right-next node
                        if (currentCol != (singleCurtainWidth - 1) && wholeCol < curtainsWidth - 1) {
                            links += Link(
                                this,
                                nodes[index + 1],
                                spacingBetweenStitch,
                            )
                        }
                        // check if we had the bottom-next node
                        if (row < curtainsLength - 1) {
                            links += Link(
                                this,
                                nodes[index + curtainsWidth],
                                spacingBetweenStitch,
                            )
                        }
                    }
                    // return the list of links
                    links
                }
                iterations = linkForceIterations
            }

            // create a collision force, only the 3 "wind" nodes have a radius
            forceCollision {
                radiusGet = { if (index < totalStitches) .0 else windRadius }
                strength = collisionForceStrength
                iterations = 1
            }

            // create a "gravity" force, only applies to the "stiches" not the "wind"
            forceY {
                yGet = { vizWidth }
                strengthGet = { if (index < totalStitches) gravityValue else 0.pct }
            }

            domainObjects = stitches
        }

    fun run() {
        _uiState.update {
            val stitchLinks = mutableStateListOf<LineNode>()
            repeat(forceLinks?.links?.size ?: 0) {
                stitchLinks.add(
                    line {
                        strokeColor = Colors.Web.black
                    },
                )
            }
            val windNodes = simulation.nodes.slice(totalStitches..totalStitches + 2).map {
                CircleNode(
                    CircleGeom(
                        x = it.x,
                        y = it.y,
                        radius = windRadius,
                    ),
                ).apply {
                    fill = Colors.rgb(
                        red = 123,
                        green = 123,
                        blue = 123,
                        alpha = 50.pct,
                    )
                }
            }
            WindSimulationUiState(
                links = stitchLinks,
                winds = windNodes,
            )
        }

        _uiState.update {
            val mutableLinks = it.links.toMutableList()

            // force move the "wind" particles
            (totalStitches..totalStitches + 2).forEach { windIndex ->
                val windNode = simulation.nodes[windIndex]
                windNode.position += movement
                if (windNode.x > vizWidth) {
                    windNode.x = -50.0
                    windNode.y = randPos()
                }
            }

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

            val windNodes = simulation.nodes.slice(totalStitches..totalStitches + 2).map {
                CircleNode(
                    CircleGeom(
                        x = it.x,
                        y = it.y,
                        radius = windRadius,
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
