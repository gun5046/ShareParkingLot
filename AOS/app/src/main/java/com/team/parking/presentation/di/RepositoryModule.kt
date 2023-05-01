package com.team.parking.presentation.di

import com.team.parking.data.repository.MapRepositoryImpl
import com.team.parking.data.repository.SearchRepositoryImpl
import com.team.parking.data.repository.dataSource.MapRemoteDatasource
import com.team.parking.data.repository.dataSource.SearchRemoteDataSource
import com.team.parking.domain.repository.MapRepository
import com.team.parking.domain.repository.SearchRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
class RepositoryModule {

    @Singleton
    @Provides
    fun provideMapRepository(
        mapRemoteDataSource: MapRemoteDatasource
    ):MapRepository{
        return MapRepositoryImpl(mapRemoteDataSource)
    }

    @Singleton
    @Provides
    fun provideSearchRepository(
        searchRemoteDataSource: SearchRemoteDataSource
    ) : SearchRepository{
        return SearchRepositoryImpl(searchRemoteDataSource)
    }

}