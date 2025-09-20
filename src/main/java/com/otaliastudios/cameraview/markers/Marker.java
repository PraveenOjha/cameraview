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

package com.otaliastudios.cameraview.markers;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.otaliastudios.cameraview.CameraView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * A marker is an overlay over the {@link CameraView} preview, which should be drawn
 * at specific times during the camera lifecycle.
 * Currently only {@link AutoFocusMarker} is available.
 */
public interface Marker {

    /**
     * Marker is being attached to the CameraView. If a {@link View} is returned,
     * it becomes part of the hierarchy and is automatically translated (if possible)
     * to match the event place on screen, for example the point where autofocus was started
     * by the user finger.
     *
     * @param context a context
     * @param container a container
     * @return a view or null
     */
    @Nullable
    View onAttach(@NonNull Context context, @NonNull ViewGroup container);
}
