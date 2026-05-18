package ly.david.musicsearch.ui.common.place

import androidx.compose.runtime.Composable
import ly.david.musicsearch.shared.domain.place.PlaceType
import musicsearch.ui.common.generated.resources.Res
import musicsearch.ui.common.generated.resources.amphitheatre
import musicsearch.ui.common.generated.resources.club
import musicsearch.ui.common.generated.resources.concertHallTheatre
import musicsearch.ui.common.generated.resources.educationalInstitution
import musicsearch.ui.common.generated.resources.festivalStage
import musicsearch.ui.common.generated.resources.indoorArena
import musicsearch.ui.common.generated.resources.other
import musicsearch.ui.common.generated.resources.park
import musicsearch.ui.common.generated.resources.pressingPlant
import musicsearch.ui.common.generated.resources.religiousBuilding
import musicsearch.ui.common.generated.resources.stadium
import musicsearch.ui.common.generated.resources.studio
import musicsearch.ui.common.generated.resources.venue
import org.jetbrains.compose.resources.stringResource

@Composable
fun PlaceType.getDisplayString(): String {
    return stringResource(
        when (this) {
            PlaceType.Amphitheatre -> Res.string.amphitheatre
            PlaceType.Club -> Res.string.club
            PlaceType.ConcertHallTheatre -> Res.string.concertHallTheatre
            PlaceType.EducationalInstitution -> Res.string.educationalInstitution
            PlaceType.FestivalStage -> Res.string.festivalStage
            PlaceType.IndoorArena -> Res.string.indoorArena
            PlaceType.Other -> Res.string.other
            PlaceType.Park -> Res.string.park
            PlaceType.PressingPlant -> Res.string.pressingPlant
            PlaceType.ReligiousBuilding -> Res.string.religiousBuilding
            PlaceType.Stadium -> Res.string.stadium
            PlaceType.Studio -> Res.string.studio
            PlaceType.Venue -> Res.string.venue
        },
    )
}
