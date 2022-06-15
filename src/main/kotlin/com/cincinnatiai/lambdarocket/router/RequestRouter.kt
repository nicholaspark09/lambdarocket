package com.cincinnatiai.lambdarocket.router

import com.amazonaws.services.lambda.runtime.LambdaLogger
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent
import com.cincinnatiai.lambdarocket.controller.RouteControllerContract
import com.cincinnatiai.lambdarocket.utils.EMPTY_STRING

interface RequestRouterContract {

    fun handleAllRoutes(input: APIGatewayProxyRequestEvent?): APIGatewayProxyResponseEvent
}

class RequestRouter(
    private val logger: LambdaLogger? = null,
    private val controllers: List<RouteControllerContract>
) : RequestRouterContract {

    override fun handleAllRoutes(
        input: APIGatewayProxyRequestEvent?
    ): APIGatewayProxyResponseEvent {
        val routeName = input?.resource ?: EMPTY_STRING
        val controller = controllers.firstOrNull { it.routeName == routeName }
        return when {
            input == null -> createNotFoundResponse(input)
            controller == null -> {
                logger?.log("No route controller implemented for route: $routeName")
                createNoControllerFoundResponse(input)
            }
            else -> controller.handleRoute(input)
        }
    }

    private fun createNotFoundResponse(input: APIGatewayProxyRequestEvent?) = APIGatewayProxyResponseEvent().apply {
        statusCode = CODE_NOT_FOUND
        body = "{\"error\": \"No input found.\"}"
    }

    private fun createNoControllerFoundResponse(input: APIGatewayProxyRequestEvent?) =
        APIGatewayProxyResponseEvent().apply {
            statusCode = CODE_NOT_FOUND
            body = "{\"error\": \"No controller found for that resource: ${input?.resource}\"}"
        }

    companion object {
        const val CODE_NOT_FOUND = 500
    }
}