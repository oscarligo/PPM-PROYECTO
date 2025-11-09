package com.example.ppm_proyecto.domain.di

import com.example.ppm_proyecto.domain.repository.teacher.TeacherRepository
import com.example.ppm_proyecto.domain.repository.teacher.TeacherRepositoryImpl
import com.example.ppm_proyecto.domain.usecase.teacher.CreateCourseUseCase
import com.example.ppm_proyecto.domain.usecase.teacher.CreateSessionUseCase
import com.example.ppm_proyecto.domain.usecase.teacher.DeleteCourseUseCase
import com.example.ppm_proyecto.domain.usecase.teacher.DeleteSessionUseCase
import com.example.ppm_proyecto.domain.usecase.teacher.GetCourseSessionsForTeacherUseCase
import com.example.ppm_proyecto.domain.usecase.teacher.GetCourseStudentsUseCase
import com.example.ppm_proyecto.domain.usecase.teacher.GetTeacherCoursesUseCase
import com.example.ppm_proyecto.domain.usecase.teacher.GetTeacherProfileUseCase
import com.example.ppm_proyecto.domain.usecase.teacher.MarkStudentAttendanceUseCase
import com.example.ppm_proyecto.domain.usecase.teacher.UpdateCourseUseCase
import com.example.ppm_proyecto.domain.usecase.teacher.UpdateSessionUseCase
import com.example.ppm_proyecto.domain.usecase.teacher.UpdateTeacherProfileUseCase
import com.example.ppm_proyecto.domain.usecase.teacher.GetSessionAttendanceUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TeacherModule {
    @Provides
    @Singleton
    fun provideTeacherRepository(impl: TeacherRepositoryImpl): TeacherRepository = impl

    @Provides
    fun provideGetTeacherCoursesUseCase(repo: TeacherRepository): GetTeacherCoursesUseCase = GetTeacherCoursesUseCase(repo)

    @Provides
    fun provideGetCourseSessionsForTeacherUseCase(repo: TeacherRepository): GetCourseSessionsForTeacherUseCase = GetCourseSessionsForTeacherUseCase(repo)

    @Provides
    fun provideCreateCourseUseCase(repo: TeacherRepository): CreateCourseUseCase = CreateCourseUseCase(repo)

    @Provides
    fun provideUpdateCourseUseCase(repo: TeacherRepository): UpdateCourseUseCase = UpdateCourseUseCase(repo)

    @Provides
    fun provideDeleteCourseUseCase(repo: TeacherRepository): DeleteCourseUseCase = DeleteCourseUseCase(repo)

    @Provides
    fun provideCreateSessionUseCase(repo: TeacherRepository): CreateSessionUseCase = CreateSessionUseCase(repo)

    @Provides
    fun provideUpdateSessionUseCase(repo: TeacherRepository): UpdateSessionUseCase = UpdateSessionUseCase(repo)

    @Provides
    fun provideDeleteSessionUseCase(repo: TeacherRepository): DeleteSessionUseCase = DeleteSessionUseCase(repo)

    @Provides
    fun provideMarkStudentAttendanceUseCase(repo: TeacherRepository): MarkStudentAttendanceUseCase = MarkStudentAttendanceUseCase(repo)

    @Provides
    fun provideGetSessionAttendanceUseCase(repo: TeacherRepository): GetSessionAttendanceUseCase = GetSessionAttendanceUseCase(repo)

    @Provides
    fun provideGetTeacherProfileUseCase(repo: TeacherRepository): GetTeacherProfileUseCase = GetTeacherProfileUseCase(repo)

    @Provides
    fun provideUpdateTeacherProfileUseCase(repo: TeacherRepository): UpdateTeacherProfileUseCase = UpdateTeacherProfileUseCase(repo)

    @Provides
    fun provideGetCourseStudentsUseCase(repo: TeacherRepository): GetCourseStudentsUseCase = GetCourseStudentsUseCase(repo)
}
