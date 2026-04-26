package ly.david.musicsearch.shared.domain.work

// search results doesn't include type_id, so we need to use its name
enum class WorkType(val idFromName: String) {
    Aria("Aria"),
    AudioDrama("Audio drama"),
    Ballet("Ballet"),
    BeijingOpera("Beijing opera"),
    Cantata("Cantata"),
    Concerto("Concerto"),
    Etude("Étude"),
    IncidentalMusic("Incidental music"),
    Madrigal("Madrigal"),
    Mass("Mass"),
    Motet("Motet"),
    Musical("Musical"),
    Opera("Opera"),
    Operetta("Operetta"),
    Oratorio("Oratorio"),
    Overture("Overture"),
    Partita("Partita"),
    Play("Play"),
    Poem("Poem"),
    Prose("Prose"),
    Quartet("Quartet"),
    Sonata("Sonata"),
    Song("Song"),
    SongCycle("Song-cycle"),
    Soundtrack("Soundtrack"),
    Suite("Suite"),
    SymphonicPoem("Symphonic poem"),
    Symphony("Symphony"),
    Zarzuela("Zarzuela"),
    ;

    companion object {
        private val ID_MAP = entries.associateBy { it.idFromName }

        fun fromId(id: String?): WorkType? = ID_MAP[id]
    }
}
