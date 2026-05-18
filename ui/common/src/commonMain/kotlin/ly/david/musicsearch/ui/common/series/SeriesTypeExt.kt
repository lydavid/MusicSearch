package ly.david.musicsearch.ui.common.series

import androidx.compose.runtime.Composable
import ly.david.musicsearch.shared.domain.series.SeriesType
import musicsearch.ui.common.generated.resources.Res
import musicsearch.ui.common.generated.resources.artistAward
import musicsearch.ui.common.generated.resources.artistSeries
import musicsearch.ui.common.generated.resources.awardCeremony
import musicsearch.ui.common.generated.resources.catalogue
import musicsearch.ui.common.generated.resources.eventSeries
import musicsearch.ui.common.generated.resources.festival
import musicsearch.ui.common.generated.resources.podcast
import musicsearch.ui.common.generated.resources.recording
import musicsearch.ui.common.generated.resources.releaseGroupAward
import musicsearch.ui.common.generated.resources.releaseGroupSeries
import musicsearch.ui.common.generated.resources.releaseSeries
import musicsearch.ui.common.generated.resources.residency
import musicsearch.ui.common.generated.resources.run
import musicsearch.ui.common.generated.resources.seriesAward
import musicsearch.ui.common.generated.resources.seriesSeries
import musicsearch.ui.common.generated.resources.tour
import musicsearch.ui.common.generated.resources.work
import musicsearch.ui.common.generated.resources.workSeries
import org.jetbrains.compose.resources.stringResource

@Composable
fun SeriesType.getDisplayString(): String {
    return stringResource(
        when (this) {
            SeriesType.ArtistAward -> Res.string.artistAward
            SeriesType.ArtistSeries -> Res.string.artistSeries
            SeriesType.AwardCeremony -> Res.string.awardCeremony
            SeriesType.Catalogue -> Res.string.catalogue
            SeriesType.EventSeries -> Res.string.eventSeries
            SeriesType.Festival -> Res.string.festival
            SeriesType.Podcast -> Res.string.podcast
            SeriesType.RecordingAward -> Res.string.recording
            SeriesType.RecordingSeries -> Res.string.recording
            SeriesType.ReleaseGroupAward -> Res.string.releaseGroupAward
            SeriesType.ReleaseGroupSeries -> Res.string.releaseGroupSeries
            SeriesType.ReleaseSeries -> Res.string.releaseSeries
            SeriesType.Residency -> Res.string.residency
            SeriesType.Run -> Res.string.run
            SeriesType.SeriesAward -> Res.string.seriesAward
            SeriesType.SeriesSeries -> Res.string.seriesSeries
            SeriesType.Tour -> Res.string.tour
            SeriesType.WorkAward -> Res.string.work
            SeriesType.WorkSeries -> Res.string.workSeries
        },
    )
}
