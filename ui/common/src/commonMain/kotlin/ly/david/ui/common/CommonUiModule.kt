package ly.david.ui.common

import ly.david.ui.common.paging.RelationsList
import ly.david.ui.common.place.PlacesByEntityPresenter
import ly.david.ui.common.relation.RelationsPresenter
import ly.david.ui.common.release.ReleasesByEntityPresenter
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val commonUiModule = module {
    singleOf(::PlacesByEntityPresenter)
    singleOf(::RelationsPresenter)
    singleOf(::ReleasesByEntityPresenter)

    factory {
        RelationsList(get())
    }
}
