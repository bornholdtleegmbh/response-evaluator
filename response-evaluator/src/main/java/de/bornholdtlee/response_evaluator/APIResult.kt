package de.bornholdtlee.response_evaluator

import retrofit2.Response

sealed class APIResult<T>(val response: Response<T>) {

    /** Http-Status 200 - 299 **/
    sealed class Success<T>(response: Response<T>) : APIResult<T>(response) {

        /** Http-Status 200 **/
        class Ok<T>(response: Response<T>) : Success<T>(response)

        /** Http-Status 201 **/
        class Created<T>(response: Response<T>) : Success<T>(response)

        /** Http-Status 202 **/
        class Accepted<T>(response: Response<T>) : Success<T>(response)
    }

    /** Http-Status 400 - 599 **/
    sealed class Failure<T>(response: Response<T>) : APIResult<T>(response) {

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
