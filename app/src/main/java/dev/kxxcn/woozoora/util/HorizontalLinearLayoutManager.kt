package dev.kxxcn.woozoora.util

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager

class HorizontalLinearLayoutManager(context: Context?) : LinearLayoutManager(context) {

    override fun setOrientation(orientation: Int) {
        super.setOrientation(HORIZONTAL)
    }
}
