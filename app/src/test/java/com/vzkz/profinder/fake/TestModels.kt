package com.vzkz.profinder.fake

import com.vzkz.profinder.domain.model.ActorModel
import com.vzkz.profinder.domain.model.Actors
import com.vzkz.profinder.domain.model.Constants.DESCRIPTION
import com.vzkz.profinder.domain.model.Constants.FIRSTNAME
import com.vzkz.profinder.domain.model.Constants.IS_USER
import com.vzkz.profinder.domain.model.Constants.LASTNAME
import com.vzkz.profinder.domain.model.Constants.NICKNAME
import com.vzkz.profinder.domain.model.Constants.PROFESSION
import com.vzkz.profinder.domain.model.Constants.STATE
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

val userDocument2_test = mapOf(
    Pair(DESCRIPTION, "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. "),
    Pair(FIRSTNAME, "Jaime"),
    Pair(IS_USER, false),
    Pair(LASTNAME, "Vázquez Martín"),
    Pair(NICKNAME, "vzkz_88"),
    Pair(PROFESSION, "Plumber"),
    Pair(STATE, "Active")
)