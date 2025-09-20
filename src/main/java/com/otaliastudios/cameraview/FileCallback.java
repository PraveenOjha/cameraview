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

import java.io.File;

import androidx.annotation.Nullable;
import androidx.annotation.UiThread;

/**
 * Receives callbacks about a file saving operation.
 */
public interface FileCallback {

    /**
     * Notifies that the data was succesfully written to file.
     * This is run on the UI thread.
     * Returns a null object if an exception was encountered, for example
     * if you don't have permissions to write to file.
     *
     * @param file the written file, or null
     */
    @UiThread
    void onFileReady(@Nullable File file);
}
