package ly.david.musicsearch.shared.domain.tag

sealed interface GenreOrTag {
    val count: Int
    val name: String
    val fullName: String
}
