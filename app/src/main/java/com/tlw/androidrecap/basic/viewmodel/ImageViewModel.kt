package com.tlw.androidrecap.basic.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.tlw.androidrecap.basic.model.ContentImage
import java.util.Collections.emptyList

class ImageViewModel : ViewModel() {
    var images by mutableStateOf(emptyList<ContentImage>())
        private set

    fun updateImages(images: List<ContentImage>) {
        this.images = images
    }
}