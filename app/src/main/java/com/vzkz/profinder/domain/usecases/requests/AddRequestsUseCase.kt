package com.vzkz.profinder.domain.usecases.requests

import com.vzkz.profinder.domain.Repository
import com.vzkz.profinder.domain.model.ServiceModel
import com.vzkz.profinder.domain.usecases.user.GetUserUseCase
import javax.inject.Inject


interface AddRequestsUseCase {
    suspend operator fun invoke(serviceModel: ServiceModel)
}


class AddRequestsUseCaseImpl @Inject constructor(
    private val repository: Repository,
    private val getUserUseCase: GetUserUseCase
) : AddRequestsUseCase {
    override suspend operator fun invoke(serviceModel: ServiceModel) {
        val user = getUserUseCase()
        repository.addJobRequest(
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