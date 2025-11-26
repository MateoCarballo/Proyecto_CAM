package com.codelabs.controlaccesoapp.data.repository

import com.codelabs.controlaccesoapp.data.api.RetrofitBuilder
import com.codelabs.controlaccesoapp.data.model.RegistroHorariosResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class HorariosRepository {

    private val api = RetrofitBuilder.apiService

    /**
     * Obtiene los registros del usuario usando el token de autenticación
     */
    suspend fun obtenerRegistros(token: String): Result<RegistroHorariosResponse> {
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
