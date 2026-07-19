package ly.david.musicsearch.shared.domain.listen

/**
 * Unlike [ly.david.musicsearch.shared.domain.listitem.ReleaseListItemModel.ListenState],
 * we do not have an unknown state. Recordings and artists are known just from a mapped listen,
 * without having to click into them.
 */
interface ListenInfo {
    val listenCount: Long?
    val lastListenedAtMs: Long?
}
