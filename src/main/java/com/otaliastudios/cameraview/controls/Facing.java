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

package com.otaliastudios.cameraview.controls;


import android.content.Context;

import com.otaliastudios.cameraview.CameraUtils;
import com.otaliastudios.cameraview.CameraView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Facing value indicates which camera sensor should be used for the current session.
 *
 * @see CameraView#setFacing(Facing)
 */
public enum Facing implements Control {

    /**
     * Back-facing camera sensor.
     */
    BACK(0),

    /**
     * Front-facing camera sensor.
     */
    FRONT(1);

    @NonNull
    static Facing DEFAULT(@Nullable Context context) {
        if (context == null) {
            return BACK;
        } else if (CameraUtils.hasCameraFacing(context, BACK)) {
            return BACK;
        } else if (CameraUtils.hasCameraFacing(context, FRONT)) {
            return FRONT;
        } else {
            // The controller will throw a CameraException.
            // This device has no cameras.
            return BACK;
        }
    }

    private int value;

    Facing(int value) {
        this.value = value;
    }

    int value() {
        return value;
    }

    @Nullable
    static Facing fromValue(int value) {
        Facing[] list = Facing.values();
        for (Facing action : list) {
            if (action.value() == value) {
                return action;
            }
        }
        return null;
    }
}
