package ly.david.mbjc.data.persistence

import androidx.room.TypeConverter
import java.util.Date
import ly.david.mbjc.ui.Destination

// Just need to make sure possible values cannot include this delimiter
private const val DELIMITER = ","

class MusicBrainzTypeConverters {

    // For things like "secondary-types" which does not need its own table.
    @TypeConverter
    fun toString(strings: List<String>?): String? {
        return strings?.joinToString(DELIMITER)
    }

    @TypeConverter
    fun fromString(string: String?): List<String>? {
        return string?.split(DELIMITER)
    }

    @TypeConverter
    fun toDestination(string: String?): Destination? {
        return Destination.values().firstOrNull { it.route == string }
    }

    @TypeConverter
    fun fromDestination(destination: Destination?): String? {
        return destination?.route
    }

    @TypeConverter
    fun toDate(dateLong: Long): Date {
        return Date(dateLong)
    }

    @TypeConverter
    fun fromDate(date: Date): Long {
        return date.time
    }
}
