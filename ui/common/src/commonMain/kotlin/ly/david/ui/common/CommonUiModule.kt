package ly.david.ui.common

import ly.david.ui.common.paging.RelationsList
import org.koin.dsl.module

// @Module
// @ComponentScan
// class CommonUiModule

val commonUiModule = module {
    factory {
        RelationsList(get())
    }
}
