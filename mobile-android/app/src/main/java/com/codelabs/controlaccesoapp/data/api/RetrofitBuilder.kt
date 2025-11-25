package com.codelabs.controlaccesoapp.data.api

import okhttp3.Interceptor
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitBuilder {
    // localhost no funciona, para que funcione desde
    // nuestro emulador en android es necesario usar '10.0.2.2'
    private const val BASE_URL = "http://10.0.2.2:20000/"

    //    Esta opcion comprueba si es valido el token antes de hacer nada la del IDE traga lo que le echen


    class AuthInterceptor(private val tokenProvider: () -> String?) : Interceptor {
        override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
            val original = chain.request()
            val token = tokenProvider()
            val requestBuilder = original.newBuilder()
            token?.let { requestBuilder.addHeader("Authorization", "Bearer $it") }
            return chain.proceed(requestBuilder.build())
        }
    }
// Por sugerencia del maravillosos ANDROID STUDIO

//    class AuthInterceptor(private val tokenProvider: () -> String?) : Interceptor {
//        override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
//            val original = chain.request()
//            val token = tokenProvider()
//            val requestBuilder = original.newBuilder()
//                .header("Authorization", "Bearer $token")
//            return chain.proceed(requestBuilder.build())
//        }
//    }

    private val loggin = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client = okhttp3.OkHttpClient.Builder()
        .connectTimeout(15, java.util.concurrent.TimeUnit.SECONDS)
        .readTimeout(15, java.util.concurrent.TimeUnit.SECONDS)
        .writeTimeout(15, java.util.concurrent.TimeUnit.SECONDS)
        .addInterceptor(loggin)
        //TOKEN AQUI SI USO DATASTORE
        .build()




    //by lazy hace que solo se cree cuando se use por
    //primera vez y no cuando se importe
    val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}
