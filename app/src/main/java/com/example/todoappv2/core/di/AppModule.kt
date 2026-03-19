package com.example.todoappv2.core.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.example.todoappv2.core.notification.TaskReminderSchedule
import com.example.todoappv2.data.local.dao.SubjectDao
import com.example.todoappv2.data.local.dao.TaskDao
import com.example.todoappv2.data.local.database.AppDataBase
import com.example.todoappv2.data.repository.AppRepository
import com.example.todoappv2.data.repository.AppRepositoryImplementation
import com.example.todoappv2.data.repository.AuthRepository
import com.example.todoappv2.data.repository.AuthRepositoryImplementation
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create(
            produceFile = { context.preferencesDataStoreFile("settings") }
        )
    }

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDataBase {
        return AppDataBase.getDatabase(context)
    }

    @Provides
    fun provideSubjectDao(database: AppDataBase): SubjectDao = database.subjectDao()

    @Provides
    fun provideTaskDao(database: AppDataBase): TaskDao = database.taskDao()

    @Provides
    @Singleton
    fun provideAppRepository(
        subjectDao: SubjectDao,
        taskDao: TaskDao
    ): AppRepository = AppRepositoryImplementation(subjectDao, taskDao)

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideAuthRepository(firebaseAuth: FirebaseAuth): AuthRepository = 
        AuthRepositoryImplementation(firebaseAuth)
    @Provides
    @Singleton
    fun provideTaskReminderScheduler(
        @ApplicationContext context: Context
    ): TaskReminderSchedule{
        return TaskReminderSchedule(context)
    }
}
