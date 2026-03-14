package com.designlife.opendraw.home.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.designlife.opendraw.R
import com.designlife.opendraw.ui.theme.PrimaryBackgroundColor
import com.designlife.opendraw.ui.theme.PrimaryButtonColor
import com.designlife.opendraw.ui.theme.boardPlaceholderStyle
import com.designlife.opendraw.ui.theme.boardTextStyle
import com.designlife.opendraw.ui.theme.buttonTextStyle

@Composable
fun HomeBottomComponent(
    boardTitleText : String,
    onBoardTitleChange: (state : String) -> Unit,
    addCreateBoard : () -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .padding(horizontal = 10.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().wrapContentHeight(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.width(12.dp))
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth(.5F)
                    .height(52.dp)
                    .align(Alignment.CenterVertically),
                colors = TextFieldDefaults.outlinedTextFieldColors(backgroundColor = Color.White, focusedBorderColor = PrimaryBackgroundColor.value, unfocusedBorderColor = PrimaryBackgroundColor.value),
                value = boardTitleText,
                shape = RoundedCornerShape(60),
                onValueChange = {value -> onBoardTitleChange(value)},
                placeholder = { Text(modifier = Modifier.fillMaxWidth(), text = "Board title ...", style = boardPlaceholderStyle.value) },
                textStyle = boardTextStyle.value,
                singleLine = true
            )
            Spacer(modifier = Modifier.width(10.dp))
            IconButton(
                modifier = Modifier
                    .size(52.dp)
                    .border(width = 2.dp, color = PrimaryButtonColor, shape = RoundedCornerShape(100))
                    .background(color = Color.White, shape = RoundedCornerShape(100)),
                onClick = {
                    keyboardController?.hide()
                    addCreateBoard() },
            ) {
                Icon(modifier = Modifier.size(20.dp), painter = painterResource(R.drawable.ic_next_done), contentDescription = "Done Icon", tint = PrimaryButtonColor)
            }
            Spacer(modifier = Modifier.width(5.dp))
        }

    }
}