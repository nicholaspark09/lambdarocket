package com.cincinnatiai.lambdarocket.repository

interface ConfigRepositoryContract {

    fun getValue(key: String): String
}