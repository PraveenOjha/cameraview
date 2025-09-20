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

package com.otaliastudios.cameraview;

import android.graphics.Bitmap;
import androidx.annotation.Nullable;
import androidx.annotation.UiThread;

/**
 * Receives callbacks about a bitmap decoding operation.
 */
public interface BitmapCallback {

    /**
     * Notifies that the bitmap was successfully decoded.
     * This is run on the UI thread.
     * Returns a null object if an {@link OutOfMemoryError} was encountered.
     *
     * @param bitmap decoded bitmap, or null
     */
    @UiThread
    void onBitmapReady(@Nullable Bitmap bitmap);
}
