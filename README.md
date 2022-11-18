# Retrofit Response Evaluator for Android

![GitHub](https://img.shields.io/github/license/bornholdtleegmbh/response-evaluator?color=%230087ff&style=for-the-badge)
![GitHub top language](https://img.shields.io/github/languages/top/bornholdtleegmbh/response-evaluator?color=%23875dff&style=for-the-badge)

The ResponseEvaluator will map a `retrofit2.Response<T>` bbject to a sealed kotlin class `Result<T>`.

This gives you a conveniant way to differentiate the different api results using kotlins when-statement.

## Usage

```kotlin

// apiResponse is of type retrofit2.Response and comes 
// from your retrofit api interface
val apiResult = ResponseEvaluator.evaluate(apiResponse)

when(apiResult){
  is ResponseEvaluator.Result.Success -> {
    result.response.body()?.let { responseObject ->
      // ...
    }
  }
  is ResponseEvaluator.Result.Failure.ClientError -> {}
  is ResponseEvaluator.Result.Failure.ServerError -> {}
}

// or fully specified

when(apiResult){
  is ResponseEvaluator.Result.Success.Ok -> {}
  is ResponseEvaluator.Result.Success.Accepted -> {}
  is ResponseEvaluator.Result.Success.Created -> {}
  is ResponseEvaluator.Result.Failure.ClientError.BadRequest -> {}
  is ResponseEvaluator.Result.Failure.ClientError.Forbidden -> {}
  is ResponseEvaluator.Result.Failure.ClientError.NotFound -> {}
  is ResponseEvaluator.Result.Failure.ClientError.Unauthorized -> {}
  is ResponseEvaluator.Result.Failure.ServerError.BadGateway -> {}
  is ResponseEvaluator.Result.Failure.ServerError.GatewayTimeout -> {}
  is ResponseEvaluator.Result.Failure.ServerError.InternalError -> {}
  is ResponseEvaluator.Result.Failure.ServerError.Unavailable -> {}
  is ResponseEvaluator.Result.Failure.UnknownError -> {}
}
````
