package ly.david.musicsearch.shared.domain.metadata

interface MetadataRepository {
    fun getAppDatabaseVersion(): String
    fun getSQLiteVersion(): String
}
