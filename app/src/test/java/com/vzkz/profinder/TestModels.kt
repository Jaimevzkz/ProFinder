package com.vzkz.profinder

import com.vzkz.profinder.domain.model.ActorModel
import com.vzkz.profinder.domain.model.Actors
import com.vzkz.profinder.core.Constants.CATEGORY
import com.vzkz.profinder.core.Constants.DESCRIPTION
import com.vzkz.profinder.core.Constants.FIRSTNAME
import com.vzkz.profinder.core.Constants.IS_ACTIVE
import com.vzkz.profinder.core.Constants.IS_USER
import com.vzkz.profinder.core.Constants.LASTNAME
import com.vzkz.profinder.core.Constants.NAME
import com.vzkz.profinder.core.Constants.NICKNAME
import com.vzkz.profinder.core.Constants.PRICE
import com.vzkz.profinder.core.Constants.PROFESSION
import com.vzkz.profinder.core.Constants.SERV_DESCRIPTION
import com.vzkz.profinder.core.Constants.STATE
import com.vzkz.profinder.core.Constants.UID
import com.vzkz.profinder.domain.model.ProfState
import com.vzkz.profinder.domain.model.Professions


val user1_test: ActorModel = ActorModel(
    uid = "mUxyPt1uC4hOMcB6jRCcr3upTZN2",
    nickname = "vzkz_88",
    firstname = "Jaime",
    lastname = "Vázquez Martín",
    actor = Actors.User
)

val userDocument1_test = mapOf(
    Pair(FIRSTNAME, "Jaime"),
    Pair(IS_USER, true),
    Pair(LASTNAME, "Vázquez Martín"),
    Pair(NICKNAME, "vzkz_88")
)

val prof2_test: ActorModel = ActorModel(
    uid = "mUxyPt1uC4hOMcB6jRCcr3upTZN2",
    nickname = "vzkz_88",
    firstname = "Jaime",
    lastname = "Vázquez Martín",
    description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. ",
    actor = Actors.Professional,
    profession = Professions.Plumber,
    state = ProfState.Active
)

val profDocument2_test = mapOf(
    Pair(DESCRIPTION, "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. "),
    Pair(FIRSTNAME, "Jaime"),
    Pair(IS_USER, false),
    Pair(LASTNAME, "Vázquez Martín"),
    Pair(NICKNAME, "vzkz_88"),
    Pair(PROFESSION, "Plumber"),
    Pair(STATE, "Active")
)

val serviceDocument_test = mapOf(
    Pair(CATEGORY, "Beauty"),
    Pair(UID, "asdl2345jflkj4"),
    Pair(NAME, "Hair cut"),
    Pair(IS_ACTIVE, true),
    Pair(SERV_DESCRIPTION, "lorem ipsum"),
    Pair(PRICE, 20.0)
)






















