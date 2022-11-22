# Retrofit Response Evaluator for Android

![JitPack](https://img.shields.io/jitpack/version/com.github.bornholdtleegmbh/response-evaluator?color=%23cdb83&style=for-the-badge)
![GitHub](https://img.shields.io/github/license/bornholdtleegmbh/response-evaluator?color=%230087ff&style=for-the-badge)
![GitHub top language](https://img.shields.io/github/languages/top/bornholdtleegmbh/response-evaluator?color=%23875dff&style=for-the-badge)

The `ResponseEvaluator` maps a `retrofit2.Response<T>` object to a kotlin sealed class `Result<T>`.

This gives you a conveniant way to differentiate the different api results using kotlins when-statement.

## Usage

```kotlin

// apiResponse is of type retrofit2.Response and comes 
// from your retrofit api interface
val apiResult = ResponseEvaluator.evaluate(apiResponse)

when(apiResult){
  is APIResult.Success -> {
    apiResult.response.body()?.let { responseObject ->
      // ...
    }
  }
  is APIResult.Failure.ClientError -> {}
  is APIResult.Failure.ServerError -> {}
}

// or fully specified

when(apiResult){
  is APIResult.Success.Ok -> {}
  is APIResult.Success.Accepted -> {}
  is APIResult.Success.Created -> {}
  is APIResult.Failure.ClientError.BadRequest -> {}
  is APIResult.Failure.ClientError.Forbidden -> {}
  is APIResult.Failure.ClientError.NotFound -> {}
  is APIResult.Failure.ClientError.Unauthorized -> {}
  is APIResult.Failure.ServerError.BadGateway -> {}
  is APIResult.Failure.ServerError.GatewayTimeout -> {}
  is APIResult.Failure.ServerError.InternalError -> {}
  is APIResult.Failure.ServerError.Unavailable -> {}
  is APIResult.Failure.UnknownError -> {}
}
````

## Installation

Add it in your root build.gradle at the end of repositories:
```gradle
allprojects {
  repositories {
    ...
    maven { url 'https://jitpack.io' }
  }
}
```

Add the dependency to your module level build.gradle:
```gradle
dependencies {
  implementation 'com.github.bornholdtleegmbh:response-evaluator:1.0.0'
}
```
