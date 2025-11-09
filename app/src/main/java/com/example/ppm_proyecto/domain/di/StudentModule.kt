package com.example.ppm_proyecto.domain.di

import com.example.ppm_proyecto.domain.repository.student.StudentRepositoryImpl
import com.example.ppm_proyecto.domain.repository.student.StudentRepository
import com.example.ppm_proyecto.domain.usecase.student.GetCourseSessionsUseCase
import com.example.ppm_proyecto.domain.usecase.student.GetStudentAttendanceUseCase
import com.example.ppm_proyecto.domain.usecase.student.GetStudentCoursesUseCase
import com.example.ppm_proyecto.domain.usecase.student.EnrollInCourseUseCase
import com.example.ppm_proyecto.domain.usecase.student.DropCourseUseCase
import com.example.ppm_proyecto.domain.usecase.student.MarkAttendanceUseCase
import com.example.ppm_proyecto.domain.usecase.student.IsStudentEnrolledInCourseUseCase
import com.example.ppm_proyecto.domain.usecase.student.GetStudentSessionAttendanceUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn

import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object StudentUseCasesModule {

    @Provides
    @Singleton
    fun repo (firebase: StudentRepositoryImpl): StudentRepository = firebase


    @Provides
    fun getStudentCourses(repository: StudentRepository): GetStudentCoursesUseCase =
        GetStudentCoursesUseCase(repository)

    @Provides
    fun getCourseSessions(repository: StudentRepository): GetCourseSessionsUseCase =
        GetCourseSessionsUseCase(repository)

    @Provides
    fun getStudentAttendance(repository: StudentRepository): GetStudentAttendanceUseCase =
        GetStudentAttendanceUseCase(repository)

    @Provides
    fun enrollInCourse(repository: StudentRepository): EnrollInCourseUseCase =
        EnrollInCourseUseCase(repository)

    @Provides
    fun dropCourse(repository: StudentRepository): DropCourseUseCase =
        DropCourseUseCase(repository)

    @Provides
    fun markAttendance(repository: StudentRepository): MarkAttendanceUseCase =
        MarkAttendanceUseCase(repository)


    @Provides
    fun isStudentEnrolled(repository: StudentRepository): IsStudentEnrolledInCourseUseCase =
        IsStudentEnrolledInCourseUseCase(repository)

    @Provides
    fun getStudentSessionAttendance(repository: StudentRepository): GetStudentSessionAttendanceUseCase =
        GetStudentSessionAttendanceUseCase(repository)
}