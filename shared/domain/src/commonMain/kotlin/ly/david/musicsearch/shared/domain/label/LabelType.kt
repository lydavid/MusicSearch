package ly.david.musicsearch.shared.domain.label

// search results does include type_id, but let's map with name anyway for simplicity
enum class LabelType(val displayName: String) {
    BootlegProduction("Bootleg Production"),
    Broadcaster("Broadcaster"),
    CreativeAgency("Creative Agency"),
    Distributor("Distributor"),
    Holding("Holding"),
    Imprint("Imprint"),
    Manufacturer("Manufacturer"),
    OriginalProduction("Original Production"),
    Production("Production"),
    Publisher("Publisher"),
    ReissueProduction("Reissue Production"),
    RightsSociety("Rights Society"),
    ;

    companion object {
        private val NAME_MAP = entries.associateBy { it.displayName }

        fun fromName(name: String?): LabelType? = NAME_MAP[name]
    }
}
