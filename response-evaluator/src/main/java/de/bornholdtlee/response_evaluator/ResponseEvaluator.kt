package de.bornholdtlee.response_evaluator

import de.bornholdtlee.response_evaluator.APIResult.Failure.*
import de.bornholdtlee.response_evaluator.APIResult.Failure.ClientError.*
import de.bornholdtlee.response_evaluator.APIResult.Failure.ServerError.*
import de.bornholdtlee.response_evaluator.APIResult.Success
import de.bornholdtlee.response_evaluator.APIResult.Success.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Response
import java.net.HttpURLConnection.*

object ResponseEvaluator {

    private const val HTTP_CODE_UNKNOWN = 600

    fun <T> evaluate(response: Response<T>?): APIResult<T> {
        response ?: return UnknownError(generateUnknownResponse())
        return when (response.code()) {
            in HTTP_OK until HTTP_MULT_CHOICE -> processSuccess(response)
            in HTTP_BAD_REQUEST until HTTP_INTERNAL_ERROR -> processClientError(response)
            in HTTP_INTERNAL_ERROR until HTTP_CODE_UNKNOWN -> processServerError(response)
            else -> null
        } ?: UnknownError(response)
    }

    private fun <T> processSuccess(response: Response<T>): Success<T>? =
        when (response.code()) {
            HTTP_OK -> Ok(response)
            HTTP_CREATED -> Created(response)
            HTTP_ACCEPTED -> Accepted(response)
            else -> null
        }

    @Throws(Exception::class)
    private fun <T> processClientError(response: Response<T>): ClientError<T>? =
        when (response.code()) {
            HTTP_BAD_REQUEST -> BadRequest(response)
            HTTP_UNAUTHORIZED -> Unauthorized(response)
            HTTP_FORBIDDEN -> Forbidden(response)
            HTTP_NOT_FOUND -> NotFound(response)
            else -> null
        }

    @Throws(Exception::class)
    private fun <T> processServerError(response: Response<T>): ServerError<T>? =
        when (response.code()) {
            HTTP_INTERNAL_ERROR -> InternalError(response)
            HTTP_BAD_GATEWAY -> BadGateway(response)
            HTTP_UNAVAILABLE -> Unavailable(response)
            HTTP_GATEWAY_TIMEOUT -> GatewayTimeout(response)
            else -> null
        }

    private fun <T> generateUnknownResponse(): Response<T> = Response.error(
        HTTP_CODE_UNKNOWN,
        "".toResponseBody("application/json".toMediaTypeOrNull())
    )
}
