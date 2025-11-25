package com.codelabs.controlaccesoapp.data.api

import com.codelabs.controlaccesoapp.data.model.LoginRequest
import com.codelabs.controlaccesoapp.data.model.LoginResponse
import com.codelabs.controlaccesoapp.data.model.RegisterReponse
import com.codelabs.controlaccesoapp.data.model.RegisterRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.Dispatcher
import retrofit2.HttpException

class AuthRepository {

    private val api = RetrofitBuilder.apiService

    suspend fun login(
        email: String,
        password: String
    ): Result<LoginResponse>{
        return withContext(Dispatchers.IO){
            try {
                val response = api.login(LoginRequest(email, password))
                if(response.isSuccessful){
                    Result.success(response.body()!!)
                }else{
                    Result.failure(Exception("Error ${response.code()}: ${response.message()}"))

                }
            }catch (e: HttpException){
                Result.failure(e)
            }catch (e: Exception){
                Result.failure(e)
            }
        }
    }

    suspend fun register(
        request: RegisterRequest
    ): Result<RegisterReponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.register(request)
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