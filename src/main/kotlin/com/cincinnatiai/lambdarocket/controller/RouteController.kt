package com.cincinnatiai.lambdarocket.controller

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent

interface RouteControllerContract {

    val routeName: String

    fun handleRoute(input: APIGatewayProxyRequestEvent): APIGatewayProxyResponseEvent

}