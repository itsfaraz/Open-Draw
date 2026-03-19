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
fun HomeHeaderComponent(
    searchState : String,
    onSearchStateChange: (state : String) -> Unit,
    onSearchEvent: () -> Unit,
    addBoardEvent : () -> Unit
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
            modifier = Modifier.fillMaxWidth(.82F).wrapContentHeight(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth(.8F)
                    .height(60.dp)
                    .align(Alignment.CenterVertically),
                colors = TextFieldDefaults.outlinedTextFieldColors(backgroundColor = Color.White, focusedBorderColor = PrimaryBackgroundColor.value, unfocusedBorderColor = PrimaryBackgroundColor.value),
                value = searchState,
                shape = RoundedCornerShape(60),
                onValueChange = {value -> onSearchStateChange(value)},
                placeholder = { Text(modifier = Modifier.fillMaxWidth(), text = "I'm Feeling Lucky ...", style = boardPlaceholderStyle.value) },
                textStyle = boardTextStyle.value,
                singleLine = true
            )
            Spacer(modifier = Modifier.width(6.dp))
            IconButton(
                modifier = Modifier
                    .size(60.dp)
                    .border(width = 2.dp, color = PrimaryButtonColor, shape = RoundedCornerShape(100))
                    .background(color = Color.White, shape = RoundedCornerShape(100)),
                onClick = {
                    keyboardController?.hide()
                    onSearchEvent() },
            ) {
                Icon(modifier = Modifier.size(25.dp), painter = painterResource(R.drawable.ic_search), contentDescription = "Search Icon", tint = PrimaryButtonColor)
            }
            Spacer(modifier = Modifier.width(5.dp))
        }

        BoxWithConstraints(modifier = Modifier.height(60.dp).fillMaxWidth()) {
            if (maxWidth >= 320.dp){
                Row(
                    modifier = Modifier.height(60.dp).fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        modifier = Modifier
                            .width(280.dp)
                            .fillMaxHeight(),
                        shape = RoundedCornerShape(100),
                        colors = ButtonDefaults.buttonColors(backgroundColor = PrimaryButtonColor),
                        onClick = { addBoardEvent() },
                    ) {
                        Row(
                            modifier = Modifier.fillMaxHeight().fillMaxWidth(),
                            horizontalArrangement = Arrangement.Start,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(modifier = Modifier.size(25.dp), painter = painterResource(R.drawable.ic_add), contentDescription = "Search Icon", tint = Color.White)
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(modifier = Modifier.fillMaxWidth(), text = "New Board", style = buttonTextStyle.value, textAlign = TextAlign.Center)
                        }
                    }
                }
            }else{
                Row(
                    modifier = Modifier.height(60.dp).fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    IconButton(
                        modifier = Modifier
                            .size(60.dp)
                            .border(width = 2.dp, color = PrimaryButtonColor, shape = RoundedCornerShape(100))
                            .background(color = Color.White, shape = RoundedCornerShape(100)),
                        onClick = { addBoardEvent() },
                    ) {
                        Icon(modifier = Modifier.size(25.dp), painter = painterResource(R.drawable.ic_add), contentDescription = "Search Icon", tint = PrimaryButtonColor)
                    }
                }
            }

        }

    }
}