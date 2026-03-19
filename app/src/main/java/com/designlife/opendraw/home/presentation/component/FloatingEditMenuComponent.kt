package com.designlife.opendraw.home.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.designlife.opendraw.R
import com.designlife.opendraw.ui.theme.PrimaryButtonColor

@Composable
fun FloatingEditMenuComponent(
    onDarkModeToggle : () -> Unit,
    onImportEvent : () -> Unit
) {
    Row(
        modifier = Modifier.padding(end =50.dp, bottom = 30.dp).wrapContentSize().background(color = Color.Transparent),
        horizontalArrangement = Arrangement.End
    ) {

        IconButton (
            modifier = Modifier.Companion
                .padding(vertical = 10.dp)
                .size(50.dp)
                .background(color = PrimaryButtonColor, shape = RoundedCornerShape(100)),
            onClick = {
                onImportEvent()
            },
        ) {
            Icon(
                modifier = Modifier.size(40.dp),
                painter = painterResource(R.drawable.ic_import),
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
                onDarkModeToggle()
            },
        ) {
            Icon(
                modifier = Modifier.size(40.dp),
                painter = painterResource(R.drawable.ic_dark),
                contentDescription = "Tooltip Icon",
                tint = Color.White
            )
        }
    }
}