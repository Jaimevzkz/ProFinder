package com.vzkz.profinder.ui.search

import androidx.lifecycle.viewModelScope
import com.vzkz.profinder.core.boilerplate.BaseViewModel
import com.vzkz.profinder.domain.error.Result
import com.vzkz.profinder.domain.usecases.user.GetAllUsersUseCase
import com.vzkz.profinder.ui.asUiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SearchViewModel @Inject constructor(
    private val getAllUsersUseCase: GetAllUsersUseCase
) : BaseViewModel<SearchState, SearchIntent>(SearchState.initial) {

    override fun reduce(state: SearchState, intent: SearchIntent): SearchState {
        return when (intent) {
            is SearchIntent.Loading -> state.copy(loading = true)

            is SearchIntent.Error -> state.copy(
                error = intent.error,
            )

            SearchIntent.CloseError -> state.copy(
                error = null,
            )

            is SearchIntent.SetUserList -> state.copy(
                loading = false,
                userList = intent.userList
            )
        }
    }

    //Observe events from UI and dispatch them, this are the methods called from the UI
    fun onInit() {
        viewModelScope.launch(Dispatchers.IO) {
            when (val userList = getAllUsersUseCase()) {
                is Result.Success -> dispatch(SearchIntent.SetUserList(userList.data))
                is Result.Error -> dispatch(SearchIntent.Error(userList.error.asUiText()))
            }
        }
    }

    fun onCloseDialog() = dispatch(SearchIntent.CloseError)

}