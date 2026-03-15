package com.designlife.opendraw.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.designlife.opendraw.home.domain.usecase.HomeUseCase
import com.designlife.opendraw.home.presentation.component.BoardListComponent
import com.designlife.opendraw.home.presentation.component.HomeBottomComponent
import com.designlife.opendraw.home.presentation.component.HomeHeaderComponent
import com.designlife.opendraw.home.presentation.viewmodel.HomeViewModel
import com.designlife.opendraw.home.presentation.viewmodel.HomeViewModelFactory
import com.designlife.opendraw.ui.theme.PrimaryButtonColor
import com.designlife.opendraw.ui.theme.PrimaryColorHome1
import com.designlife.opendraw.R
class HomeFragment : Fragment() {

    private lateinit var viewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val factory = HomeViewModelFactory()
        viewModel = ViewModelProvider(this,factory)[HomeViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent{
                val searchState = viewModel.searchState.value
                val boardList = viewModel.boardList
                val boardTitle = viewModel.boardTitle.value
                val bottomBarView = viewModel.bottomBarLayoutState.value
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.BottomEnd
                ) {
                    Column (
                        modifier = Modifier
                            .fillMaxSize()
                            .background(color = PrimaryColorHome1.value),
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Spacer(modifier = Modifier.height(20.dp))
                        HomeHeaderComponent(
                            searchState = searchState,
                            onSearchStateChange ={
                                viewModel.onEvent(HomeUseCase.SearchStateChangeEvent(it))
                            },
                            onSearchEvent = {
                                viewModel.onEvent(HomeUseCase.OnSearchClickEvent)
                            },
                            addBoardEvent = {
                                viewModel.onEvent(HomeUseCase.AddBoardEvent)
                            }
                        )
                        Spacer(modifier = Modifier.height(30.dp))
                        BoardListComponent(boardList) {  }
                        Spacer(modifier = Modifier.height(10.dp))
                        if (bottomBarView){
                            HomeBottomComponent(
                                boardTitleText = boardTitle,
                                onBoardTitleChange = {
                                    viewModel.onEvent(HomeUseCase.BoardTitleChangeEvent(it))
                                },
                                addCreateBoard = {
                                    viewModel.onEvent(HomeUseCase.CreateBoardEvent)
                                    findNavController().navigate(R.id.boardFragment)
                                }
                            )
                        }
                    }
                    FloatingActionButton(
                        modifier = Modifier.Companion
                            .padding(bottom = 65.dp, end = 20.dp)
                            .border(width = 3.dp, color = Color.White, shape = RoundedCornerShape(100))
                            .size(48.dp),
                        onClick = {  },
                        backgroundColor = PrimaryButtonColor,
                    ) {
                        Icon(
                            modifier = Modifier.size(25.dp),
                            painter = painterResource(R.drawable.ic_setting),
                            contentDescription = "FAB",
                            tint = Color.White
                        )
                    }
                }

            }
        }
    }
}