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

package com.otaliastudios.cameraview.engine.orchestrator;

import androidx.annotation.NonNull;

public enum CameraState {
    OFF(0), ENGINE(1), BIND(2), PREVIEW(3);

    private int mState;

    CameraState(int state) {
        mState = state;
    }

    public boolean isAtLeast(@NonNull CameraState reference) {
        return mState >= reference.mState;
    }
}
