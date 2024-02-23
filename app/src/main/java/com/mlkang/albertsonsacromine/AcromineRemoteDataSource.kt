package com.mlkang.albertsonsacromine

import retrofit2.http.GET
import retrofit2.http.Query

interface AcromineRemoteDataSource {
    @GET("/software/acromine/dictionary.py")
    suspend fun getLongForms(@Query("sf") acronym: String): List<LongFormNetworkData>
}