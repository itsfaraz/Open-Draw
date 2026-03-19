package com.designlife.opendraw.home

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.designlife.opendraw.home.domain.usecase.HomeUseCase
import com.designlife.opendraw.home.presentation.component.BoardListComponent
import com.designlife.opendraw.home.presentation.component.HomeBottomComponent
import com.designlife.opendraw.home.presentation.component.HomeHeaderComponent
import com.designlife.opendraw.home.presentation.viewmodel.HomeViewModel
import com.designlife.opendraw.home.presentation.viewmodel.HomeViewModelFactory
import com.designlife.opendraw.ui.theme.PrimaryColorHome1
import com.designlife.opendraw.R
import com.designlife.opendraw.common.utils.ServiceLocator
import com.designlife.opendraw.home.presentation.component.FloatingEditMenuComponent

class HomeFragment : Fragment() {


    private lateinit var openFileLauncher: ActivityResultLauncher<Array<String>>


    private lateinit var viewModel: HomeViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val boardRepository = ServiceLocator.provideBoardRepository(requireContext())
        val factory = HomeViewModelFactory(boardRepository)
        viewModel = ViewModelProvider(this,factory)[HomeViewModel::class.java]
        openFileLauncher =
            registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
                uri?.let {
                    // Persist permission (important!)
                    requireContext().contentResolver.takePersistableUriPermission(
                        it,
                        Intent.FLAG_GRANT_READ_URI_PERMISSION or
                                Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                    )

                    readFromUri(it)
                }
            }

    }

    private fun readFromUri(uri: Uri) {
        val text = requireContext().contentResolver
            .openInputStream(uri)
            ?.bufferedReader()
            ?.use { it.readText() }
        text?.let {
            viewModel.onEvent(HomeUseCase.OnImportEvent(text))
        }
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
                        BoardListComponent(boardList) { boardId ->
                            navigateToBoardFragmentById(boardId)
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        if (bottomBarView){
                            HomeBottomComponent(
                                boardTitleText = boardTitle,
                                onBoardTitleChange = {
                                    viewModel.onEvent(HomeUseCase.BoardTitleChangeEvent(it))
                                },
                                addCreateBoard = {
                                    viewModel.onEvent(HomeUseCase.CreateBoardEvent)
                                    navigateToBoardFragment()
                                }
                            )
                        }
                    }

                    Box(contentAlignment = Alignment.BottomEnd) {
                        FloatingEditMenuComponent(
                            onImportEvent = {
//                                viewModel.onEvent(HomeUseCase.OnImportEvent(requireContext()))
                                openFileLauncher.launch(arrayOf("*/*"))
                            },
                            onDarkModeToggle = {

                            }
                        )
                    }

                }

            }
        }
    }

    fun navigateToBoardFragmentById(boardId : Long){
        val bundle = bundleOf()
        bundle.putLong("boardId", boardId)
        bundle.putString("boardName", "")
        bundle.putLong("createdAt", 0L)
        findNavController().navigate(
            R.id.boardFragment,
            bundle
        )
        Log.d("FLOW","HomeFragment :: navigateToBoardFragmentById : Board Id = ${boardId}")
    }
    fun navigateToBoardFragment(){
        val bundle = bundleOf()
        bundle.putLong("boardId", 404L)
        bundle.putString("boardTitle", viewModel.boardTitle.value)
        bundle.putLong("createdAt", System.currentTimeMillis())
        findNavController().navigate(
            R.id.boardFragment,
            bundle
        )
        Log.d("FLOW","HomeFragment :: navigateToBoardFragment : Board Id = 404 , Board Title = ${viewModel.boardTitle.value}")

    }
}