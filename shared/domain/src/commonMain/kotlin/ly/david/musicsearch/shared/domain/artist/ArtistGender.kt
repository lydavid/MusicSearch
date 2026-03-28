package ly.david.musicsearch.shared.domain.artist

// consider whether to store id and map from that instead
enum class ArtistGender(val id: String) {
    FEMALE("female"),
    MALE("male"),
    NON_BINARY("non-binary"),
    NOT_APPLICABLE("not applicable"),
    OTHER("other"),
    ;

    companion object {
        private val ID_MAP = entries.associateBy { it.id }

        fun fromId(id: String?): ArtistGender? = ID_MAP[id?.lowercase()]
    }
}
