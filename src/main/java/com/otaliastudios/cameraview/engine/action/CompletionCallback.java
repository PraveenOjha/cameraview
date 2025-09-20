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

import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

/**
 * A special {@link ActionCallback} that just checks for the
 * completed state. Handy as an inner anonymous class.
 */
@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
public abstract class CompletionCallback implements ActionCallback {

    @Override
    public final void onActionStateChanged(@NonNull Action action, int state) {
        if (state == Action.STATE_COMPLETED) {
            onActionCompleted(action);
        }
    }

    /**
     * The given action has just reached the completed state.
     * @param action action
     */
    protected abstract void onActionCompleted(@NonNull Action action);
}
