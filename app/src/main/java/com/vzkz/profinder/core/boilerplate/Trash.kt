package com.vzkz.profinder.core.boilerplate

import com.vzkz.profinder.domain.model.Actors
import com.vzkz.profinder.domain.model.ActorModel
import com.vzkz.profinder.domain.model.Categories
import com.vzkz.profinder.domain.model.ProfState
import com.vzkz.profinder.domain.model.Professions
import com.vzkz.profinder.domain.model.ServiceModel


val USERMODELFORTESTS: ActorModel = ActorModel(
    uid = "mUxyPt1uC4hOMcB6jRCcr3upTZN2",
    nickname = "vzkz_88",
    firstname = "Jaime",
    lastname = "Vázquez Martín",
    actor = Actors.User
)
val PROFESSIONALMODELFORTESTS: ActorModel = ActorModel(
    uid = "mUxyPt1uC4hOMcB6jRCcr3upTZN2",
    nickname = "vzkz_88",
    firstname = "Jaime",
    lastname = "Vázquez Martín",
    description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. ",
    actor = Actors.Professional,
    profession = Professions.Plumber,
    state = ProfState.Active
)

val SERVICEMODELFORTEST = ServiceModel(
    sid = "-1",
    uid = "vo4008dpUlOulCuPQUDFpLEtjbR2",
    name = "Plumbing",
    isActive = true,
    category = Categories.Household,
    servDescription = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, consectetur adipiscing elit.".repeat(4)
)
val SERVICEMODEL1FORTEST = ServiceModel(
    sid = "-1",
    uid = "vo4008dpUlOulCuPQUDFpLEtjbR2",
    name = "Tap check",
    isActive = true,
    category = Categories.Household,
    servDescription = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, consectetur adipiscing elit.".repeat(4)
)
val SERVICEMODEL2FORTEST = ServiceModel(
    sid = "-1",
    uid = "vo4008dpUlOulCuPQUDFpLEtjbR2",
    name = "Toilet fix",
    isActive = false,
    category = Categories.Household,
    servDescription = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, consectetur adipiscing elit.".repeat(4)
)
val SERVICEMODEL3FORTEST = ServiceModel(
    sid = "-1",
    uid = "vo4008dpUlOulCuPQUDFpLEtjbR2",
    name = "Boiler installation",
    isActive = false,
    category = Categories.Household,
    servDescription = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, consectetur adipiscing elit.".repeat(4)
)

val SERVICELISTFORTEST = listOf(SERVICEMODELFORTEST, SERVICEMODEL1FORTEST)


/*
Column {
    Text(
        text = "Functionality not yet developed...",
        style = MaterialTheme.typography.bodyLarge
    )
}
            */
