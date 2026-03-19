package com.designlife.opendraw.home.domain.usecase

import android.content.Context

sealed class HomeUseCase {
    data class SearchStateChangeEvent(val state : String) : HomeUseCase()
    object OnSearchClickEvent : HomeUseCase()
    object AddBoardEvent : HomeUseCase()
    data class BoardTitleChangeEvent(val state : String) : HomeUseCase()
    object CreateBoardEvent : HomeUseCase()

//    data class OnImportEvent(val context: Context) : HomeUseCase()
    data class OnImportEvent(val json : String) : HomeUseCase()
    data object OnDarkModeToggle : HomeUseCase()
}