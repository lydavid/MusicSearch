package ly.david.mbjc.data.persistence

import androidx.room.TypeConverter
import java.util.Date
import ly.david.mbjc.ui.navigation.Destination

// Just need to make sure possible values cannot include this delimiter
private const val DELIMITER = ","

class MusicBrainzRoomTypeConverters {

    // For things like "secondary-types" which does not need its own table.
    @TypeConverter
    fun toString(strings: List<String>?): String? = strings?.joinToString(DELIMITER)

    @TypeConverter
    fun fromString(string: String?): List<String> =
        if (string.isNullOrEmpty()) {
            listOf()
        } else {
            string.split(DELIMITER)
        }

    @TypeConverter
    fun toDestination(string: String?): Destination? = Destination.values().firstOrNull { it.route == string }

    @TypeConverter
    fun fromDestination(destination: Destination?): String? = destination?.route

    @TypeConverter
    fun toDate(dateLong: Long): Date = Date(dateLong)

    @TypeConverter
    fun fromDate(date: Date): Long = date.time
}
