package ly.david.musicsearch.data.repository.helpers

import ly.david.musicsearch.shared.domain.ListFilters

data class FilterTestCase<T>(
    val description: String,
    val query: String,
    val expectedResult: List<T>
)

data class FiltersTestCase<T>(
    val description: String,
    val listFilters: ListFilters,
    val expectedResult: List<T>
)
