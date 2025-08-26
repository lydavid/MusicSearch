package ly.david.musicsearch.shared.domain

interface ExportDatabase {
    suspend operator fun invoke(): String
}
