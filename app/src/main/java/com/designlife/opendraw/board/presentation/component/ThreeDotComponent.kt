package com.designlife.opendraw.board.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.designlife.opendraw.R
import com.designlife.opendraw.ui.theme.LightPrimaryColorHome1
import com.designlife.opendraw.ui.theme.PrimaryButtonColor

@Composable
fun ThreeDotComponent(
    onSaveEvent : () -> Unit,
    onExportEvent : () -> Unit
) {
    Row(
        modifier = Modifier.padding(end =50.dp, top = 30.dp).wrapContentSize().background(color = Color.Transparent),
        horizontalArrangement = Arrangement.End
    ) {

        IconButton (
            modifier = Modifier.Companion
                .padding(vertical = 10.dp)
                .size(50.dp)
                .background(color = PrimaryButtonColor, shape = RoundedCornerShape(100)),
            onClick = {
                onExportEvent()
            },
        ) {
            Icon(
                modifier = Modifier.size(40.dp),
                painter = painterResource(R.drawable.ic_export),
                contentDescription = "Tooltip Icon",
                tint = Color.White
            )
        }


        Spacer(modifier = Modifier.width(6.dp))

        IconButton (
            modifier = Modifier.Companion
                .padding(vertical = 10.dp)
                .size(50.dp)
                .background(color = PrimaryButtonColor, shape = RoundedCornerShape(100)),
            onClick = {
                onSaveEvent()
            },
        ) {
            Icon(
                modifier = Modifier.size(40.dp),
                painter = painterResource(R.drawable.ic_save),
                contentDescription = "Tooltip Icon",
                tint = Color.White
            )
        }
    }
}