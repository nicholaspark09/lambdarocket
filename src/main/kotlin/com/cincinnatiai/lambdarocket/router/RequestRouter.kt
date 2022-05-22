package com.cincinnatiai.lambdarocket.router

import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.LambdaLogger
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent
import com.cincinnatiai.lambdarocket.LambdaRocket
import com.cincinnatiai.lambdarocket.utils.EMPTY_STRING

interface RequestRouterContract {

    fun handleAllRoutes(input: APIGatewayProxyRequestEvent?, context: Context):
            APIGatewayProxyResponseEvent
}

class RequestRouter(
    private val logger: LambdaLogger,
    private val rocket: LambdaRocket
) : RequestRouterContract {

    override fun handleAllRoutes(
        input: APIGatewayProxyRequestEvent?,
        context: Context
    ): APIGatewayProxyResponseEvent {
        val routeName = input?.resource ?: EMPTY_STRING
        val controller = rocket.findController(routeName)
        return when {
            input == null -> createNotFoundResponse()
            controller == null -> {
                logger.log("No route controller implemented for route: $routeName")
                createNotFoundResponse()
            }
            else -> controller.handleRoute(input)
        }
    }

    private fun createNotFoundResponse() = APIGatewayProxyResponseEvent().apply {
        statusCode = CODE_NOT_FOUND
        body = NOT_FOUND_ERROR_BODY
    }

    companion object {
        const val CODE_NOT_FOUND = 500
        const val NOT_FOUND_ERROR_BODY = "{\"error\": \"No route found for that url\"}"
    }
}