package ly.david.musicsearch.ui.common.event

import androidx.compose.runtime.Composable
import ly.david.musicsearch.shared.domain.event.EventType
import musicsearch.ui.common.generated.resources.Res
import musicsearch.ui.common.generated.resources.awardCeremony
import musicsearch.ui.common.generated.resources.competition
import musicsearch.ui.common.generated.resources.concert
import musicsearch.ui.common.generated.resources.conventionExpo
import musicsearch.ui.common.generated.resources.festival
import musicsearch.ui.common.generated.resources.launchEvent
import musicsearch.ui.common.generated.resources.masterclassClinic
import musicsearch.ui.common.generated.resources.stagePerformance
import org.jetbrains.compose.resources.stringResource

@Composable
fun EventType.getDisplayString(): String {
    return stringResource(
        when (this) {
            EventType.Concert -> Res.string.concert
            EventType.Festival -> Res.string.festival
            EventType.LaunchEvent -> Res.string.launchEvent
            EventType.ConventionExpo -> Res.string.conventionExpo
            EventType.MasterclassClinic -> Res.string.masterclassClinic
            EventType.StagePerformance -> Res.string.stagePerformance
            EventType.AwardCeremony -> Res.string.awardCeremony
            EventType.Competition -> Res.string.competition
        },
    )
}
