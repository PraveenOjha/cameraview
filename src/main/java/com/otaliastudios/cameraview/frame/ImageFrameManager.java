/*
 * *
 *
 *  * Copyright (c)  Ilabs 2025 . All rights reserved.
 *  * Last modified 17/09/25, 7:58 pm
 *  * Use only if permission provided by ILabs (inductiongames.com)
 *   *  * Use or edit only if permission provided by Ilbas
 *   *  * email : inductionlabs@gmail.com
 *   *  * A github invite link will be considered permission for using this code
 *   *
 *   *
 *  *
 *
 *
 */

package com.otaliastudios.cameraview.frame;

import android.media.Image;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

@RequiresApi(Build.VERSION_CODES.KITKAT)
public class ImageFrameManager extends FrameManager<Image> {

    public ImageFrameManager(int poolSize) {
        super(poolSize, Image.class);
    }

    @Override
    protected void onFrameDataReleased(@NonNull Image data, boolean recycled) {
        try {
            data.close();
        } catch (Exception ignore) {}
    }

    @NonNull
    @Override
    protected Image onCloneFrameData(@NonNull Image data) {
        throw new RuntimeException("Cannot freeze() an Image Frame. " +
                "Please consider using the frame synchronously in your process() method, " +
                "which also gives better performance.");
    }
}
