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

import com.otaliastudios.cameraview.CameraView;

import androidx.annotation.NonNull;
import androidx.annotation.WorkerThread;

/**
 * A FrameProcessor will process {@link Frame}s coming from the camera preview.
 * It must be passed to {@link CameraView#addFrameProcessor(FrameProcessor)}.
 */
public interface FrameProcessor {

    /**
     * Processes the given frame. The frame will hold the correct values only for the
     * duration of this method. When it returns, the frame contents will be replaced.
     *
     * To keep working with the Frame in an async manner, please use {@link Frame#freeze()},
     * which will return an immutable Frame. In that case you can pass / hold the frame for
     * as long as you want, and then release its contents using {@link Frame#release()}.
     *
     * @param frame the new frame
     */
    @WorkerThread
    void process(@NonNull Frame frame);
}
