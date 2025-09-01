package ly.david.musicsearch.shared.domain.release

enum class ReleaseStatus(val id: String) {
    OFFICIAL("4e304316-386d-3409-af2e-78857eec5cfe"),
    PROMOTION("518ffc83-5cde-34df-8627-81bff5093d92"),
    BOOTLEG("1156806e-d06a-38bd-83f0-cf2284a808b9"),
    PSEUDO_RELEASE("41121bb9-3413-3818-8a9a-9742318349aa"),
    WITHDRAWN("6a3772de-4605-4132-993d-aa315cd19b4b"),
    EXPUNGED("cddc06b1-1afc-4bbb-83e6-0a902ffb4aba"),
    CANCELLED("55005350-bc3f-441a-b0c7-27c565eae341"),
    ;

    companion object {
        private val ID_MAP = entries.associateBy { it.id }

        fun fromId(id: String?): ReleaseStatus? = ID_MAP[id]
    }
}
