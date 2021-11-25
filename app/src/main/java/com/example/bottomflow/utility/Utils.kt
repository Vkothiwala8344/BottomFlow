package com.example.bottomflow.utility

import android.content.Context
import android.os.Parcelable
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.bumptech.glide.Glide
import com.example.bottomflow.R
import com.example.bottomflow.utility.interfaces.RetrofitService.Companion.TMDB_IMAGE_BASE_URL
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_EXPANDED
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import kotlinx.parcelize.Parcelize
import java.util.*

object Utils {

    private val mTag = Utils::class.java.simpleName
    private var isBottomSheetInitialized = false
    private fun getBottomSheetDialog(context: Context): BottomSheetDialog =
        BottomSheetDialog(context)

    fun loadFragment(fragmentManager: FragmentManager?, fragment: Fragment) {
        fragmentManager?.beginTransaction()
            ?.replace(R.id.fragment_container, fragment)
            ?.addToBackStack(null)
            ?.commit()
    }

    fun triggerSnackBar(view: View, message: String) =
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show()

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

    fun loadImageFromServer(view: ImageView, url: String) {
        Glide.with(view.context)
            .load(TMDB_IMAGE_BASE_URL + url)
            .into(view)
    }

    fun getRandomPageType() =
        PageType.values()[Random().nextInt(PageType.values().size)]

}

@Parcelize
data class BottomSheetData(
    val title: Int,
    val subtitle: Int,
    val showAccept: Boolean = false,
    val showDecline: Boolean = false
) : Parcelable

@Parcelize
enum class PageType(val id: Int) : Parcelable {
    TOP_RATED(R.string.top_rated_movie_list),
    POPULAR(R.string.popular_movie_list),
    NOW_PLAYING(R.string.now_playing_movie_list)
}