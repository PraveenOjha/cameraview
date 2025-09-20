/*
 * *
 *
 *  * Copyright (c)  Ilabs 2025 . All rights reserved.
 *  * Last modified 20/09/25, 1:16 am
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

import android.content.Context;

import androidx.annotation.Nullable;

/**
 * Holds a global application context for components that do not have
 * direct access to an Android Context (e.g., encoders).
 */
public final class GlobalContextProvider {
    private static Context sAppContext;

    private GlobalContextProvider() { }

    public static void init(Context context) {
        if (sAppContext == null && context != null) {
            sAppContext = context.getApplicationContext();
        }
    }

    @Nullable
    public static Context getContext() {
        return sAppContext;
    }
}

