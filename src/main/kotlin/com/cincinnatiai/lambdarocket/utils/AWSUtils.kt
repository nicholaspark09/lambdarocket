package com.cincinnatiai.lambdarocket.utils

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent
import com.google.gson.Gson

fun APIGatewayProxyRequestEvent.isUserLoggedIn() = this.getIdentityId().isNotEmpty()

fun <T> APIGatewayProxyResponseEvent.createSuccessResponse(data: T? = null, message: String = "", gson: Gson = Gson()) =
    this.apply {
        statusCode = 200
        body = when {
            data != null -> gson.toJson(data)
            else -> message
        }
    }

fun APIGatewayProxyResponseEvent.createErrorResponse(
    statusCode: Int = 500,
    message: String = "Error processing request"
) = this.apply {
    this.statusCode = statusCode
    body = message
}

fun APIGatewayProxyResponseEvent.notLoggedInResponse(statusCode: Int = 401, message: String = "Must be logged in") =
    this.apply {
        this.statusCode = statusCode
        body = message
    }

fun APIGatewayProxyRequestEvent.getIdentityId(): String {
    lateinit var id: String
    try {
        val regex =
            """sub=([a-zA-Z0-9\-]+)""".toRegex().find(this.requestContext.authorizer["claims"]?.toString() ?: "")
        regex?.groups?.firstOrNull()?.value.let { foundText ->
            id = foundText?.replace("sub=", "") ?: ""
        }
    } catch (e: Throwable) {
        id = ""
    }
    return id
}

fun APIGatewayProxyRequestEvent.getEmail(): String {
    var email = ""
    val regex = """email=([a-zA-Z0-9\-]+)""".toRegex().find(this.requestContext.authorizer["claims"]?.toString() ?: "")
    regex?.groups?.firstOrNull()?.value.let { foundText ->
        email = foundText?.replace("email=", "") ?: ""
    }
    return email
}

fun APIGatewayProxyRequestEvent.getUsername(): String {
    var email = ""
    val regex = """cognito:username=([a-zA-Z0-9\-]+)""".toRegex()
        .find(this.requestContext.authorizer["claims"]?.toString() ?: "")
    regex?.groups?.firstOrNull()?.value.let { foundText ->
        email = foundText?.replace("cognito:username=", "") ?: ""
    }
    return email
}