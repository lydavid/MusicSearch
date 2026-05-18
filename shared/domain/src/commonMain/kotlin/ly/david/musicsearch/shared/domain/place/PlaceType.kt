package ly.david.musicsearch.shared.domain.place

// search results does include type_id, but let's map with name anyway for simplicity
enum class PlaceType(val displayName: String) {
    Amphitheatre("Amphitheatre"),
    Club("Club"),
    ConcertHallTheatre("Concert hall / Theatre"),
    EducationalInstitution("Educational institution"),
    FestivalStage("Festival stage"),
    IndoorArena("Indoor arena"),
    Other("Other"),
    Park("Park"),
    PressingPlant("Pressing plant"),
    ReligiousBuilding("Religious building"),
    Stadium("Stadium"),
    Studio("Studio"),
    Venue("Venue"),
    ;

    companion object {
        private val NAME_MAP = entries.associateBy { it.displayName }

        fun fromName(name: String?): PlaceType? = NAME_MAP[name]
    }
}
