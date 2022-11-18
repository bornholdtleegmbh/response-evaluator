package de.bornholdtlee.response_evaluator

import de.bornholdtlee.response_evaluator.ResponseEvaluator.Result.Failure.*
import de.bornholdtlee.response_evaluator.ResponseEvaluator.Result.Failure.ClientError.*
import de.bornholdtlee.response_evaluator.ResponseEvaluator.Result.Failure.ServerError.*
import de.bornholdtlee.response_evaluator.ResponseEvaluator.Result.Success
import de.bornholdtlee.response_evaluator.ResponseEvaluator.Result.Success.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Response
import java.net.HttpURLConnection.*

object ResponseEvaluator {

    private const val HTTP_CODE_UNKNOWN = 600

    fun <T> evaluate(response: Response<T>?): Result<T> {
        response ?: return UnknownError(generateUnknownResult())
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

    private fun <T> generateUnknownResult(): Response<T> = Response.error(
        HTTP_CODE_UNKNOWN,
        "".toResponseBody("application/json".toMediaTypeOrNull())
    )

    sealed class Result<T>(val response: Response<T>) {

        /** Http-Status 200 - 299 **/
        sealed class Success<T>(response: Response<T>) : Result<T>(response) {

            /** Http-Status 200 **/
            class Ok<T>(response: Response<T>) : Success<T>(response)

            /** Http-Status 201 **/
            class Created<T>(response: Response<T>) : Success<T>(response)

            /** Http-Status 202 **/
            class Accepted<T>(response: Response<T>) : Success<T>(response)
        }

        /** Http-Status 400 - 599 **/
        sealed class Failure<T>(response: Response<T>) : Result<T>(response) {

            /** Http-Status 400 - 499 **/
            sealed class ClientError<T>(response: Response<T>) : Failure<T>(response) {
                /** Http-Status 400 **/
                class BadRequest<T>(response: Response<T>) : ClientError<T>(response)

                /** Http-Status 401 **/
                class Unauthorized<T>(response: Response<T>) : ClientError<T>(response)

                /** Http-Status 403 **/
                class Forbidden<T>(response: Response<T>) : ClientError<T>(response)

                /** Http-Status 404 **/
                class NotFound<T>(response: Response<T>) : ClientError<T>(response)
            }

            /** Http-Status 500 - 599 **/
            sealed class ServerError<T>(response: Response<T>) : Failure<T>(response) {
                /** Http-Status 500 **/
                class InternalError<T>(response: Response<T>) : ServerError<T>(response)

                /** Http-Status 502 **/
                class BadGateway<T>(response: Response<T>) : ServerError<T>(response)

                /** Http-Status 503 **/
                class Unavailable<T>(response: Response<T>) : ServerError<T>(response)

                /** Http-Status 504 **/
                class GatewayTimeout<T>(response: Response<T>) : ServerError<T>(response)
            }

            /** Http-Status 600 and above **/
            class UnknownError<T>(response: Response<T>) : Failure<T>(response)
        }
    }
}
