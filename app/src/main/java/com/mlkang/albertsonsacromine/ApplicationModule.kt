package com.mlkang.albertsonsacromine

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

@Module
@InstallIn(SingletonComponent::class)
class ApplicationModule {
    @Provides
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://www.nactem.ac.uk/")
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
    }

    @Provides
    fun provideAcromineRemoteDataSource(retrofit: Retrofit): AcromineRemoteDataSource {
        return retrofit.create(AcromineRemoteDataSource::class.java)
    }
}