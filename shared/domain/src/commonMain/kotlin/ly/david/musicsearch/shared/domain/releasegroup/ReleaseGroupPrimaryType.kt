package ly.david.musicsearch.shared.domain.releasegroup

enum class ReleaseGroupPrimaryType(val id: String) {
    Album("f529b476-6e62-324f-b0aa-1f3e33d313fc"),
    Broadcast("3b2e49e1-2875-37b8-9fa9-1f7cf3f49900"),
    EP("6d0c5bf6-7a33-3420-a519-44fc63eedebf"),
    Other("4fc3be2b-de1e-396b-a933-beb8f1607a22"),
    Single("d6038452-8ee0-3f68-affc-2de9a1ede0b9"),
    ;

    companion object {
        private val ID_MAP = entries.associateBy { it.id }

        fun fromId(id: String?): ReleaseGroupPrimaryType? = ID_MAP[id]
    }
}
