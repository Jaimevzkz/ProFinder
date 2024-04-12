package com.vzkz.profinder.domain.usecases.jobs

import com.vzkz.profinder.domain.Repository
import com.vzkz.profinder.domain.model.ServiceModel
import com.vzkz.profinder.domain.usecases.user.GetUserUseCase
import javax.inject.Inject


interface AddJobOrRequestsUseCase {
    suspend operator fun invoke(isRequest: Boolean, serviceModel: ServiceModel)}


class AddJobOrJobOrRequestsUseCaseImpl @Inject constructor(
    private val repository: Repository,
    private val getUserUseCase: GetUserUseCase
) : AddJobOrRequestsUseCase {
    override suspend operator fun invoke(isRequest: Boolean, serviceModel: ServiceModel) {
        val user = getUserUseCase()
        repository.addJobOrRequest(
            isRequest = isRequest,
            profUid = serviceModel.owner.uid,
            profNickname = serviceModel.owner.nickname,
            clientNickname = user.nickname,
            clientId = user.uid,
            serviceName = serviceModel.name,
            serviceId = serviceModel.sid,
            price = serviceModel.price
        )
    }
}