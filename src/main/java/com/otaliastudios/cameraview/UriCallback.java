/*
 * *
 *
 *  * Copyright (c)  Ilabs 2025 . All rights reserved.
 *  * Last modified 19/09/25, 9:34 pm
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

import android.content.ContentResolver;
import android.net.Uri;

import androidx.annotation.Nullable;
import androidx.annotation.UiThread;

/**
 * Receives callbacks about a Uri write operation.
 */
public interface UriCallback {

    /**
     * Notifies that data was written to the given Uri (or failed).
     * This is run on the UI thread.
     *
     * @param uri the target uri
     * @param success true if write completed, false otherwise
     */
    @UiThread
    void onUriResult(@Nullable Uri uri, boolean success);
}

