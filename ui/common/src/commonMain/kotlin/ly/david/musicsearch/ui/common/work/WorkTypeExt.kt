package ly.david.musicsearch.ui.common.work

import androidx.compose.runtime.Composable
import ly.david.musicsearch.shared.domain.work.WorkType
import musicsearch.ui.common.generated.resources.Res
import musicsearch.ui.common.generated.resources.aria
import musicsearch.ui.common.generated.resources.audio_drama
import musicsearch.ui.common.generated.resources.ballet
import musicsearch.ui.common.generated.resources.beijing_opera
import musicsearch.ui.common.generated.resources.cantata
import musicsearch.ui.common.generated.resources.concerto
import musicsearch.ui.common.generated.resources.etude
import musicsearch.ui.common.generated.resources.incidental_music
import musicsearch.ui.common.generated.resources.madrigal
import musicsearch.ui.common.generated.resources.mass
import musicsearch.ui.common.generated.resources.motet
import musicsearch.ui.common.generated.resources.musical
import musicsearch.ui.common.generated.resources.opera
import musicsearch.ui.common.generated.resources.operetta
import musicsearch.ui.common.generated.resources.oratorio
import musicsearch.ui.common.generated.resources.overture
import musicsearch.ui.common.generated.resources.partita
import musicsearch.ui.common.generated.resources.play
import musicsearch.ui.common.generated.resources.poem
import musicsearch.ui.common.generated.resources.prose
import musicsearch.ui.common.generated.resources.quartet
import musicsearch.ui.common.generated.resources.sonata
import musicsearch.ui.common.generated.resources.song
import musicsearch.ui.common.generated.resources.song_cycle
import musicsearch.ui.common.generated.resources.soundtrack
import musicsearch.ui.common.generated.resources.suite
import musicsearch.ui.common.generated.resources.symphonic_poem
import musicsearch.ui.common.generated.resources.symphony
import musicsearch.ui.common.generated.resources.zarzuela
import org.jetbrains.compose.resources.stringResource

@Composable
fun WorkType.getDisplayString(): String {
    return stringResource(
        when (this) {
            WorkType.Aria -> Res.string.aria
            WorkType.AudioDrama -> Res.string.audio_drama
            WorkType.Ballet -> Res.string.ballet
            WorkType.BeijingOpera -> Res.string.beijing_opera
            WorkType.Cantata -> Res.string.cantata
            WorkType.Concerto -> Res.string.concerto
            WorkType.Etude -> Res.string.etude
            WorkType.IncidentalMusic -> Res.string.incidental_music
            WorkType.Madrigal -> Res.string.madrigal
            WorkType.Mass -> Res.string.mass
            WorkType.Motet -> Res.string.motet
            WorkType.Musical -> Res.string.musical
            WorkType.Opera -> Res.string.opera
            WorkType.Operetta -> Res.string.operetta
            WorkType.Oratorio -> Res.string.oratorio
            WorkType.Overture -> Res.string.overture
            WorkType.Partita -> Res.string.partita
            WorkType.Play -> Res.string.play
            WorkType.Poem -> Res.string.poem
            WorkType.Prose -> Res.string.prose
            WorkType.Quartet -> Res.string.quartet
            WorkType.Sonata -> Res.string.sonata
            WorkType.Song -> Res.string.song
            WorkType.SongCycle -> Res.string.song_cycle
            WorkType.Soundtrack -> Res.string.soundtrack
            WorkType.Suite -> Res.string.suite
            WorkType.Symphony -> Res.string.symphony
            WorkType.SymphonicPoem -> Res.string.symphonic_poem
            WorkType.Zarzuela -> Res.string.zarzuela
        },
    )
}
