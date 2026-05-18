package ly.david.musicsearch.shared.domain.event

// search results doesn't include type_id, so we need to use its name
enum class EventType(val displayName: String) {
    Concert("Concert"),
    Festival("Festival"),
    LaunchEvent("Launch event"),
    ConventionExpo("Convention/Expo"),
    MasterclassClinic("Masterclass/Clinic"),
    StagePerformance("Stage performance"),
    AwardCeremony("Award ceremony"),
    Competition("Competition"),
    ;

    companion object {
        private val NAME_MAP = entries.associateBy { it.displayName }

        fun fromName(name: String?): EventType? = NAME_MAP[name]
    }
}
