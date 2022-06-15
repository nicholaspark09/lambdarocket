package com.cincinnatiai.lambdarocket

import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.LambdaLogger
import com.cincinnatiai.lambdarocket.controller.RouteControllerContract
import com.cincinnatiai.lambdarocket.repository.ConfigRepositoryContract
import com.cincinnatiai.lambdarocket.router.RequestRouter
import com.cincinnatiai.lambdarocket.router.RequestRouterContract
import com.google.gson.Gson

/**
 * This class should be initialized in your project before `handleRequest` is called
 * in the RequestHandler
 */
class LambdaRocket private constructor(
    builder: Builder
) {
    val configRepository: ConfigRepositoryContract? by lazy { builder.configRepository }
    val controllers: List<RouteControllerContract> = builder.controllers.toList()
    val context: Context? = builder.context
    val logger: LambdaLogger? = builder.logger

    class Builder constructor() {
        internal var context: Context? = null
        internal var logger: LambdaLogger? = null
        internal var configRepository: ConfigRepositoryContract? = null
        internal val controllers: MutableSet<RouteControllerContract> = hashSetOf()

        fun setContext(context: Context) = apply {
            this.context = context
        }

        fun setLogger(logger: LambdaLogger) = apply {
            this.logger = logger
        }

        fun setConfigRepository(configRepository: ConfigRepositoryContract) = apply {
            this.configRepository = configRepository
        }

        fun addController(controller: RouteControllerContract) = apply {
            this.controllers.add(controller)
        }

        fun build(): LambdaRocket = LambdaRocket(this)
    }

    val gson: Gson by lazy { Gson() }
    val requestRouter: RequestRouterContract by lazy { RequestRouter(logger, controllers) }
}