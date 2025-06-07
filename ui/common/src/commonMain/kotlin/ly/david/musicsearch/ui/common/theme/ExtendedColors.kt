package ly.david.musicsearch.ui.common.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

private val AreaLight = Color(0xFF4CAF50) // Green
private val AreaDark = Color(0xFF2E7D32)

private val ArtistLight = PrimaryLight
private val ArtistDark = PrimaryDark

private val CollectionLight = Color(0xFFFF9800) // Orange
private val CollectionDark = Color(0xFFF57C00)

private val EventLight = Color(0xFFE91E63) // Pink
private val EventDark = Color(0xFFC2185B)

private val GenreLight = Color(0xFF3F51B5) // Indigo
private val GenreDark = Color(0xFF303F9F)

private val InstrumentLight = Color(0xFF795548) // Brown
private val InstrumentDark = Color(0xFF5D4037)

private val LabelLight = Color(0xFF607D8B) // Blue Grey
private val LabelDark = Color(0xFF455A64)

private val PlaceLight = Color(0xFF009688) // Teal
private val PlaceDark = Color(0xFF00796B)

private val RecordingLight = Color(0xFFC51162)
private val RecordingDark = Color(0xFFFF4081)

private val ReleaseLight = Color(0xFF2196F3) // Blue
private val ReleaseDark = Color(0xFF1976D2)

private val ReleaseGroupLight = Color(0xFF03A9F4) // Light Blue
private val ReleaseGroupDark = Color(0xFF0288D1)

private val SeriesLight = Color(0xFFFFEB3B) // Yellow
private val SeriesDark = Color(0xFFFBC02D)

private val WorkLight = Color(0xFF673AB7) // Deep Purple
private val WorkDark = Color(0xFF512DA8)

private val UrlLight = Color(0xFF8BC34A) // Light Green
private val UrlDark = Color(0xFF689F38)

@Immutable
data class ExtendedColors(
    val artist: Color,
    val area: Color,
    val collection: Color,
    val event: Color,
    val genre: Color,
    val instrument: Color,
    val label: Color,
    val place: Color,
    val recording: Color,
    val release: Color,
    val releaseGroup: Color,
    val series: Color,
    val work: Color,
    val url: Color,
)

internal val DarkExtendedColors = ExtendedColors(
    artist = ArtistDark,
    area = AreaDark,
    collection = CollectionDark,
    event = EventDark,
    genre = GenreDark,
    instrument = InstrumentDark,
    label = LabelDark,
    place = PlaceDark,
    recording = RecordingDark,
    release = ReleaseDark,
    releaseGroup = ReleaseGroupDark,
    series = SeriesDark,
    work = WorkDark,
    url = UrlDark,
)

internal val LightExtendedColors = ExtendedColors(
    artist = ArtistLight,
    area = AreaLight,
    collection = CollectionLight,
    event = EventLight,
    genre = GenreLight,
    instrument = InstrumentLight,
    label = LabelLight,
    place = PlaceLight,
    recording = RecordingLight,
    release = ReleaseLight,
    releaseGroup = ReleaseGroupLight,
    series = SeriesLight,
    work = WorkLight,
    url = UrlLight,
)

@Suppress("CompositionLocalAllowlist")
val LocalExtendedColors = staticCompositionLocalOf { LightExtendedColors }
