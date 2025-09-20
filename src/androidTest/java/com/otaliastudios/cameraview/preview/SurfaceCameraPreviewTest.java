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


import android.content.Context;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.SmallTest;
import android.view.ViewGroup;

import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@SmallTest
public class SurfaceCameraPreviewTest extends CameraPreviewTest<SurfaceCameraPreview> {

    @Override
    protected SurfaceCameraPreview createPreview(Context context, ViewGroup parent) {
        return new SurfaceCameraPreview(context, parent);
    }

    @Override
    protected float getCropScaleX() {
        return 1F;
    }

    @Override
    protected float getCropScaleY() {
        return 1F;
    }
}
