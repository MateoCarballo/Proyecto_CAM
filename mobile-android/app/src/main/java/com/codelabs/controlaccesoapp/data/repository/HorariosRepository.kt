package com.codelabs.controlaccesoapp.data.repository

import com.codelabs.controlaccesoapp.data.api.RetrofitBuilder
import com.codelabs.controlaccesoapp.data.model.HorariosResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class HorariosRepository(
    private val tokenManager: TokenManager
) {

    private val api = RetrofitBuilder.createApiService { tokenManager.getToken() }

    /**
     * Obtiene los registros del usuario usando el token de autenticación
     */
    suspend fun getHorariosRegistros(token: String?): Result<HorariosResponse> {
        return withContext(Dispatchers.IO) {
            try {
                // Se asume que el endpoint requiere el token en el header Authorization
                val response = api.obtenerRegistros("Bearer $token")

                if (response.isSuccessful) {
                    Result.success(response.body()!!)
                } else {
                    Result.failure(Exception("Error ${response.code()}: ${response.message()}"))
                }

            } catch (e: HttpException) {
                Result.failure(e)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}
