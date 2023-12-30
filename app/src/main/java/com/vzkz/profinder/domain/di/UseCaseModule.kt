package com.vzkz.profinder.domain.di

import com.vzkz.profinder.domain.usecases.GetUidDataStoreUseCase
import com.vzkz.profinder.domain.usecases.GetUidDataStoreUseCaseImpl
import com.vzkz.profinder.domain.usecases.GetUserUseCase
import com.vzkz.profinder.domain.usecases.GetUserUseCaseImpl
import com.vzkz.profinder.domain.usecases.LoginUseCase
import com.vzkz.profinder.domain.usecases.LoginUseCaseImpl
import com.vzkz.profinder.domain.usecases.LogoutUseCase
import com.vzkz.profinder.domain.usecases.LogoutUseCaseImpl
import com.vzkz.profinder.domain.usecases.ModifyUserDataUseCase
import com.vzkz.profinder.domain.usecases.ModifyUserDataUseCaseImpl
import com.vzkz.profinder.domain.usecases.SaveUidDataStoreUseCase
import com.vzkz.profinder.domain.usecases.SaveUidDataStoreUseCaseImpl
import com.vzkz.profinder.domain.usecases.SignUpUseCase
import com.vzkz.profinder.domain.usecases.SignUpUseCaseImpl
import com.vzkz.profinder.domain.usecases.ThemeDSUseCase
import com.vzkz.profinder.domain.usecases.ThemeDSUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Suppress("unused")
@Module
@InstallIn(SingletonComponent::class)
abstract class UseCaseModule {
    @Binds
    abstract fun bindGetUidDataStoreUseCase(impl: GetUidDataStoreUseCaseImpl): GetUidDataStoreUseCase
    @Binds
    abstract fun bindGetUserUseCase(impl: GetUserUseCaseImpl): GetUserUseCase
    @Binds
    abstract fun bindLoginUseCase(impl: LoginUseCaseImpl): LoginUseCase
    @Binds
    abstract fun bindLogoutUseCase(impl: LogoutUseCaseImpl): LogoutUseCase
    @Binds
    abstract fun bindModifyUserDataUseCase(impl: ModifyUserDataUseCaseImpl): ModifyUserDataUseCase
    @Binds
    abstract fun bindSaveUidDataStoreUseCase(impl: SaveUidDataStoreUseCaseImpl): SaveUidDataStoreUseCase
    @Binds
    abstract fun bindSignUpUseCase(impl: SignUpUseCaseImpl): SignUpUseCase
    @Binds
    abstract fun bindThemeDSUseCase(impl: ThemeDSUseCaseImpl): ThemeDSUseCase

}