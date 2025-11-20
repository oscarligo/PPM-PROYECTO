package com.example.ppm_proyecto.domain.di

import com.example.ppm_proyecto.domain.repository.user.UserRepository
import com.example.ppm_proyecto.domain.repository.user.UserRepositoryImpl
import com.example.ppm_proyecto.domain.usecase.user.GetUserUseCase
import com.example.ppm_proyecto.domain.usecase.user.CreateUserUseCase
import com.example.ppm_proyecto.domain.usecase.user.GetStudentNotificationsUseCase
import com.example.ppm_proyecto.domain.usecase.user.UpdateUserNfcTagUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UserModule {

    @Provides
    @Singleton
    fun provideUserRepository(impl: UserRepositoryImpl): UserRepository = impl

    @Provides
    fun provideGetUserUseCase(repo: UserRepository): GetUserUseCase = GetUserUseCase(repo)

    @Provides
    fun provideCreateUserUseCase(repo: UserRepository): CreateUserUseCase = CreateUserUseCase(repo)

    @Provides
    fun provideGetStudentNotificationsUseCase(repo: UserRepository): GetStudentNotificationsUseCase = GetStudentNotificationsUseCase(repo)

    @Provides
    fun provideUpdateUserNfcTagUseCase(repo: UserRepository): UpdateUserNfcTagUseCase = UpdateUserNfcTagUseCase(repo)
}


