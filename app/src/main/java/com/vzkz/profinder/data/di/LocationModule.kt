package com.vzkz.profinder.data.di

import android.content.Context
import com.google.android.gms.location.LocationServices
import com.vzkz.profinder.data.ILocationService
import com.vzkz.profinder.data.LocationService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocationModule {
    @Singleton
    @Provides
    fun provideLocationClient(
        @ApplicationContext context: Context
    ): ILocationService = LocationService(
        context,
        LocationServices.getFusedLocationProviderClient(context)
    )
}