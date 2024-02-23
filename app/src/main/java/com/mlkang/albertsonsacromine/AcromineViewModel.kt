package com.mlkang.albertsonsacromine

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.transformLatest
import javax.inject.Inject

@HiltViewModel
class AcromineViewModel @Inject constructor(
    private val acromineRepository: AcromineRepository
) : ViewModel() {
    private val _acronymState: MutableStateFlow<String> = MutableStateFlow("")
    val acronymState = _acronymState.asStateFlow()

    val longFormsState = acronymState
        .debounce(DEBOUNCE_MILLIS)
        .transformLatest { acronym ->
            if (acronym.isEmpty()) {
                emit(NetworkResult.Success(emptyList()))
            } else {
                emit(NetworkResult.Loading)
                emit(NetworkResult.Success(acromineRepository.getLongForms(acronym)))
            }
        }
        .catch {
            Log.e(AcromineViewModel::class.simpleName, "Error getting long forms", it)
            emit(NetworkResult.Failure(it))
        }
        .stateIn(viewModelScope, SharingStarted.Eagerly, NetworkResult.Success(emptyList()))

    fun updateAcronymInput(acronym: String) {
        _acronymState.value = acronym
    }

    companion object {
        const val DEBOUNCE_MILLIS = 300L
    }
}