package com.maat.cha.core.network.api

import com.maat.cha.core.data.model.MatchViewResponse
import retrofit2.http.GET

interface MatchViewApi {
    @GET("matchView")
    suspend fun getMatchView(): MatchViewResponse
} 