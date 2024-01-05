package com.medichain.totalhealth.network

import android.content.Context
import android.util.Log
import com.medichain.totalhealth.BuildConfig
import com.medichain.totalhealth.R
import okhttp3.Headers
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.internal.http.promisesBody
import okhttp3.logging.HttpLoggingInterceptor
import okio.Buffer
import okio.GzipSource
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.EOFException
import java.io.IOException
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets
import java.util.*
import java.util.concurrent.TimeUnit
import javax.net.ssl.*

public class ServerAPI {
    fun getAPI(context: Context) : Retrofit {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        return Retrofit.Builder()
            .baseUrl(context.getString(R.string.server_url))
            .client(
                OkHttpClient.Builder()
//                    .sslSocketFactory(sslContext.socketFactory, trustManager)
                    .hostnameVerifier(NullHostNameVerifier())
                    .readTimeout(30, TimeUnit.SECONDS)
                    .addInterceptor(interceptor)
                    .addInterceptor(
                        AddCookiesInterceptor(
                            context
                        )
                    )
                    .addInterceptor(CustomInterceptor(context, ""))
                    .build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    fun getAmazonAPI(context: Context) : Retrofit {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        return Retrofit.Builder()
            .baseUrl(context.getString(R.string.amazon_server_url))
            .client(
                OkHttpClient.Builder()
//                    .sslSocketFactory(sslContext.socketFactory, trustManager)
                    .hostnameVerifier(NullHostNameVerifier())
                    .readTimeout(30, TimeUnit.SECONDS)
                    .addInterceptor(interceptor)
                    .addInterceptor(
                        AddCookiesInterceptor(
                            context
                        )
                    )
                    .addInterceptor(CustomInterceptor(context, ""))
                    .build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    class NullHostNameVerifier : HostnameVerifier {
        override fun verify(p0: String?, p1: SSLSession?): Boolean {
            return true
        }
    }

    class AppInterceptor : Interceptor {
        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain) : Response = with(chain) {
            val newRequest = request().newBuilder()
                .addHeader("X-OCR-SECRET", "ZHNrem5MaUJtY3dXTkVTcHdmbkNnbG9tWVdpVnFCelI=")
//                .addHeader("Content-Type", "application/json")
                .build()
            proceed(newRequest)
        }
    }
    class LogDataAPIInterceptor : Interceptor {
        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain) : Response = with(chain) {
//            val key = "0gLHyC6Wl0YWadajyh2Ae1evBKYUx9GUqjRTSU4TybPtAzFuWBuiUg=="
            val debug_key = "w7XNj_G6gEnmpuH15ZIw_bO3HEL4z6gcvzij8V1f4A0gAzFu6tkJGw=="
            val newRequest = request().newBuilder()
                .addHeader("x-functions-key", debug_key)
                .build()
            proceed(newRequest)
        }
    }
    class NaverGeocodingAPIInterceptor : Interceptor {
        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain) : Response = with(chain) {
            val newRequest = request().newBuilder()
                .addHeader("X-NCP-APIGW-API-KEY-ID", "hhabw2x06g")
                .addHeader("X-NCP-APIGW-API-KEY", "2JMog70u9gsIOELwpj0MNQz6VGBsrcW27g8rfjog")
                .build()
            proceed(newRequest)
        }
    }
    class CustomInterceptor(val context: Context, val Category:String) : Interceptor {
        private val logger: HttpLoggingInterceptor.Logger = HttpLoggingInterceptor.Logger.DEFAULT

        @Volatile private var headersToRedact = emptySet<String>()

        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain) : Response {
            val request = when(Category){
//                "MediChain" -> {
//                    val debug_key = "w7XNj_G6gEnmpuH15ZIw_bO3HEL4z6gcvzij8V1f4A0gAzFu6tkJGw=="
//                    chain.request().newBuilder()
//                        .addHeader("x-functions-key", debug_key)
//                        .build()
//
//                }
                "Kiwoong" -> {
                    chain.request().newBuilder()
                        .addHeader("Authorization", "4ac3bee5a4904d42833eaa5f1c5d04c90a89c5a1")
                        .build()

                }
                else ->{
                    chain.request()
                }
            }

            var logMsg = ""

            val requestBody = request.body

            val connection = chain.connection()
            var requestStartMessage =
                ("--> ${request.method} ${request.url}${if (connection != null) " " + connection.protocol() else ""}")
            if (requestBody != null) {
                requestStartMessage += " (${requestBody.contentLength()}-byte body)"
            }
            logger.log(requestStartMessage)
            logMsg += requestStartMessage + "\n"

            val headers = request.headers

            if (requestBody != null) {
                // Request body headers are only present when installed as a network interceptor. When not
                // already present, force them to be included (if available) so their values are known.
                requestBody.contentType()?.let {
                    if (headers["Content-Type"] == null) {
                        logger.log("Content-Type: $it")
                        logMsg += "Content-Type: $it\n"
                    }
                }
                if (requestBody.contentLength() != -1L) {
                    if (headers["Content-Length"] == null) {
                        logger.log("Content-Length: ${requestBody.contentLength()}")
                        logMsg += "Content-Length: ${requestBody.contentLength()}\n"
                    }
                }
            }

            for (i in 0 until headers.size) {
                logHeader(headers, i)
            }

            if (requestBody == null) {
                logger.log("--> END ${request.method}")
                logMsg += "--> END ${request.method}"
            } else if (bodyHasUnknownEncoding(request.headers)) {
                logger.log("--> END ${request.method} (encoded body omitted)")
                logMsg += "--> END ${request.method} (encoded body omitted)"
            } else if (requestBody.isDuplex()) {
                logger.log("--> END ${request.method} (duplex request body omitted)")
                logMsg += "--> END ${request.method} (duplex request body omitted)"
            } else if (requestBody.isOneShot()) {
                logger.log("--> END ${request.method} (one-shot body omitted)")
                logMsg += "--> END ${request.method} (one-shot body omitted)"
            } else {
                val buffer = Buffer()
                requestBody.writeTo(buffer)

                val contentType = requestBody.contentType()
                val charset: Charset = contentType?.charset(StandardCharsets.UTF_8) ?: StandardCharsets.UTF_8

                logger.log("")
                if (buffer.isProbablyUtf8()) {
                    val requestBodyStr = buffer.readString(charset)
                    logger.log(requestBodyStr)
                    logger.log("--> END ${request.method} (${requestBody.contentLength()}-byte body)")
                    logMsg += "${requestBodyStr}\n--> END ${request.method} (${requestBody.contentLength()}-byte body)"
                } else {
                    logger.log(
                        "--> END ${request.method} (binary ${requestBody.contentLength()}-byte body omitted)")
                    logMsg += "--> END ${request.method} (binary ${requestBody.contentLength()}-byte body omitted)"
                }
            }


            val startNs = System.nanoTime()
            val response: Response
            try {
                response = chain.proceed(request)
            } catch (e: Exception) {
                logger.log("<-- HTTP FAILED: $e")
                throw e
            }
            logMsg = ""
            val tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs)

            val responseBody = response.body!!
            val contentLength = responseBody.contentLength()
            val bodySize = if (contentLength != -1L) "$contentLength-byte" else "unknown-length"
            logger.log(
                "<-- ${response.code}${if (response.message.isEmpty()) "" else ' ' + response.message} ${response.request.url} (${tookMs}ms, ${bodySize})")
            logMsg += "<-- ${response.code}${if (response.message.isEmpty()) "" else ' ' + response.message} ${response.request.url} (${tookMs}ms, ${bodySize})\n"

            for (i in 0 until headers.size) {
                logHeader(headers, i)
            }

            if (!response.promisesBody()) {
                logger.log("<-- END HTTP")
                logMsg += "<-- END HTTP"
            } else if (bodyHasUnknownEncoding(response.headers)) {
                logger.log("<-- END HTTP (encoded body omitted)")
                logMsg += "<-- END HTTP (encoded body omitted)"
            } else {
                val source = responseBody.source()
                source.request(Long.MAX_VALUE) // Buffer the entire body.
                var buffer = source.buffer

                var gzippedLength: Long? = null
                if ("gzip".equals(headers["Content-Encoding"], ignoreCase = true)) {
                    gzippedLength = buffer.size
                    GzipSource(buffer.clone()).use { gzippedResponseBody ->
                        buffer = Buffer()
                        buffer.writeAll(gzippedResponseBody)
                    }
                }

                val contentType = responseBody.contentType()
                val charset: Charset = contentType?.charset(StandardCharsets.UTF_8) ?: StandardCharsets.UTF_8

                if (!buffer.isProbablyUtf8()) {
                    logMsg += "\n<-- END HTTP (binary ${buffer.size}-byte body omitted)"
                    logger.log("")
                    logger.log("<-- END HTTP (binary ${buffer.size}-byte body omitted)")
                    return response
                }

                if (contentLength != 0L) {
                    logger.log("")
                    val responseStr = buffer.clone().readString(charset)
                    logger.log(responseStr)
                    logMsg += "\n<-- $responseStr\n"

                }

                if (gzippedLength != null) {
                    logMsg += "<-- END HTTP (${buffer.size}-byte, $gzippedLength-gzipped-byte body)"
                    logger.log("<-- END HTTP (${buffer.size}-byte, $gzippedLength-gzipped-byte body)")
                } else {
                    logMsg += "<-- END HTTP (${buffer.size}-byte body)"
                    logger.log("<-- END HTTP (${buffer.size}-byte body)")
                }
            }

            return response
        }

        private fun Buffer.isProbablyUtf8(): Boolean {
            try {
                val prefix = Buffer()
                val byteCount = size.coerceAtMost(64)
                copyTo(prefix, 0, byteCount)
                for (i in 0 until 16) {
                    if (prefix.exhausted()) {
                        break
                    }
                    val codePoint = prefix.readUtf8CodePoint()
                    if (Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
                        return false
                    }
                }
                return true
            } catch (_: EOFException) {
                return false // Truncated UTF-8 sequence.
            }
        }
        private fun logHeader(headers: Headers, i: Int) {
            val value = if (headers.name(i) in headersToRedact) "██" else headers.value(i)
            logger.log(headers.name(i) + ": " + value)
        }

        private fun bodyHasUnknownEncoding(headers: Headers): Boolean {
            val contentEncoding = headers["Content-Encoding"] ?: return false
            return !contentEncoding.equals("identity", ignoreCase = true) &&
                    !contentEncoding.equals("gzip", ignoreCase = true)
        }
    }
    //    class KiwoongInterceptor : Interceptor {
//        @Throws(IOException::class)
//        override fun intercept(chain: Interceptor.Chain) : Response = with(chain) {
//            val newRequest = request().newBuilder()
//                .addHeader("Authorization", "4ac3bee5a4904d42833eaa5f1c5d04c90a89c5a1")
////                .addHeader("Authorization", "f3d50958ca674985be2b6e41bb96a04d88d6beda")
////                .addHeader("Content-Type", "application/json")
//                .build()
//            proceed(newRequest)
//        }
//    }
    class KakaoLocalInterceptor : Interceptor {
        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain) : Response = with(chain) {
            val newRequest = request().newBuilder()
                .addHeader("Authorization", "KakaoAK b339be3bba3e3e9c0acc094939513501")
//                .addHeader("Content-Type", "application/json")
                .build()
            proceed(newRequest)
        }
    }
    class AddCookiesInterceptor(context: Context) : Interceptor {
        private var con = context
        override fun intercept(chain: Interceptor.Chain): Response {
            val builder = chain.request().newBuilder()

            val url = chain.request().url.newBuilder()
//                .addEncodedQueryParameter("Ver", BuildConfig.VERSION_NAME)
                .build()
            builder.url(url).build()
            val preferences = con.getSharedPreferences(con.packageName, Context.MODE_PRIVATE).getStringSet("Cookie", HashSet())

            if (preferences != null) {
                for (cookie in preferences) {
                    builder.addHeader("Cookie", cookie)
                    Log.d("Cookies", "AddCookiesInterceptor : $cookie")
                }
            }
            builder.removeHeader("User-Agent").addHeader("User-Agent", "Android")

            val originalResponse = chain.proceed(builder.build())

            if (!originalResponse.headers("Set-Cookie").isEmpty()) {
                val cookies = HashSet<String>()

                for (header in originalResponse.headers("Set-Cookie")) {
                    cookies.add(header)
                }

                con.getSharedPreferences(con.packageName, Context.MODE_PRIVATE).edit()
                    .putStringSet("Cookie", cookies).apply()
                Log.d("Cookies", "ReceivedCookiesInterceptor : $cookies")
            }

            return originalResponse
        }
    }
}