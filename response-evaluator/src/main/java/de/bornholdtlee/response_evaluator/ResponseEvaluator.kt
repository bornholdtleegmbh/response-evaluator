package de.bornholdtlee.response_evaluator

import de.bornholdtlee.response_evaluator.APIResult.Failure.ClientError
import de.bornholdtlee.response_evaluator.APIResult.Failure.ServerError
import de.bornholdtlee.response_evaluator.APIResult.Success
import retrofit2.Response
import java.net.HttpURLConnection.*

object ResponseEvaluator {

    private val httpCodeRangeSuccess = 200 until 300
    private val httpCodeRangeRedirect = 300 until 400
    private val httpCodeRangeClientError = 400 until 500
    private val httpCodeRangeServerError = 500 until 600

    fun <T> evaluate(response: Response<T>?): APIResult<T> {
        response?.code() ?: return APIResult.Unknown()
        return when (response.code()) {
            in httpCodeRangeSuccess -> mapSuccess(response)
            in httpCodeRangeRedirect -> APIResult.Redirect(response)
            in httpCodeRangeClientError -> mapClientError(response)
            in httpCodeRangeServerError -> mapServerError(response)
            else -> APIResult.Unknown()
        }
    }

    private fun <T> mapSuccess(response: Response<T>): Success<T> {
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

    private fun <T> mapClientError(response: Response<T>): ClientError<T> {
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

    private fun <T> mapServerError(response: Response<T>): ServerError<T> {
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
}
