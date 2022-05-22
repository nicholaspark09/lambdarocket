package com.cincinnatiai.lambdarocket

import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.LambdaLogger
import com.cincinnatiai.lambdarocket.controller.RouteControllerContract
import com.cincinnatiai.lambdarocket.router.RequestRouter
import com.cincinnatiai.lambdarocket.router.RequestRouterContract
import com.google.gson.Gson

/**
 * This class should be initialized in your project before `handleRequest` is called
 * in the RequestHandler
 */
class LambdaRocket private constructor(
    val context: Context,
    val logger: LambdaLogger = context.logger,
    val controllers: List<RouteControllerContract> = listOf()
) {

    val gson: Gson by lazy { Gson() }
    val requestRouter: RequestRouterContract by lazy { RequestRouter(logger, this) }

    @Synchronized
    fun findController(routeName: String) = controllers.firstOrNull { it.routeName == routeName }

    companion object {

        @Volatile
        var INSTANCE: LambdaRocket? = null

        @JvmStatic
        fun initialize(context: Context) {
            if (INSTANCE == null) {
                synchronized(this) {
                    if (INSTANCE == null) {
                        INSTANCE = LambdaRocket(context)
                    }
                }
            }
        }

        @JvmStatic
        fun instance() = INSTANCE ?: throw IllegalArgumentException("Please initialize the library")
    }
}