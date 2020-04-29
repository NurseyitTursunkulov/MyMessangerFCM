package com.example.mymessangerfcm

import android.app.Application
import com.example.core.data.implementation.core.RepositoryMessangerImpl
import com.example.core.data.logic.core.RepositoryMessanger
import com.example.core.domain.implementation.MessangerDomainImpl
import com.example.core.domain.logic.core.MessangerDomain
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidFileProperties
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.module

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            // use AndroidLogger as Koin Logger - default Level.INFO
            androidLogger()

            // use the Android context given there
            androidContext(this@App)

            // load properties from assets/koin.properties file
            androidFileProperties()

            // module list
            modules(offlineWeatherApp)
        }
    }

    private val offlineWeatherApp: Module = module {
        viewModel {
            MessangerViewModel(
                messangerDomainImpl = get()
            )
        }

        single<MessangerDomain> {
            MessangerDomainImpl(
                repositoryMessanger = get()
            )
        }
        single<RepositoryMessanger> { RepositoryMessangerImpl() }
    }
}