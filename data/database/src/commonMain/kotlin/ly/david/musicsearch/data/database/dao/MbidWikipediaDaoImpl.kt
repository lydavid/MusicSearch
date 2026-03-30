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
        languageTag: String,
        wikipediaExtract: WikipediaExtract,
    ) {
        transacter.insert(
            mbid_wikipedia = Mbid_wikipedia(
                mbid = mbid,
                language_tag = languageTag,
                extract = wikipediaExtract.extract,
                url = wikipediaExtract.wikipediaUrl,
            ),
        )
    }

    override fun get(
        mbid: String,
        languageTag: String,
    ): WikipediaExtract? =
        transacter.get(
            mbid = mbid,
            languageTag = languageTag,
            mapper = { extract: String, url: String ->
                WikipediaExtract(
                    extract = extract,
                    wikipediaUrl = url,
                )
            },
        ).executeAsOneOrNull()

    override fun deleteByIdAndTag(
        mbid: String,
        languageTag: String,
    ) {
        transacter.deleteByIdAndTag(
            mbid = mbid,
            languageTag = languageTag,
        )
    }
}
