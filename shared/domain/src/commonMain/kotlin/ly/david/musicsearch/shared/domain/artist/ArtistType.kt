package ly.david.musicsearch.shared.domain.artist

enum class ArtistType(val id: String) {
    CHARACTER("5c1375b0-f18d-3db7-a164-a49d7a63773f"),
    CHOIR("6124967d-7e3a-3eba-b642-c9a2ffb44d94"),
    GROUP("e431f5f6-b5d2-343d-8b36-72607fffb74b"),
    ORCHESTRA("a0b36c92-3eb1-3839-a4f9-4799823f54a5"),
    OTHER("ac897045-5043-3294-969b-187360e45d86"),
    PERSON("b6e035f4-3ce9-331c-97df-83397230b0df"),
    ;

    companion object {
        private val ID_MAP = entries.associateBy { it.id }

        fun fromId(id: String?): ArtistType? = ID_MAP[id]
    }
}
