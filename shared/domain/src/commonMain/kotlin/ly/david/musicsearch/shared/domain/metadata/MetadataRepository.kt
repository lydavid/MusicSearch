package ly.david.musicsearch.shared.domain.metadata

interface MetadataRepository {
    fun getDatabaseVersion(): String
}
