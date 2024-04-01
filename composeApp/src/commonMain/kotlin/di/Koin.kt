package di

import org.koin.core.context.startKoin

fun initKoin() {
    startKoin {
        modules(appModules())
    }
}

fun appModules() = listOf(queueModule, appModule, authModule, validationModule)