package ly.david.musicsearch.data.repository.helpers

import androidx.paging.testing.asSnapshot
import app.cash.paging.PagingData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.test.runTest
import ly.david.musicsearch.shared.domain.ListFilters
import org.junit.Assert

fun <T : Any> testFilter(
    pagingFlowProducer: (query: String) -> Flow<PagingData<T>>,
    testCases: List<FilterTestCase<T>>,
) = runTest {
    testCases.forEach { testCase ->
        pagingFlowProducer(testCase.query).asSnapshot().run {
            Assert.assertEquals(
                "${testCase.description} ${testCase.query} did not return the expected result.",
                testCase.expectedResult,
                this,
            )
        }
    }
}

fun <T : Any> testFilters(
    pagingFlowProducer: (listFilters: ListFilters) -> Flow<PagingData<T>>,
    testCases: List<FiltersTestCase<T>>,
) = runTest {
    testCases.forEach { testCase ->
        pagingFlowProducer(testCase.listFilters).asSnapshot().run {
            Assert.assertEquals(
                "${testCase.description} ${testCase.listFilters} did not return the expected result.",
                testCase.expectedResult,
                this,
            )
        }
    }
}
