package com.codelabs.controlaccesoapp.data.api

import com.codelabs.controlaccesoapp.data.model.LoginRequest
import com.codelabs.controlaccesoapp.data.model.LoginResponse
import com.codelabs.controlaccesoapp.data.model.RegisterReponse
import com.codelabs.controlaccesoapp.data.model.RegisterRequest
import com.codelabs.controlaccesoapp.data.model.RegistroHorariosResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<RegisterReponse>

    @POST("horarios/registros")
    suspend fun obtenerRegistros(@Header("Authorization") token: String): Response<RegistroHorariosResponse>

}
