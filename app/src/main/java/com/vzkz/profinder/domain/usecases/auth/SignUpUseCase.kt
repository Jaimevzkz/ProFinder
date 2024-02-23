package com.vzkz.profinder.domain.usecases.auth

import com.vzkz.profinder.domain.Repository
import com.vzkz.profinder.domain.model.Actors
import com.vzkz.profinder.domain.model.ActorModel
import com.vzkz.profinder.domain.model.Professions
import javax.inject.Inject

interface SignUpUseCase {
    suspend operator fun invoke(
        email: String,
        password: String,
        nickname: String,
        firstname: String,
        lastname: String,
        actor: Actors,
        profession: Professions?
    ): Result<ActorModel>
}

class SignUpUseCaseImpl @Inject constructor(private val repository: Repository) : SignUpUseCase {
    override suspend operator fun invoke(
        email: String,
        password: String,
        nickname: String,
        firstname: String,
        lastname: String,
        actor: Actors,
        profession: Professions?
    ): Result<ActorModel> {
        return repository.signUp(
            email = email,
            password = password,
            nickname = nickname,
            firstname = firstname,
            lastname = lastname,
            actor = actor,
            profession = profession
        )
    }
}