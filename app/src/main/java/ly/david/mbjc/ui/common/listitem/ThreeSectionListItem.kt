package ly.david.mbjc.ui.common

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import ly.david.mbjc.ExcludeFromJacocoGeneratedReport
import ly.david.mbjc.ui.common.listitem.ClickableListItem
import ly.david.mbjc.ui.common.preview.DefaultPreviews
import ly.david.mbjc.ui.theme.PreviewTheme

/**
 * Partitions a list item into three parts with [mainContent] in the center.
 *
 * If they exist, [startContent] and [endContent] will display all of its content on one line centered vertically,
 * forcing [mainContent] to wrap based on remaining space.
 *
 * Content in [startContent] and [endContent] are thus expected to be short.
 * Otherwise they will cause clipping issues.
 */
@Composable
internal fun ThreeSectionListItem(
    onClick: () -> Unit = {},
    startAlignment: Alignment.Horizontal = Alignment.Start,
    startContent: @Composable ColumnScope.() -> Unit = {},
    startMainPadding: Dp = 0.dp,
    mainContent: @Composable ColumnScope.() -> Unit,
    endMainPadding: Dp = 0.dp,
    endContent: @Composable ColumnScope.() -> Unit = {},
    endAlignment: Alignment.Horizontal = Alignment.End,
) {
    ClickableListItem(
        onClick = onClick,
    ) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        ) {
            val (startSection, mainSection, endSection) = createRefs()

            Column(
                modifier = Modifier.constrainAs(startSection) {
                    width = Dimension.wrapContent
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                    end.linkTo(mainSection.start, margin = startMainPadding)
                    bottom.linkTo(parent.bottom)
                },
                horizontalAlignment = startAlignment
            ) {
                startContent()
            }

            Column(
                modifier = Modifier.constrainAs(mainSection) {
                    width = Dimension.fillToConstraints
                    start.linkTo(startSection.end)
                    top.linkTo(parent.top)
                    end.linkTo(endSection.start)
                    bottom.linkTo(parent.bottom)
                }
            ) {
                mainContent()
            }

            Column(
                modifier = Modifier
                    .constrainAs(endSection) {
                        width = Dimension.wrapContent
                        start.linkTo(mainSection.end, margin = endMainPadding)
                        top.linkTo(parent.top)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                    },
                horizontalAlignment = endAlignment
            ) {
                endContent()
            }
        }
    }
}

@ExcludeFromJacocoGeneratedReport
@DefaultPreviews
@Composable
private fun NoStart() {
    PreviewTheme {
        Surface {
            ThreeSectionListItem(
                mainContent = {
                    Text(text = "Main")
                },
                endMainPadding = 16.dp,
                endContent = {
                    Text(text = "End")
                }
            )
        }
    }
}

@ExcludeFromJacocoGeneratedReport
@DefaultPreviews
@Composable
private fun NoEnd() {
    PreviewTheme {
        Surface {
            ThreeSectionListItem(
                startContent = {
                    Text(text = "Start")

                },
                startMainPadding = 16.dp,
                mainContent = {
                    Text(text = "Main")
                }
            )
        }
    }
}

@ExcludeFromJacocoGeneratedReport
@DefaultPreviews
@Composable
private fun All() {
    PreviewTheme {
        Surface {
            ThreeSectionListItem(
                startContent = {
                    Text(text = "Start")

                },
                startMainPadding = 16.dp,
                mainContent = {
                    Text(text = "Main")
                },
                endMainPadding = 16.dp,
                endContent = {
                    Text(text = "End")
                }
            )
        }
    }
}
