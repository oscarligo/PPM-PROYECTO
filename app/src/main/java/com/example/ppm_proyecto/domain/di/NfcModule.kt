package com.example.ppm_proyecto.domain.di

import com.example.ppm_proyecto.data.remote.nfc.NfcRemoteDataSource
import com.example.ppm_proyecto.domain.repository.nfc.NfcRepository
import com.example.ppm_proyecto.domain.repository.nfc.NfcRepositoryImpl
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Módulo de Hilt para las dependencias relacionadas con NFC
 */
@Module
@InstallIn(SingletonComponent::class)
object NfcModule {

    @Provides
    @Singleton
    fun provideNfcRemoteDataSource(
        firestore: FirebaseFirestore
    ): NfcRemoteDataSource {
        return NfcRemoteDataSource(firestore)
    }

    @Provides
    @Singleton
    fun provideNfcRepository(
        remoteDataSource: NfcRemoteDataSource
    ): NfcRepository {
        return NfcRepositoryImpl(remoteDataSource)
    }
}

