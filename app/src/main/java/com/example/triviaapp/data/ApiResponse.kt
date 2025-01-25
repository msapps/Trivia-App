package com.example.triviaapp.data

data class ApiResponse<T, Boolean, E : Exception>(
    var data: T? = null,
    var isLoading: Boolean? = null,
    var exception: E? = null
)
