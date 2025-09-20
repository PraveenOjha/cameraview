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

package com.otaliastudios.cameraview.preview;

import androidx.annotation.NonNull;

/**
 * Base interface for previews that support renderer frame callbacks,
 * see {@link RendererFrameCallback}.
 */
public interface RendererCameraPreview {

    /**
     * Adds a {@link RendererFrameCallback} to receive renderer frame events.
     * @param callback a callback
     */
    void addRendererFrameCallback(@NonNull final RendererFrameCallback callback);

    /**
     * Removes a {@link RendererFrameCallback} that was previously added to receive renderer
     * frame events.
     * @param callback a callback
     */
    void removeRendererFrameCallback(@NonNull final RendererFrameCallback callback);
}
