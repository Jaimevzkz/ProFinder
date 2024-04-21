package com.vzkz.profinder.domain.di

import com.vzkz.profinder.domain.usecases.FlushSingletonsUseCase
import com.vzkz.profinder.domain.usecases.FlushSingletonsUseCaseImpl
import com.vzkz.profinder.domain.usecases.ThemeDSUseCase
import com.vzkz.profinder.domain.usecases.ThemeDSUseCaseImpl
import com.vzkz.profinder.domain.usecases.auth.LoginUseCase
import com.vzkz.profinder.domain.usecases.auth.LoginUseCaseImpl
import com.vzkz.profinder.domain.usecases.auth.LogoutUseCase
import com.vzkz.profinder.domain.usecases.auth.LogoutUseCaseImpl
import com.vzkz.profinder.domain.usecases.auth.SignUpUseCase
import com.vzkz.profinder.domain.usecases.auth.SignUpUseCaseImpl
import com.vzkz.profinder.domain.usecases.chat.AddNewMessageUseCase
import com.vzkz.profinder.domain.usecases.chat.AddNewMessageUseCaseImpl
import com.vzkz.profinder.domain.usecases.chat.GetIndChatsUseCase
import com.vzkz.profinder.domain.usecases.chat.GetIndChatsUseCaseImpl
import com.vzkz.profinder.domain.usecases.chat.GetRecentChatsUseCase
import com.vzkz.profinder.domain.usecases.chat.GetRecentChatsUseCaseImpl
import com.vzkz.profinder.domain.usecases.chat.GetUnreadMessageAndOwnerUseCase
import com.vzkz.profinder.domain.usecases.chat.GetUnreadMessageAndOwnerUseCaseImpl
import com.vzkz.profinder.domain.usecases.chat.OpenRecentChatsUseCase
import com.vzkz.profinder.domain.usecases.chat.OpenRecentChatsUseCaseImpl
import com.vzkz.profinder.domain.usecases.jobs.AddJobOrRequestsUseCase
import com.vzkz.profinder.domain.usecases.jobs.AddJobOrJobOrRequestsUseCaseImpl
import com.vzkz.profinder.domain.usecases.jobs.CheckExistingRequestUseCase
import com.vzkz.profinder.domain.usecases.jobs.CheckExistingRequestUseCaseImpl
import com.vzkz.profinder.domain.usecases.jobs.DeleteJobOrRequestUseCase
import com.vzkz.profinder.domain.usecases.jobs.DeleteJobOrRequestUseCaseImpl
import com.vzkz.profinder.domain.usecases.jobs.GetJobOrRequestsUseCase
import com.vzkz.profinder.domain.usecases.jobs.GetJobOrRequestsUseCaseImpl
import com.vzkz.profinder.domain.usecases.jobs.RateProfUseCase
import com.vzkz.profinder.domain.usecases.jobs.RateProfUseCaseUseCaseImpl
import com.vzkz.profinder.domain.usecases.jobs.RateUserUseCase
import com.vzkz.profinder.domain.usecases.jobs.RateUserUseCaseImpl
import com.vzkz.profinder.domain.usecases.jobs.TurnJobIntoRequestUseCase
import com.vzkz.profinder.domain.usecases.jobs.TurnJobIntoRequestUseCaseImpl
import com.vzkz.profinder.domain.usecases.location.GetLocationUseCase
import com.vzkz.profinder.domain.usecases.location.GetLocationUseCaseImpl
import com.vzkz.profinder.domain.usecases.location.GetOtherLocationsUseCase
import com.vzkz.profinder.domain.usecases.location.GetOtherLocationsUseCaseImpl
import com.vzkz.profinder.domain.usecases.services.ChangeServiceActivityUseCase
import com.vzkz.profinder.domain.usecases.services.ChangeServiceActivityUseCaseImpl
import com.vzkz.profinder.domain.usecases.services.DeleteServiceUseCase
import com.vzkz.profinder.domain.usecases.services.DeleteServiceUseCaseImpl
import com.vzkz.profinder.domain.usecases.services.GetActiveServiceListUseCase
import com.vzkz.profinder.domain.usecases.services.GetActiveServiceListUseCaseImpl
import com.vzkz.profinder.domain.usecases.services.GetServiceListUseCase
import com.vzkz.profinder.domain.usecases.services.GetServiceListUseCaseImpl
import com.vzkz.profinder.domain.usecases.services.InsertServiceUseCase
import com.vzkz.profinder.domain.usecases.services.InsertServiceUseCaseImpl
import com.vzkz.profinder.domain.usecases.user.ChangeStateUseCase
import com.vzkz.profinder.domain.usecases.user.ChangeStateUseCaseImpl
import com.vzkz.profinder.domain.usecases.user.FavouriteListUseCase
import com.vzkz.profinder.domain.usecases.user.FavouriteListUseCaseImpl
import com.vzkz.profinder.domain.usecases.user.GetUidDataStoreUseCase
import com.vzkz.profinder.domain.usecases.user.GetUidDataStoreUseCaseImpl
import com.vzkz.profinder.domain.usecases.user.GetUserUseCase
import com.vzkz.profinder.domain.usecases.user.GetUserUseCaseImpl
import com.vzkz.profinder.domain.usecases.user.ModifyUserDataUseCase
import com.vzkz.profinder.domain.usecases.user.ModifyUserDataUseCaseImpl
import com.vzkz.profinder.domain.usecases.user.SaveUidDataStoreUseCase
import com.vzkz.profinder.domain.usecases.user.SaveUidDataStoreUseCaseImpl
import com.vzkz.profinder.domain.usecases.user.UploadPhotoUseCase
import com.vzkz.profinder.domain.usecases.user.UploadPhotoUseCaseImpl
import com.vzkz.profinder.domain.usecases.user.UserProfileToSeeUseCase
import com.vzkz.profinder.domain.usecases.user.UserProfileToSeeUseCaseImpl
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
    @Binds
    abstract fun bindGetServiceListUseCase(impl: GetServiceListUseCaseImpl): GetServiceListUseCase
    @Binds
    abstract fun bindGetActiveServiceListUseCase(impl: GetActiveServiceListUseCaseImpl): GetActiveServiceListUseCase
    @Binds
    abstract fun bindInsertServiceUseCase(impl: InsertServiceUseCaseImpl): InsertServiceUseCase
    @Binds
    abstract fun bindDeleteServiceUseCase(impl: DeleteServiceUseCaseImpl): DeleteServiceUseCase
    @Binds
    abstract fun bindChangeStateUseCase(impl: ChangeStateUseCaseImpl): ChangeStateUseCase
    @Binds
    abstract fun bindChangeServiceActivityUseCase(impl: ChangeServiceActivityUseCaseImpl): ChangeServiceActivityUseCase
    @Binds
    abstract fun bindUserProfileToSeeUseCase(impl: UserProfileToSeeUseCaseImpl): UserProfileToSeeUseCase
    @Binds
    abstract fun bindFavouriteListUseCase(impl: FavouriteListUseCaseImpl): FavouriteListUseCase
    @Binds
    abstract fun bindUploadPhotoUseCase(impl: UploadPhotoUseCaseImpl): UploadPhotoUseCase
    @Binds
    abstract fun bindFlushSingletonsUseCase(impl: FlushSingletonsUseCaseImpl): FlushSingletonsUseCase
    @Binds
    abstract fun bindGetRecentChatsUseCase(impl: GetRecentChatsUseCaseImpl): GetRecentChatsUseCase
    @Binds
    abstract fun bindGetIndChatsUseCase(impl: GetIndChatsUseCaseImpl): GetIndChatsUseCase
    @Binds
    abstract fun bindAddNewMessageUseCase(impl: AddNewMessageUseCaseImpl): AddNewMessageUseCase
    @Binds
    abstract fun bindOpenRecentChatsUseCase(impl: OpenRecentChatsUseCaseImpl): OpenRecentChatsUseCase
    @Binds
    abstract fun bindGetUnreadMessageAndOwnerUseCase(impl: GetUnreadMessageAndOwnerUseCaseImpl): GetUnreadMessageAndOwnerUseCase
    @Binds
    abstract fun bindGetRequestsUseCase(impl: GetJobOrRequestsUseCaseImpl): GetJobOrRequestsUseCase
    @Binds
    abstract fun bindAddRequestsUseCase(impl: AddJobOrJobOrRequestsUseCaseImpl): AddJobOrRequestsUseCase
    @Binds
    abstract fun bindCheckExistingRequestUseCase(impl: CheckExistingRequestUseCaseImpl): CheckExistingRequestUseCase
    @Binds
    abstract fun bindDeleteRequestUseCase(impl: DeleteJobOrRequestUseCaseImpl): DeleteJobOrRequestUseCase
    @Binds
    abstract fun bindTurnJobIntoRequestUseCase(impl: TurnJobIntoRequestUseCaseImpl): TurnJobIntoRequestUseCase
    @Binds
    abstract fun bindRateJobUseCase(impl: RateProfUseCaseUseCaseImpl): RateProfUseCase
    @Binds
    abstract fun bindGetLocationUseCase(impl: GetLocationUseCaseImpl): GetLocationUseCase
    @Binds
    abstract fun bindGetLocationsUseCase(impl: GetOtherLocationsUseCaseImpl): GetOtherLocationsUseCase
    @Binds
    abstract fun bindRateUserUseCase(impl: RateUserUseCaseImpl): RateUserUseCase
}