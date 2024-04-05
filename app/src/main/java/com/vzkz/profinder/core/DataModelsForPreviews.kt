package com.vzkz.profinder.core

import com.vzkz.profinder.domain.model.ActorModel
import com.vzkz.profinder.domain.model.Actors
import com.vzkz.profinder.domain.model.Categories
import com.vzkz.profinder.domain.model.ChatListItemModel
import com.vzkz.profinder.domain.model.ChatMsgModel
import com.vzkz.profinder.domain.model.ProfState
import com.vzkz.profinder.domain.model.Professions
import com.vzkz.profinder.domain.model.RequestModel
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

val PROFFESIONALLISTFORTEST =
    listOf(PROFESSIONALMODELFORTESTS, PROFESSIONALMODELFORTESTS, PROFESSIONALMODELFORTESTS)

val REQUESTMODELFORTESTS: RequestModel = RequestModel(
    rid = "123",
    clientNickname = "jaimee1",
    clientUid = "123",
    serviceId = "123",
    serviceName = "Plumbing",
    price = 12.3
)
val REQUESTLISTFORTEST = listOf(REQUESTMODELFORTESTS, REQUESTMODELFORTESTS, REQUESTMODELFORTESTS, REQUESTMODELFORTESTS)

val SERVICEMODELFORTEST = ServiceModel(
    sid = "-1",
    uid = "vo4008dpUlOulCuPQUDFpLEtjbR2",
    name = "Plumbing",
    isActive = true,
    category = Categories.Household,
    servDescription = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, consectetur adipiscing elit.".repeat(
        4
    ),
    owner = PROFESSIONALMODELFORTESTS,
    price = 18.0
)
val SERVICEMODEL1FORTEST = ServiceModel(
    sid = "-1",
    uid = "vo4008dpUlOulCuPQUDFpLEtjbR2",
    name = "Tap check",
    isActive = true,
    category = Categories.Household,
    servDescription = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, consectetur adipiscing elit.".repeat(
        4
    ),
    owner = PROFESSIONALMODELFORTESTS,
    price = 12.0
)
val SERVICEMODEL2FORTEST = ServiceModel(
    sid = "-1",
    uid = "vo4008dpUlOulCuPQUDFpLEtjbR2",
    name = "Toilet fix",
    isActive = false,
    category = Categories.Household,
    servDescription = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, consectetur adipiscing elit.".repeat(
        4
    ),
    owner = PROFESSIONALMODELFORTESTS,
    price = 16.5
)
val SERVICEMODEL3FORTEST = ServiceModel(
    sid = "-1",
    uid = "vo4008dpUlOulCuPQUDFpLEtjbR2",
    name = "Boiler installation",
    isActive = false,
    category = Categories.Household,
    servDescription = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, consectetur adipiscing elit.".repeat(
        4
    ),
    owner = PROFESSIONALMODELFORTESTS,
    price = 9.99
)

val SERVICELISTFORTEST = listOf(SERVICEMODELFORTEST, SERVICEMODEL1FORTEST)


val CHATLISTITEMFORTEST = ChatListItemModel(
    nickname = "larbyysbarber",
    profilePhoto = null,
    timestamp = 1710498348086,
    lastMsg = "Hello, how are you? I was wondering whether you could help me with something.",
    unreadMsgNumber = 2,
    lastMsgUid = "123",
    chatId = "1234",
    uid = "vo4008dpUlOulCuPQUDFpLEtjbR2"
)

val INDIVIDUALCHATITEMFORTEST = listOf(
    ChatMsgModel(
        msgId = "12",
        msg = "Hello, how are you? I was wondering whether you could help me with something.",
        timestamp = 1710498348086,
        isMine = false,
    ),
    ChatMsgModel(
        msgId = "12",
        msg = "Hello, how are you? I was wondering whether you could help me with something.",
        timestamp = 1710498348086,
        isMine = false
    ),
    ChatMsgModel(
        msgId = "12",
        msg = "Hello, how are you? I was wondering whether you could help me with something.",
        timestamp = 1710498348086,
        isMine = true,
    ),
    ChatMsgModel(
        msgId = "12",
        msg = "Hello, how are you? I was wondering whether you could help me with something.",
        timestamp = 1710498348086,
        isMine = true,
    ),
    ChatMsgModel(
        msgId = "12",
        msg = "Hello, how are you? I was wondering whether you could help me with something.",
        timestamp = 1710498348086,
        isMine = false,
    ),
    ChatMsgModel(
        msgId = "12",
        msg = "Hello, how are you? I was wondering whether you could help me with something.",
        timestamp = 1710498348086,
        isMine = true,
    ),
    ChatMsgModel(
        msgId = "12",
        msg = "Hello, how are you? I was wondering whether you could help me with something.",
        timestamp = 1710498348086,
        isMine = false,
    ),
    ChatMsgModel(
        msgId = "12",
        msg = "Hello, how are you? I was wondering whether you could help me with something.",
        timestamp = 1710498348086,
        isMine = false,
    ),
    ChatMsgModel(
        msgId = "12",
        msg = "Hello, how are you? I was wondering whether you could help me with something.",
        timestamp = 1710498348086,
        isMine = true,
    ),
)

//val RECENTCHATSDTOFORTEST = RecentChatDto(
//    profilePhoto = null,
//    nickname = "luisito17",
//    timestamp = 1710494349086,
//    lastMsg = "This realtime thing working!",
//    unreadMsgNumber = 1,
//    isLastMessageMine = false
//)

//DB Fillling
object DBDATA_POBLATION_ACTORS {
    data class PROF_TO_INSERT(
        val email: String,
        val password: String = "1234Qwerty",
        val nickname: String,
        val firstname: String,
        val lastname: String,
        val description: String?,
        val profession: Professions?,
        val state: ProfState?
    )

    val DBDATA_PROF1: ActorModel = ActorModel(
        uid = "DU4rMbfJafPOVPpnkNP3o5MgrlX2",
        nickname = "fede_lopez1",
        firstname = "Federico",
        lastname = "López",
        description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. ",
        actor = Actors.Professional,
        profession = Professions.Electrician,
        state = ProfState.Working
    )
    val DBDATA_PROF1_TOINSERT = PROF_TO_INSERT(
        email = "example@gmail.com",
        nickname = DBDATA_PROF1.nickname,
        firstname = DBDATA_PROF1.firstname,
        lastname = DBDATA_PROF1.lastname,
        description = DBDATA_PROF1.description,
        profession = DBDATA_PROF1.profession,
        state = DBDATA_PROF1.state
    )

    val DBDATA_PROF2: ActorModel = ActorModel(
        uid = "dEs1iAJYiJRcg9iNZk9yUpf5g7D3",
        nickname = "larbyysbarber",
        firstname = "Larby",
        lastname = "Mubbarahk",
        description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. ",
        actor = Actors.Professional,
        profession = Professions.Hairdresser,
        state = ProfState.Active
    )
    val DBDATA_PROF2_TOINSERT = PROF_TO_INSERT(
        email = "example+1@gmail.com",
        nickname = DBDATA_PROF2.nickname,
        firstname = DBDATA_PROF2.firstname,
        lastname = DBDATA_PROF2.lastname,
        description = DBDATA_PROF2.description,
        profession = DBDATA_PROF2.profession,
        state = DBDATA_PROF2.state
    )

    val DBDATA_PROF3: ActorModel = ActorModel(
        uid = "s1BTZkVpHZQ9TfrOhseltZHE1CF2",
        nickname = "saidbarbershop",
        firstname = "Said",
        lastname = "Tabbarahk",
        description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. ",
        actor = Actors.Professional,
        profession = Professions.Hairdresser,
        state = ProfState.Inactive
    )
    val DBDATA_PROF3_TOINSERT = PROF_TO_INSERT(
        email = "example+2@gmail.com",
        nickname = DBDATA_PROF3.nickname,
        firstname = DBDATA_PROF3.firstname,
        lastname = DBDATA_PROF3.lastname,
        description = DBDATA_PROF3.description,
        profession = DBDATA_PROF3.profession,
        state = DBDATA_PROF3.state
    )

    val DBDATA_LISTPROF = listOf(DBDATA_PROF1, DBDATA_PROF2, DBDATA_PROF3)
    val DBDATA_LISTPROF_TOINSERT =
        listOf(DBDATA_PROF1_TOINSERT, DBDATA_PROF2_TOINSERT, DBDATA_PROF3_TOINSERT)


    data class USER_TO_INSERT(
        val email: String,
        val password: String = "1234Qwerty",
        val nickname: String,
        val firstname: String,
        val lastname: String,
        val description: String?
    )

    val DBDATA_USER1: ActorModel = ActorModel(
        uid = "LIUWKzIM82eSfzldJYWvUjdoaTj1",
        nickname = "javii02",
        firstname = "Javier",
        lastname = "Romero",
        actor = Actors.User
    )
    val DBDATA_USER1_TOINSERT = USER_TO_INSERT(
        email = "example+3@gmail.com",
        nickname = DBDATA_USER1.nickname,
        firstname = DBDATA_USER1.firstname,
        lastname = DBDATA_USER1.lastname,
        description = DBDATA_USER1.description,
    )

    val DBDATA_USER2: ActorModel = ActorModel(
        uid = "UPyAoFuyZqVUjItALfQS6CNbaku2",
        nickname = "_crecente",
        firstname = "Pablo",
        lastname = "Crecente Maseda",
        actor = Actors.User
    )
    val DBDATA_USER2_TOINSERT = USER_TO_INSERT(
        email = "example+4@gmail.com",
        nickname = DBDATA_USER2.nickname,
        firstname = DBDATA_USER2.firstname,
        lastname = DBDATA_USER2.lastname,
        description = DBDATA_USER2.description,
    )

    val DBDATA_LISTUSER = listOf(DBDATA_USER1, DBDATA_USER2)
    val DBDATA_LISTUSER_TOINSERT = listOf(DBDATA_USER1_TOINSERT, DBDATA_USER2_TOINSERT)

}

object DBDATA_POBLATION_SERVICES {
    val DBDATA_SERVICE1: ServiceModel = ServiceModel(
        sid = "-1",
        uid = "DU4rMbfJafPOVPpnkNP3o5MgrlX2",
        name = "Cable installation",
        isActive = true,
        category = Categories.Household,
        servDescription = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, consectetur adipiscing elit.".repeat(
            4
        ),
        owner = DBDATA_POBLATION_ACTORS.DBDATA_PROF1,
        price = 18.0
    )

    val DBDATA_SERVICE2: ServiceModel = ServiceModel(
        sid = "-1",
        uid = "DU4rMbfJafPOVPpnkNP3o5MgrlX2",
        name = "Wifi check",
        isActive = false,
        category = Categories.Household,
        servDescription = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, consectetur adipiscing elit.".repeat(
            4
        ),
        owner = DBDATA_POBLATION_ACTORS.DBDATA_PROF1,
        price = 12.0
    )

    val DBDATA_SERVICE3: ServiceModel = ServiceModel(
        sid = "-1",
        uid = "dEs1iAJYiJRcg9iNZk9yUpf5g7D3",
        name = "Coloured hair dyeing",
        isActive = false,
        category = Categories.Beauty,
        servDescription = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, consectetur adipiscing elit.".repeat(
            4
        ),
        owner = DBDATA_POBLATION_ACTORS.DBDATA_PROF2,
        price = 16.5
    )
    val DBDATA_SERVICE4: ServiceModel = ServiceModel(
        sid = "-1",
        uid = "dEs1iAJYiJRcg9iNZk9yUpf5g7D3",
        name = "Basic haircut",
        isActive = true,
        category = Categories.Beauty,
        servDescription = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, consectetur adipiscing elit.".repeat(
            4
        ),
        owner = DBDATA_POBLATION_ACTORS.DBDATA_PROF2,
        price = 12.0
    )

    val DBDATA_SERVICE5: ServiceModel = ServiceModel(
        sid = "-1",
        uid = "s1BTZkVpHZQ9TfrOhseltZHE1CF2",
        name = "Facial shaving",
        isActive = true,
        category = Categories.Beauty,
        servDescription = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, consectetur adipiscing elit.".repeat(
            4
        ),
        owner = DBDATA_POBLATION_ACTORS.DBDATA_PROF3,
        price = 9.0
    )

    val DBDATA_SERVICE6: ServiceModel = ServiceModel(
        sid = "-1",
        uid = "s1BTZkVpHZQ9TfrOhseltZHE1CF2",
        name = "head shaving",
        isActive = false,
        category = Categories.Beauty,
        servDescription = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, consectetur adipiscing elit.".repeat(
            4
        ),
        owner = DBDATA_POBLATION_ACTORS.DBDATA_PROF3,
        price = 10.8
    )

    val SERVICESTOINSERT = listOf(
        DBDATA_SERVICE1,
        DBDATA_SERVICE2,
        DBDATA_SERVICE3,
        DBDATA_SERVICE4,
        DBDATA_SERVICE5,
        DBDATA_SERVICE6
    )
}
