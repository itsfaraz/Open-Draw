package com.designlife.opendraw.ui.theme

import androidx.compose.material.Typography
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.designlife.opendraw.R


val Typography = Typography(
    body1 = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    )
    /* Other default text styles to override
    button = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.W500,
        fontSize = 14.sp
    ),
    caption = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp
    )
    */
)

val fontFamily = FontFamily(
    Font(R.font.inconsolata_regular),
    Font(R.font.inconsolata_medium, weight = FontWeight.Medium),
    Font(R.font.inconsolata_black, weight = FontWeight.Normal),
    Font(R.font.inconsolata_bold, weight = FontWeight.Bold),
    Font(R.font.inconsolata_extrabold, weight = FontWeight.ExtraBold),
    Font(R.font.inconsolata_light, weight = FontWeight.ExtraLight),
    Font(R.font.inconsolata_expanded_light, weight = FontWeight.Light),
    Font(R.font.inconsolata_semibold, weight = FontWeight.SemiBold),
)

val headerStyleFontSize = mutableStateOf<TextUnit>(16.sp)
val contentStyle_OneSize = mutableStateOf<TextUnit>(10.sp)
val buttonStyleSize = mutableStateOf<TextUnit>(12.sp)
val commonStyleSize = mutableStateOf<TextUnit>(12.sp)
val taskItemLabelStyleSize = mutableStateOf<TextUnit>(10.sp)
val taskItemStyleSize = mutableStateOf<TextUnit>(13.sp)
val noteTitleStyleSize = mutableStateOf<TextUnit>(22.sp)
val noteContentStyleSize = mutableStateOf<TextUnit>(20.sp)
val noteItemTitleStyleSize = mutableStateOf<TextUnit>(15.sp)
val noteItemContentStyleSize = mutableStateOf<TextUnit>(12.sp)
val folderTextStyleSize = mutableStateOf<TextUnit>(12.sp)
val cardTextStyleSize = mutableStateOf<TextUnit>(18.sp)
val highlightTextStyleSize = mutableStateOf<TextUnit>(8.sp)
val deckItemTitleStyleSize = mutableStateOf<TextUnit>(16.sp)
val deckItemContentStyleSize = mutableStateOf<TextUnit>(12.sp)
val attachmentTabItemTextStyleSize = mutableStateOf<TextUnit>(13.sp)
val settingPageHeaderStyleSize = mutableStateOf<TextUnit>(26.sp)
val settingHeaderStyleSize = mutableStateOf<TextUnit>(12.sp)
val settingItemStyleSize = mutableStateOf<TextUnit>(16.sp)
val pickerItemStyleSize = mutableStateOf<TextUnit>(14.sp)















val placeholderStyleFontSize = mutableStateOf<TextUnit>(18.sp)
val boardStyleFontSize = mutableStateOf<TextUnit>(18.sp)
val buttonStyleFontSize = mutableStateOf<TextUnit>(18.sp)
val boardViewTitleStyleFontSize = mutableStateOf<TextUnit>(14.sp)
val boardViewCategoryStyleFontSize = mutableStateOf<TextUnit>(16.sp)
val boardViewDateStyleFontSize = mutableStateOf<TextUnit>(12.sp)

val boardPlaceholderStyle = mutableStateOf(TextStyle(
    color = PrimaryPlaceholderTextColor,
    fontFamily = fontFamily,
    fontSize = placeholderStyleFontSize.value,
    fontWeight = FontWeight.Medium
))

val boardTextStyle = mutableStateOf(TextStyle(
    color = Color.Black,
    fontFamily = fontFamily,
    fontSize = placeholderStyleFontSize.value,
    fontWeight = FontWeight.Medium
))

val buttonTextStyle = mutableStateOf(TextStyle(
    color = Color.White,
    fontFamily = fontFamily,
    fontSize = buttonStyleFontSize.value,
    fontWeight = FontWeight.Medium
))

val boardViewCategoryTextStyle = mutableStateOf(TextStyle(
    color = PrimaryButtonColor,
    fontFamily = fontFamily,
    fontSize = boardViewCategoryStyleFontSize.value,
    fontWeight = FontWeight.SemiBold
))

val boardViewTitleTextStyle = mutableStateOf(TextStyle(
    color = PrimaryButtonColor,
    fontFamily = fontFamily,
    fontSize = boardViewTitleStyleFontSize.value,
    fontWeight = FontWeight.Bold
))


val boardViewDateTextStyle = mutableStateOf(TextStyle(
    color = TextComponentLight,
    fontFamily = fontFamily,
    fontSize = boardViewDateStyleFontSize.value,
    fontWeight = FontWeight.Medium
))
























val addBoardTextStyle = mutableStateOf(TextStyle(
    color = Color.White,
    fontFamily = fontFamily,
    fontSize = boardStyleFontSize.value,
    fontWeight = FontWeight.Medium
))



val headerStyle = mutableStateOf(TextStyle(
    color = Color.Black,
    fontFamily = fontFamily,
    fontSize = headerStyleFontSize.value,
    fontWeight = FontWeight.Medium
))

val contentStyle_One = mutableStateOf(
    TextStyle(
        color = Color.Black,
        fontFamily = fontFamily,
        fontSize = contentStyle_OneSize.value,
        fontWeight = FontWeight.Medium
    )
)
//
//val buttonStyle = mutableStateOf(TextStyle(
//    color = Color.White,
//    fontFamily = fontFamily,
//    fontSize = buttonStyleSize.value,
//    fontWeight = FontWeight.Medium
//))

val taskItemLabelStyle = mutableStateOf(TextStyle(
    color = TaskItemLabelColor.value,
    fontFamily = fontFamily,
    fontSize = taskItemLabelStyleSize.value,
    fontWeight = FontWeight.Medium
))

val taskItemStyle = mutableStateOf(TextStyle(
    color = Color.Black,
    fontFamily = fontFamily,
    fontSize = taskItemStyleSize.value,
    fontWeight = FontWeight.Medium
))


val noteTitleStyle = mutableStateOf(TextStyle(
    color = Color.Black,
    fontFamily = fontFamily,
    fontSize = noteTitleStyleSize.value,
    fontWeight = FontWeight.SemiBold
))

val noteContentStyle = mutableStateOf(TextStyle(
    color = Color.Black,
    fontFamily = fontFamily,
    fontSize = noteContentStyleSize.value,
    fontWeight = FontWeight.Normal,
    textAlign = TextAlign.Start
))

val noteItemTitleStyle = mutableStateOf(TextStyle(
    color = Color.Black,
    fontFamily = fontFamily,
    fontSize = noteItemTitleStyleSize.value,
    fontWeight = FontWeight.SemiBold
))

val noteItemContentStyle = mutableStateOf(TextStyle(
    color = Color.Gray,
    fontFamily = fontFamily,
    fontSize = noteItemContentStyleSize.value,
    fontWeight = FontWeight.Normal,
    textAlign = TextAlign.Start
))

val folderTextStyle = mutableStateOf(TextStyle(
    color = Color.White,
    fontFamily = fontFamily,
    fontSize = folderTextStyleSize.value,
    fontWeight = FontWeight.Normal,
    textAlign = TextAlign.Start
))

val cardTextStyle = mutableStateOf(TextStyle(
    color = Color.Black,
    fontFamily = fontFamily,
    fontSize = cardTextStyleSize.value,
    fontWeight = FontWeight.Normal,
    textAlign = TextAlign.Center
))

val highlightTextStyle = mutableStateOf(TextStyle(
    color = Color.Gray,
    fontFamily = fontFamily,
    fontSize = highlightTextStyleSize.value,
    fontWeight = FontWeight.Normal,
    textAlign = TextAlign.Center
))

val deckItemTitleStyle = mutableStateOf(TextStyle(
    color = Color.Black,
    fontFamily = fontFamily,
    fontSize = deckItemTitleStyleSize.value,
    fontWeight = FontWeight.SemiBold
))

val deckItemContentStyle = mutableStateOf(TextStyle(
    color = Color.White,
    fontFamily = fontFamily,
    fontSize = deckItemContentStyleSize.value,
    fontWeight = FontWeight.Light
))

val AttachmentTabItemTextStyle = mutableStateOf(TextStyle(
    color = Color.Gray,
    fontFamily = fontFamily,
    fontSize = attachmentTabItemTextStyleSize.value,
    fontWeight = FontWeight.Normal,
    textAlign = TextAlign.Center
))

val SettingPageHeaderStyle = mutableStateOf(TextStyle(
    color = Color.Black,
    fontFamily = fontFamily,
    fontSize = settingPageHeaderStyleSize.value,
    fontWeight = FontWeight.Bold
))

val SettingHeaderStyle = mutableStateOf(TextStyle(
    color = ButtonHighLightPrimary.value,
    fontFamily = fontFamily,
    fontSize = settingHeaderStyleSize.value,
    fontWeight = FontWeight.SemiBold
))

val SettingItemStyle = mutableStateOf(TextStyle(
    color = ButtonHighLightPrimary.value,
    fontFamily = fontFamily,
    fontSize = settingItemStyleSize.value,
    fontWeight = FontWeight.Light
))


val PickerItemStyle = mutableStateOf(TextStyle(
    color = ButtonPrimary.value,
    fontFamily = fontFamily,
    fontSize = pickerItemStyleSize.value,
    fontWeight = FontWeight.Light
))
