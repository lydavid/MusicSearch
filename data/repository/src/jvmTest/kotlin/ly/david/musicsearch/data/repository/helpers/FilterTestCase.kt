package ly.david.musicsearch.data.repository.helpers

data class FilterTestCase<T>(
    val description: String,
    val query: String,
    val expectedResult: List<T>
)
