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


import com.otaliastudios.cameraview.CameraView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Constants for selecting the encoder of video recordings.
 * https://developer.android.com/guide/topics/media/media-formats.html#video-formats
 *
 * @see CameraView#setVideoCodec(VideoCodec)
 */
public enum VideoCodec implements Control {


    /**
     * Let the device choose its codec.
     */
    DEVICE_DEFAULT(0),

    /**
     * The H.263 codec.
     */
    H_263(1),

    /**
     * The H.264 codec.
     */
    H_264(2);

    static final VideoCodec DEFAULT = DEVICE_DEFAULT;

    private int value;

    VideoCodec(int value) {
        this.value = value;
    }

    int value() {
        return value;
    }

    @NonNull
    static VideoCodec fromValue(int value) {
        VideoCodec[] list = VideoCodec.values();
        for (VideoCodec action : list) {
            if (action.value() == value) {
                return action;
            }
        }
        return DEFAULT;
    }
}
