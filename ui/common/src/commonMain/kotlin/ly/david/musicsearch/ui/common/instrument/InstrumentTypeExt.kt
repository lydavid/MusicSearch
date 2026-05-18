package ly.david.musicsearch.ui.common.instrument

import androidx.compose.runtime.Composable
import ly.david.musicsearch.shared.domain.instrument.InstrumentType
import musicsearch.ui.common.generated.resources.Res
import musicsearch.ui.common.generated.resources.electronicInstrument
import musicsearch.ui.common.generated.resources.ensemble
import musicsearch.ui.common.generated.resources.instrumentFamily
import musicsearch.ui.common.generated.resources.otherInstrument
import musicsearch.ui.common.generated.resources.percussionInstrument
import musicsearch.ui.common.generated.resources.stringInstrument
import musicsearch.ui.common.generated.resources.windInstrument
import org.jetbrains.compose.resources.stringResource

@Composable
fun InstrumentType.getDisplayString(): String {
    return stringResource(
        when (this) {
            InstrumentType.WindInstrument -> Res.string.windInstrument
            InstrumentType.StringInstrument -> Res.string.stringInstrument
            InstrumentType.PercussionInstrument -> Res.string.percussionInstrument
            InstrumentType.ElectronicInstrument -> Res.string.electronicInstrument
            InstrumentType.OtherInstrument -> Res.string.otherInstrument
            InstrumentType.Ensemble -> Res.string.ensemble
            InstrumentType.Family -> Res.string.instrumentFamily
        },
    )
}
