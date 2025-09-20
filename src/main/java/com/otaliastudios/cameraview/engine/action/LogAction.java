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

package com.otaliastudios.cameraview.engine.action;

import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.TotalCaptureResult;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.otaliastudios.cameraview.CameraLogger;
import com.otaliastudios.cameraview.engine.CameraEngine;

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
public class LogAction extends BaseAction {

    private final static CameraLogger LOG
            = CameraLogger.create(CameraEngine.class.getSimpleName());

    private String lastLog;

    @Override
    public void onCaptureCompleted(@NonNull ActionHolder holder,
                                   @NonNull CaptureRequest request,
                                   @NonNull TotalCaptureResult result) {
        super.onCaptureCompleted(holder, request, result);
        Integer aeMode = result.get(CaptureResult.CONTROL_AE_MODE);
        Integer aeState = result.get(CaptureResult.CONTROL_AE_STATE);
        Integer afState = result.get(CaptureResult.CONTROL_AF_STATE);
        Boolean aeLock = result.get(CaptureResult.CONTROL_AE_LOCK);
        Integer aeTriggerState = result.get(CaptureResult.CONTROL_AE_PRECAPTURE_TRIGGER);
        Integer afTriggerState = result.get(CaptureResult.CONTROL_AF_TRIGGER);
        String log = "aeMode: " + aeMode + " aeLock: " + aeLock +
                " aeState: " + aeState + " aeTriggerState: " + aeTriggerState +
                " afState: " + afState + " afTriggerState: " + afTriggerState;
        if (!log.equals(lastLog)) {
            lastLog = log;
            LOG.i(log);
        }
    }

    @Override
    protected void onCompleted(@NonNull ActionHolder holder) {
        super.onCompleted(holder);
        setState(0); // set another state.
        start(holder); // restart.
    }
}
