package com.maat.cha.core.network.repository

import com.maat.cha.core.data.model.MatchViewResponse
import com.maat.cha.core.network.api.MatchViewApi
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.withTimeout
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MatchViewRepository @Inject constructor(
    private val api: MatchViewApi
) {
    suspend fun getMatchView(): Result<MatchViewResponse> {
        return try {
            val response = withTimeout(15000) { api.getMatchView() }
            Result.success(response)
        } catch (e: TimeoutCancellationException) {
            Result.failure(Exception("Request timeout"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
} 