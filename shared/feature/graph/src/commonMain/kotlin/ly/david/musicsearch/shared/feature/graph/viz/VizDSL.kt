/*
 * Copyright 2022-2022 Prat Tana
 * Copyright 2024 David Ly
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ly.david.musicsearch.shared.feature.graph.viz

import io.data2viz.viz.CircleNode
import io.data2viz.viz.LineNode
import io.data2viz.viz.PathNode
import io.data2viz.viz.RectNode
import io.data2viz.viz.TextNode

inline fun circle(init: CircleNode.() -> Unit): CircleNode =
    CircleNode().apply(init)

inline fun line(init: LineNode.() -> Unit): LineNode =
    LineNode().apply(init)

inline fun path(init: PathNode.() -> Unit): PathNode =
    PathNode().apply(init)

inline fun rect(init: RectNode.() -> Unit): RectNode =
    RectNode().apply(init)

inline fun text(init: TextNode.() -> Unit): TextNode =
    TextNode().apply(init)
