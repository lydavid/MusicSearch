package ly.david.musicsearch.ui.common.area

import androidx.compose.runtime.Composable
import ly.david.musicsearch.shared.domain.area.AreaType
import musicsearch.ui.common.generated.resources.Res
import musicsearch.ui.common.generated.resources.city
import musicsearch.ui.common.generated.resources.country
import musicsearch.ui.common.generated.resources.county
import musicsearch.ui.common.generated.resources.district
import musicsearch.ui.common.generated.resources.indigenousTerritory
import musicsearch.ui.common.generated.resources.island
import musicsearch.ui.common.generated.resources.militaryBase
import musicsearch.ui.common.generated.resources.municipality
import musicsearch.ui.common.generated.resources.subdivision
import org.jetbrains.compose.resources.stringResource

@Composable
fun AreaType.getDisplayString(): String {
    return stringResource(
        when (this) {
            AreaType.Country -> Res.string.country
            AreaType.Subdivision -> Res.string.subdivision
            AreaType.County -> Res.string.county
            AreaType.Municipality -> Res.string.municipality
            AreaType.City -> Res.string.city
            AreaType.District -> Res.string.district
            AreaType.Island -> Res.string.island
            AreaType.MilitaryBase -> Res.string.militaryBase
            AreaType.IndigenousTerritory -> Res.string.indigenousTerritory
        },
    )
}
