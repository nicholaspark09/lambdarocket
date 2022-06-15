package com.cincinnatiai.lambdarocket.controller

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent

interface RouteControllerContract {

    /**
     * Route names that are routed through API Gateway must have their routeName set as "/{name}"
     * So, https://www.meh.com/hellowork on API Gateway would equate to `/hellowork` in their RouteController
     */
    val routeName: String

    fun handleRoute(input: APIGatewayProxyRequestEvent): APIGatewayProxyResponseEvent

}