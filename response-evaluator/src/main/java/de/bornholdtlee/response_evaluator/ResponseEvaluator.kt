package de.bornholdtlee.response_evaluator

import de.bornholdtlee.response_evaluator.APIResult.Failure.*
import de.bornholdtlee.response_evaluator.APIResult.Success
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Response
import java.net.HttpURLConnection.*

object ResponseEvaluator {

    private val httpCodeRangeSuccess = 200 until 300
    private val httpCodeRangeRedirect = 300 until 400
    private val httpCodeRangeClientError = 400 until 500
    private val httpCodeRangeServerError = 500 until 600
    private const val HTTP_CODE_UNKNOWN = 600

    fun <T> evaluate(response: Response<T>?): APIResult<T> {
        response ?: return UnknownError(generateUnknownResponse())
        return when (response.code()) {
            in httpCodeRangeSuccess -> processSuccess(response)
            in httpCodeRangeRedirect -> APIResult.Redirect(response)
            in httpCodeRangeClientError -> processClientError(response)
            in httpCodeRangeServerError -> processServerError(response)
            else -> UnknownError(response)
        }
    }

    private fun <T> processSuccess(response: Response<T>): Success<T> {
        assert(response.code() in httpCodeRangeSuccess) {
            "response code must be in 200..299"
        }
        return when (response.code()) {
            HTTP_OK -> Success.Ok(response)
            HTTP_CREATED -> Success.Created(response)
            HTTP_ACCEPTED -> Success.Accepted(response)
            else -> Success.Other(response)
        }
    }

    private fun <T> processClientError(response: Response<T>): ClientError<T> {
        assert(response.code() in httpCodeRangeClientError) {
            "response code must be in 400..499"
        }
        return when (response.code()) {
            HTTP_BAD_REQUEST -> ClientError.BadRequest(response)
            HTTP_UNAUTHORIZED -> ClientError.Unauthorized(response)
            HTTP_FORBIDDEN -> ClientError.Forbidden(response)
            HTTP_NOT_FOUND -> ClientError.NotFound(response)
            HTTP_CONFLICT -> ClientError.Conflict(response)
            HTTP_GONE -> ClientError.Gone(response)
            else -> ClientError.Other(response)
        }
    }

    private fun <T> processServerError(response: Response<T>): ServerError<T> {
        assert(response.code() in httpCodeRangeServerError) {
            "response code must be in 500..599"
        }
        return when (response.code()) {
            HTTP_INTERNAL_ERROR -> ServerError.InternalError(response)
            HTTP_BAD_GATEWAY -> ServerError.BadGateway(response)
            HTTP_UNAVAILABLE -> ServerError.Unavailable(response)
            HTTP_GATEWAY_TIMEOUT -> ServerError.GatewayTimeout(response)
            else -> ServerError.Other(response)
        }
    }

    private fun <T> generateUnknownResponse(): Response<T> = Response.error(
        HTTP_CODE_UNKNOWN,
        "".toResponseBody("application/json".toMediaTypeOrNull())
    )
}
