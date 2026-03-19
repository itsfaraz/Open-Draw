package com.designlife.opendraw.home.presentation.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import coil.compose.rememberAsyncImagePainter
import com.designlife.opendraw.home.data.entity.CanvasBoard
import com.designlife.opendraw.home.domain.converters.ColorConverter
import com.designlife.opendraw.ui.theme.DarkCustomGrey
import com.designlife.opendraw.ui.theme.boardViewCategoryTextStyle
import com.designlife.opendraw.ui.theme.boardViewDateTextStyle
import com.designlife.opendraw.ui.theme.boardViewTitleTextStyle

@Composable
fun BoardListComponent(
    boardList : List<CanvasBoard>,
    onBoardSelected : (boardId : Long) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells
            .Fixed(4),
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(.8F)
            .padding(horizontal = 12.dp))
    {
        items(
            count = boardList.size
        ){ index ->
            BoardItem(boardList.get(index)) {
                onBoardSelected(boardList.get(index).id)
            }
        }
    }
}

@Composable
fun BoardItem(
    board : CanvasBoard,
    onBoardEvent : () -> Unit
) {
    val painter = rememberAsyncImagePainter(
        board.coverImage
    )
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .height(250.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(color = Color.White)
            .clickable{onBoardEvent() },
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Column(modifier = Modifier.padding(start = 10.dp).fillMaxWidth(.7F)) {
                Spacer(modifier = Modifier.height(10.dp))
                Text(text = board.category, style = boardViewCategoryTextStyle.value)
                Spacer(modifier = Modifier.height(6.dp))
                Text(text = board.boardTitle, style = boardViewTitleTextStyle.value)
                Spacer(modifier = Modifier.height(2.dp))
            }
            Text(text = board.lastModified.toString(), style = boardViewDateTextStyle.value.copy(color = Color.DarkGray))


        }

        Row(modifier = Modifier
            .fillMaxWidth()
            .height(220.dp)
        ) {
            Column(modifier = Modifier
                .fillMaxHeight()
                .width(8.dp)
                .background(color = ColorConverter.deserializeColor(board.color)),
            ) {}
            if (board.coverImage == null){
                Column(modifier = Modifier.fillMaxSize().background(Color.Green)) {  }
            }else{
                Image(
                    modifier = Modifier.fillMaxWidth(),
                    painter = painter,
                    contentScale = ContentScale.Crop,
                    contentDescription = "Cover Image"
                )
            }

        }
    }

}