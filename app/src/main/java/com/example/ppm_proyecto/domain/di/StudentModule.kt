package com.example.ppm_proyecto.domain.di

import com.example.ppm_proyecto.data.repository.student.StudentRepositoryImpl
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
import com.example.ppm_proyecto.domain.repository.student.StudentMockRepositoryImpl

@Module
@InstallIn(SingletonComponent::class)
object StudentUseCasesModule {

    @Provides
    @Singleton
    fun repo (firebase: StudentRepositoryImpl): StudentRepository = firebase


    @Provides
    fun getStudentData(repository: StudentRepository): GetStudentDataUseCase =
        GetStudentDataUseCase(repository)

    @Provides
    fun getStudentCourses(repository: StudentRepository): GetStudentCoursesUseCase =
        GetStudentCoursesUseCase(repository)

    @Provides
    fun getCourseSessions(repository: StudentRepository): GetCourseSessionsUseCase =
        GetCourseSessionsUseCase(repository)

    @Provides
    fun getStudentAttendance(repository: StudentRepository): GetStudentAttendanceUseCase =
        GetStudentAttendanceUseCase(repository)

}