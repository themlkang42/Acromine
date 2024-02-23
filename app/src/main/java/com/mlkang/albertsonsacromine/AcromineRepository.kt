package com.mlkang.albertsonsacromine

import javax.inject.Inject

class AcromineRepository @Inject constructor(
    private val acromineRemoteDataSource: AcromineRemoteDataSource
) {
    suspend fun getLongForms(acronym: String): List<LongForm> {
        val longFormNetworkDatas = acromineRemoteDataSource.getLongForms(acronym)
        return longFormNetworkDatas.firstOrNull()?.longForms ?: emptyList()
    }
}