package com.tlw.androidrecap.basic.viewmodel

import android.content.ContentResolver
import android.content.ContentUris
import android.provider.MediaStore
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tlw.androidrecap.basic.model.ContentImage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Calendar

enum class TimeFilter(val label: String) {
    LAST_HOUR("Last 1 hour"),
    LAST_DAY("Last 1 day"),
    LAST_WEEK("Last 7 days"),
    LAST_MONTH("Last 30 days"),
    ALL("All images")
}

class ImageViewModel : ViewModel() {

    var images by mutableStateOf(emptyList<ContentImage>())
        private set

    var selectedFilter by mutableStateOf(TimeFilter.ALL)
        private set

    fun loadImages(contentResolver: ContentResolver, filter: TimeFilter) {
        selectedFilter = filter
        viewModelScope.launch(Dispatchers.IO) {
            val result = queryImages(contentResolver, filter)
            withContext(Dispatchers.Main) { images = result }
        }
    }

    private fun queryImages(contentResolver: ContentResolver, filter: TimeFilter): List<ContentImage> {
        val projection = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DISPLAY_NAME
        )

        val selection: String?
        val selectionArgs: Array<String>?

        if (filter == TimeFilter.ALL) {
            selection = null
            selectionArgs = null
        } else {
            val cutoff = Calendar.getInstance().apply {
                when (filter) {
                    TimeFilter.LAST_HOUR -> add(Calendar.HOUR_OF_DAY, -1)
                    TimeFilter.LAST_DAY  -> add(Calendar.DAY_OF_YEAR, -1)
                    TimeFilter.LAST_WEEK -> add(Calendar.DAY_OF_YEAR, -7)
                    TimeFilter.LAST_MONTH -> add(Calendar.DAY_OF_YEAR, -30)
                    TimeFilter.ALL -> {}
                }
            }.timeInMillis
            selection = "${MediaStore.Images.Media.DATE_TAKEN} >= ?"
            selectionArgs = arrayOf(cutoff.toString())
        }

        val result = mutableListOf<ContentImage>()
        contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection,
            selection,
            selectionArgs,
            "${MediaStore.Images.Media.DATE_TAKEN} DESC"
        )?.use { cursor ->
            val idCol = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            val nameCol = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)
            while (cursor.moveToNext()) {
                val id = cursor.getLong(idCol)
                result.add(
                    ContentImage(
                        id = id,
                        name = cursor.getString(nameCol),
                        uri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)
                    )
                )
            }
        }
        return result
    }
}
