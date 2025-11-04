package com.example.ppm_proyecto.domain.di

import com.example.ppm_proyecto.domain.repository.student.StudentRepository
import com.example.ppm_proyecto.domain.usecase.student.GetCourseSessionsUseCase
import com.example.ppm_proyecto.domain.usecase.student.GetStudentAttendanceUseCase
import com.example.ppm_proyecto.domain.usecase.student.GetStudentCoursesUseCase
import com.example.ppm_proyecto.domain.usecase.student.GetStudentDataUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn

import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object StudentUseCasesModule {

    @Provides
    fun repo(repository: StudentRepository): StudentRepository = repository


    @Provides
    fun provideGetStudentDataUseCase(repository: StudentRepository): GetStudentDataUseCase =
        GetStudentDataUseCase(repository)

    @Provides
    fun provideGetStudentCoursesUseCase(repository: StudentRepository): GetStudentCoursesUseCase =
        GetStudentCoursesUseCase(repository)

    @Provides
    fun provideGetCourseSessionsUseCase(repository: StudentRepository): GetCourseSessionsUseCase =
        GetCourseSessionsUseCase(repository)

    @Provides
    fun provideGetStudentAttendanceUseCase(repository: StudentRepository): GetStudentAttendanceUseCase =
        GetStudentAttendanceUseCase(repository)

}