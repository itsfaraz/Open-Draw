package com.designlife.opendraw.board.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.designlife.opendraw.common.domain.repository.BoardRepository

class BoardViewModelFactory(
    private val boardRepository: BoardRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return BoardViewModel(boardRepository) as T
    }
}