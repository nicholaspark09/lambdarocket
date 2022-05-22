package com.cincinnatiai.lambdarocket.utils

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent
import com.cincinnatiai.lambdarocket.LambdaRocket

fun APIGatewayProxyRequestEvent.isUserLoggedIn() = this.getIdentityId().isNotBlank()

fun <T> APIGatewayProxyResponseEvent.createSuccessResponse(data: T? = null, message: String = "") = this.apply {
    statusCode = 200
    body = when {
        data != null -> LambdaRocket.instance().gson.toJson(data)
        else -> message
    }
}

fun APIGatewayProxyResponseEvent.createErrorResponse(statusCode: Int = 500, message: String = "Error processing request") = this.apply {
    this.statusCode = statusCode
    body = message
}

fun APIGatewayProxyResponseEvent.notLoggedInResponse(statusCode: Int = 401, message: String = "Must be logged in") = this.apply {
    this.statusCode = statusCode
    body = message
}

fun APIGatewayProxyRequestEvent.getIdentityId(): String {
    var id = ""
    val regex = """sub=([a-zA-Z0-9\-]+)""".toRegex().find(this.requestContext.authorizer["claims"]?.toString() ?: "")
    regex?.groups?.firstOrNull()?.value.let { foundText ->
        id = foundText?.replace("sub=", "") ?: ""
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