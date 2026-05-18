package ly.david.musicsearch.ui.common.label

import androidx.compose.runtime.Composable
import ly.david.musicsearch.shared.domain.label.LabelType
import musicsearch.ui.common.generated.resources.Res
import musicsearch.ui.common.generated.resources.bootlegProduction
import musicsearch.ui.common.generated.resources.broadcaster
import musicsearch.ui.common.generated.resources.creativeAgency
import musicsearch.ui.common.generated.resources.distributor
import musicsearch.ui.common.generated.resources.holding
import musicsearch.ui.common.generated.resources.imprint
import musicsearch.ui.common.generated.resources.manufacturer
import musicsearch.ui.common.generated.resources.originalProduction
import musicsearch.ui.common.generated.resources.production
import musicsearch.ui.common.generated.resources.publisher
import musicsearch.ui.common.generated.resources.reissueProduction
import musicsearch.ui.common.generated.resources.rightsSociety
import org.jetbrains.compose.resources.stringResource

@Composable
fun LabelType.getDisplayString(): String {
    return stringResource(
        when (this) {
            LabelType.BootlegProduction -> Res.string.bootlegProduction
            LabelType.Broadcaster -> Res.string.broadcaster
            LabelType.CreativeAgency -> Res.string.creativeAgency
            LabelType.Distributor -> Res.string.distributor
            LabelType.Holding -> Res.string.holding
            LabelType.Imprint -> Res.string.imprint
            LabelType.Manufacturer -> Res.string.manufacturer
            LabelType.OriginalProduction -> Res.string.originalProduction
            LabelType.Production -> Res.string.production
            LabelType.Publisher -> Res.string.publisher
            LabelType.ReissueProduction -> Res.string.reissueProduction
            LabelType.RightsSociety -> Res.string.rightsSociety
        },
    )
}
