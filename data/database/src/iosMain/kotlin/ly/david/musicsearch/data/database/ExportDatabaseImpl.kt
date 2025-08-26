package ly.david.musicsearch.data.database

import ly.david.musicsearch.shared.domain.ExportDatabase

internal class ExportDatabaseImpl : ExportDatabase {
    override suspend operator fun invoke(): String {
        return "Not supported yet!"
    }
}
