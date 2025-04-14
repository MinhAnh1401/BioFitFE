package com.example.biofit.data.remote

/*import okhttp3.logging.HttpLoggingInterceptor*/
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDateTime
import com.google.gson.JsonDeserializationContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

import java.lang.reflect.Type


object RetrofitClient {
    const val BASE_URL = "http://172.16.3.102:8080/"

    // Create a custom deserializer for LocalDateTime
    private val localDateTimeDeserializer = object : JsonDeserializer<LocalDateTime> {
        override fun deserialize(
            json: JsonElement,
            typeOfT: Type,
            context: JsonDeserializationContext
        ): LocalDateTime {
            return LocalDateTime.parse(json.asString)
        }
    }

    private val gson = GsonBuilder()
        .registerTypeAdapter(LocalDateTime::class.java, localDateTimeDeserializer)
        .create()

    val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    val instance: ApiService by lazy {
        Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}
