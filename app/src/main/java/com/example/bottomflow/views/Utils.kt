package com.example.bottomflow.views

import android.content.Context
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.bottomflow.R
import com.example.bottomflow.model.BottomSheetData
import com.example.bottomflow.model.PageType
import com.example.bottomflow.views.Constants.TMDB_IMAGE_BASE_URL
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_EXPANDED
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import java.util.*

fun Fragment.triggerSnackBar(message: String) =
    view?.run { Snackbar.make(this, message, Snackbar.LENGTH_SHORT).show() }

fun Fragment.loadFragment(fragment: Fragment) {
    activity?.supportFragmentManager
        ?.beginTransaction()
        ?.add(R.id.fragment_container, fragment)
        ?.addToBackStack(null)
        ?.commit()
}

fun ImageView.loadImageFromServer(url: String) {
    Glide.with(context)
        .load(TMDB_IMAGE_BASE_URL + url)
        .into(this)
}

fun Fragment.setupToolbar(showBackButton: Boolean = false, title: Int = R.string.app_name) {
    val toolbar: Toolbar = this.view!!.findViewById(R.id.bottom_flow_toolbar)
    toolbar.setTitle(title)
    if (showBackButton) {
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24)
        toolbar.setNavigationOnClickListener {
            activity?.supportFragmentManager?.popBackStack()
        }
    }
}

fun <T> ArrayList<T>.clearAndAddAll(list: List<T>) {
    this.clear()
    this.addAll(list)
}

fun PageType.getRandomPageType() =
    PageType.values()[Random().nextInt(PageType.values().size)]

object Utils {

    private val mTag = Utils::class.java.simpleName
    private var isBottomSheetInitialized = false
    private fun getBottomSheetDialog(context: Context): BottomSheetDialog =
        BottomSheetDialog(context)

    fun triggerTermsBottomSheet(context: Context, data: BottomSheetData): BottomSheetDialog? {
        val bottomSheet = getBottomSheetDialog(context)
        if (isBottomSheetInitialized) return null
        bottomSheet.let { dialogue ->
            // make true to prevent multiple instance
            isBottomSheetInitialized = true

            // set layout
            dialogue.setContentView(R.layout.bottom_sheet_layout)

            // set behavior to expanded
            val behavior = dialogue.behavior
            behavior.state = STATE_EXPANDED

            // set title, subtitle
            dialogue.findViewById<TextView>(R.id.title).also { title ->
                title?.text = context.getString(data.title)
            }
            dialogue.findViewById<TextView>(R.id.subtitle).also { subtitle ->
                subtitle?.text = context.getString(data.subtitle)
            }

            // accept and decline button
            dialogue.findViewById<TextView>(R.id.btn_accept).also { accept ->
                accept?.isVisible = data.showAccept
            }
            dialogue.findViewById<TextView>(R.id.btn_decline).also { decline ->
                decline?.isVisible = data.showDecline
            }

            bottomSheet.setOnCancelListener {
                Log.d(mTag, "Dialogue dismissed by user")
                bottomSheet.dismiss()
            }

            bottomSheet.setOnDismissListener {
                isBottomSheetInitialized = false
            }
        }
        return bottomSheet
    }
}


