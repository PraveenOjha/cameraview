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
 * A callback for {@link Action} state changes.
 * See the action class.
 *
 * See also {@link CompletionCallback}.
 */
@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
public interface ActionCallback {

    /**
     * Action state has just changed.
     * @param action action
     * @param state new state
     */
    void onActionStateChanged(@NonNull Action action, int state);
}
