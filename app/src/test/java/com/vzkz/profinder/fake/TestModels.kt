package com.vzkz.profinder.fake

import com.vzkz.profinder.domain.model.ActorModel
import com.vzkz.profinder.domain.model.Actors
import com.vzkz.profinder.domain.model.ProfState
import com.vzkz.profinder.domain.model.Professions


val user1_test: ActorModel = ActorModel(
    uid = "mUxyPt1uC4hOMcB6jRCcr3upTZN2",
    nickname = "vzkz_88",
    firstname = "Jaime",
    lastname = "Vázquez Martín",
    actor = Actors.User
)
val user2_test: ActorModel = ActorModel(
    uid = "mUxyPt1uC4hOMcB6jRCcr3upTZN2",
    nickname = "vzkz_88",
    firstname = "Jaime",
    lastname = "Vázquez Martín",
    description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. ",
    actor = Actors.Professional,
    profession = Professions.Plumber,
    state = ProfState.Active
)