package ly.david.musicsearch.shared.domain.history

interface VisitedDao {
    fun insert(entityId: String)
    fun contains(entityId: String): Boolean
}
