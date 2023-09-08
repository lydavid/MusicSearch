package ly.david.data.core

/**
 * All formats in this list of media grouped together.
 *
 * Example returns:
 * * 170xCD
 * * 2×CD + Blu-ray
 */
fun List<String?>?.getFormatsForDisplay(): String {
    val hashMap = hashMapOf<String, Int>()

    this?.forEach { format ->
        if (format != null) {
            val currentCount = hashMap[format]
            if (currentCount == null) {
                hashMap[format] = 1
            } else {
                hashMap[format] = currentCount + 1
            }
        }
    }

    return if (hashMap.isEmpty()) {
        ""
    } else {
        hashMap.map {
            val count = it.value
            if (count == 1) {
                it.key
            } else {
                "$count×${it.key}"
            }
        }.joinToString(" + ")
    }
}
