package de.bornholdtlee.android_response_evaluator.sample.usecase

import de.bornholdtlee.android_response_evaluator.sample.api.dto.response.ExampleResponseDTO
import de.bornholdtlee.android_response_evaluator.sample.repository.ExampleRepository
import de.bornholdtlee.response_evaluator.APIResult

class ExampleUseCase(
    private val exampleRepository: ExampleRepository
) {

    suspend fun getExampleData(id: String): ExampleResponseDTO? {

        val apiResult: APIResult<ExampleResponseDTO> = exampleRepository.getExampleDataFromApi(id)

        when (apiResult) {
            is APIResult.Success.Ok -> {}
            is APIResult.Success.Created -> {}
            is APIResult.Success.Accepted -> {}
            is APIResult.Success.Other -> {}
            is APIResult.Redirect -> {}
            is APIResult.Failure.ClientError.BadRequest -> {}
            is APIResult.Failure.ClientError.Conflict -> {}
            is APIResult.Failure.ClientError.Forbidden -> {}
            is APIResult.Failure.ClientError.Gone -> {}
            is APIResult.Failure.ClientError.NotFound -> {}
            is APIResult.Failure.ClientError.Other -> {}
            is APIResult.Failure.ClientError.Unauthorized -> {}
            is APIResult.Failure.ServerError.BadGateway -> {}
            is APIResult.Failure.ServerError.GatewayTimeout -> {}
            is APIResult.Failure.ServerError.InternalError -> {}
            is APIResult.Failure.ServerError.Other -> {}
            is APIResult.Failure.ServerError.Unavailable -> {}
            is APIResult.Unknown -> {}
        }
        return null
    }
}
