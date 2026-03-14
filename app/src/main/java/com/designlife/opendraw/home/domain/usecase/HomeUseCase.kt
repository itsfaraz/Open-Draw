package com.designlife.opendraw.home.domain.usecase

sealed class HomeUseCase {
    data class SearchStateChangeEvent(val state : String) : HomeUseCase()
    object OnSearchClickEvent : HomeUseCase()
    object AddBoardEvent : HomeUseCase()
    data class BoardTitleChangeEvent(val state : String) : HomeUseCase()
    object CreateBoardEvent : HomeUseCase()
}