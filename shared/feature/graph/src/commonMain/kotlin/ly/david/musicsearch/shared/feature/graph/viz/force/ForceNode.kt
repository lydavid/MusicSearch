/*
 * Copyright (c) 2018-2021. data2viz sàrl.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package ly.david.musicsearch.shared.feature.graph.viz.force

import ly.david.musicsearch.shared.feature.graph.viz.core.geom.Point
import ly.david.musicsearch.shared.feature.graph.viz.core.geom.Vector

/**
 * The position ⟨x,y⟩ and velocity ⟨vx,vy⟩ may be subsequently modified by forces and by the simulation.
 * If either vx or vy is NaN, the velocity is initialized to ⟨0,0⟩. If either x or y is NaN, the position
 * is initialized in a phyllotaxis arrangement, so chosen to ensure a deterministic, uniform distribution around the origin.
 *
 * To fix a node in a given position, you may specify two additional properties:
 *      fx - the node’s fixed x-position
 *      fy - the node’s fixed y-position
 *
 * At the end of each tick, after the application of any forces, a node with a defined node.fx
 * has node.x reset to this value and node.vx set to zero; likewise, a node with a defined node.fy
 * has node.y reset to this value and node.vy set to zero. To unfix a node that was previously fixed,
 * set node.fx and node.fy to null, or delete these properties.
 *
 * If the specified array of nodes is modified, such as when nodes are added to or removed from the
 * simulation, this method must be called again with the new (or changed) array to notify the simulation and bound
 * forces of the change; the simulation does not make a defensive copy of the specified array.
 */

public data class ForceNode<D>(
    val index: Int,
    val domain: D,
    var x: Double = Double.NaN,
    var y: Double = Double.NaN,
    var vx: Double = Double.NaN,
    var vy: Double = Double.NaN,
    var fixedX: Double? = null,
    var fixedY: Double? = null,
) {
    var position: Point
        get() = Point(
            x,
            y,
        )
        set(value) {
            x = value.x
            y = value.y
        }

    var velocity: Vector
        get() = Vector(
            vx,
            vy,
        )
        set(value) {
            vx = value.vx
            vy = value.vy
        }
}
