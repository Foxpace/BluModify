package com.tomasrepcik.blumodify.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val customTypography = Typography(
    titleLarge = TextStyle(
        fontFamily = GloryFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 36.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    titleMedium = TextStyle(
        fontFamily = GloryFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = GloryFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 18.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    labelMedium = TextStyle(
        fontFamily = GloryFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 40.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
)