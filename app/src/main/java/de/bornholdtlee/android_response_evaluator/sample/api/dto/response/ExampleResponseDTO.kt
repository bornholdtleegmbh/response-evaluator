package de.bornholdtlee.android_response_evaluator.sample.api.dto.response

import com.google.gson.annotations.SerializedName

data class ExampleResponseDTO(

    @SerializedName("id")
    val id: String,

    @SerializedName("name")
    val name: String
)
