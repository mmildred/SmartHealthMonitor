package mx.utng.smarthealthmonitor.data.remote

import mx.utng.smarthealthmonitor.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Cliente Singleton para configurar Retrofit y la conexión con PostgreSQL Neon.
 */
object NeonClient {
    private const val BASE_URL = "https://${BuildConfig.NEON_HOST}/"
    
    // El Auth Header se construye dinámicamente con la API KEY de local.properties
    const val AUTH_HEADER = "Bearer ${BuildConfig.NEON_API_KEY}"
    
    // Nota: La cadena de conexión completa debería configurarse también en local.properties
    // Se asume que el host ya es parte de la URL base.
    const val CONN_STRING = "postgres://alex:tu_password@${BuildConfig.NEON_HOST}/smarthealth_db?sslmode=require"

    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val httpClient = OkHttpClient.Builder()
        .addInterceptor(logging)
        .build()

    val apiService: NeonApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient)
            .build()
            .create(NeonApiService::class.java)
    }
}
