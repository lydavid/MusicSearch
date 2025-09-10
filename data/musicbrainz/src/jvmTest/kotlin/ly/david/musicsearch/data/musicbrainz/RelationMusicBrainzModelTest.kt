package ly.david.musicsearch.data.musicbrainz

import io.mockk.every
import io.mockk.mockk
import ly.david.musicsearch.data.musicbrainz.models.relation.RelationMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.relation.getFormattedAttributes
import org.junit.Assert.assertEquals
import org.junit.Test

internal class RelationMusicBrainzModelTest {

    private val relation: RelationMusicBrainzModel = mockk()

    @Test
    fun `null`() {
        every { relation.attributes } returns null
        every { relation.attributeValues } returns null

        assertEquals("", relation.getFormattedAttributes())
    }

    @Test
    fun empty() {
        every { relation.attributes } returns listOf()
        every { relation.attributeValues } returns mapOf()

        assertEquals("", relation.getFormattedAttributes())
    }

    @Test
    fun `attribute without attribute value`() {
        every { relation.attributes } returns listOf("strings")
        every { relation.attributeValues } returns mapOf()

        assertEquals("strings", relation.getFormattedAttributes())
    }

    @Test
    fun `attribute with attribute value`() {
        every { relation.attributes } returns listOf("task")
        every { relation.attributeValues } returns mapOf("task" to "director & organizer")

        assertEquals("task: director & organizer", relation.getFormattedAttributes())
    }

    @Test
    fun `attribute with attribute value but null`() {
        every { relation.attributes } returns listOf("task")
        every { relation.attributeValues } returns mapOf()

        assertEquals("task", relation.getFormattedAttributes())
    }

    @Test
    fun `attribute with attribute value and attribute without`() {
        every { relation.attributes } returns listOf("task", "strings")
        every { relation.attributeValues } returns mapOf("task" to "director & organizer")

        assertEquals("task: director & organizer, strings", relation.getFormattedAttributes())
    }

    @Test
    fun `attribute without attribute value and attribute with`() {
        every { relation.attributes } returns listOf("strings", "task")
        every { relation.attributeValues } returns mapOf("task" to "director & organizer")

        assertEquals("strings, task: director & organizer", relation.getFormattedAttributes())
    }
}
