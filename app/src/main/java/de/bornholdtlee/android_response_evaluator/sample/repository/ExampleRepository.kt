package de.bornholdtlee.android_response_evaluator.sample.repository

import de.bornholdtlee.android_response_evaluator.sample.api.ApiInterfaceProvider.apiInterface
import de.bornholdtlee.android_response_evaluator.sample.api.dto.request.ExampleRequestDTO
import de.bornholdtlee.android_response_evaluator.sample.api.dto.response.ExampleResponseDTO
import de.bornholdtlee.response_evaluator.APIResult
import de.bornholdtlee.response_evaluator.ResponseEvaluator
import retrofit2.Response

class ExampleRepository {

    suspend fun getExampleDataFromApi(id: String): APIResult<ExampleResponseDTO> {

        // Get the retrofit response object from api call interface
        val retrofitResponse: Response<ExampleResponseDTO> = apiInterface.exampleApiCall(
            body = ExampleRequestDTO(id = id)
        )

        // Map retrofit response to APIResult
        return ResponseEvaluator.evaluate(
            response = retrofitResponse
        )
    }
}
