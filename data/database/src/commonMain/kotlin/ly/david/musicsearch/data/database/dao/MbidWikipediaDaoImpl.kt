package ly.david.musicsearch.data.database.dao

import ly.david.musicsearch.data.database.Database
import ly.david.musicsearch.shared.domain.wikimedia.MbidWikipediaDao
import ly.david.musicsearch.shared.domain.wikimedia.WikipediaExtract
import lydavidmusicsearchdatadatabase.Mbid_wikipedia

class MbidWikipediaDaoImpl(
    database: Database,
) : EntityDao, MbidWikipediaDao {
    override val transacter = database.mbid_wikipediaQueries

    override fun save(
        mbid: String,
        wikipediaExtract: WikipediaExtract,
    ) {
        transacter.transaction {
            transacter.insert(
                mbid_wikipedia = Mbid_wikipedia(
                    mbid = mbid,
                    extract = wikipediaExtract.extract,
                    url = wikipediaExtract.wikipediaUrl,
                ),
            )
        }
    }

    override fun deleteById(mbid: String) {
        transacter.deleteById(mbid)
    }
}
