package ly.david.musicsearch.shared.domain.instrument

// search results does include type_id, but let's map with name anyway for simplicity
enum class InstrumentType(val displayName: String) {
    WindInstrument("Wind instrument"),
    StringInstrument("String instrument"),
    PercussionInstrument("Percussion instrument"),
    ElectronicInstrument("Electronic instrument"),
    OtherInstrument("Other instrument"),
    Ensemble("Ensemble"),
    Family("Family"),
    ;

    companion object {
        private val NAME_MAP = entries.associateBy { it.displayName }

        fun fromName(name: String?): InstrumentType? = NAME_MAP[name]
    }
}
