package com.termux.shared.models

import java.io.Serializable

class TextIOInfo(
    val action: String,
    val sender: String
) : Serializable {
    var editingTextDisabled: Boolean = false
    var label: String? = null
    var labelColor: Int = 0
    var labelEnabled: Boolean = false
    var labelSize: Int = 0
    var labelTypeFaceFamily: String? = null
    var labelTypeFaceStyle: Int = 0
    var showBackButtonInActionBar: Boolean = false
    var showTextCharacterUsage: Boolean = false
    var text: String? = null
    var textColor: Int = 0
    var textHorizontallyScrolling: Boolean = false
    var textLengthLimit: Int = 0
    var textSize: Int = 0
    var textTypeFaceFamily: String? = null
    var textTypeFaceStyle: Int = 0
    var title: String? = null
}
