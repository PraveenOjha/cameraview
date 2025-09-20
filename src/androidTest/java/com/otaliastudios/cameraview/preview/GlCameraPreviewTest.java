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

import com.otaliastudios.cameraview.filter.Filter;
import com.otaliastudios.cameraview.filter.Filters;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
@SmallTest
public class GlCameraPreviewTest extends CameraPreviewTest<GlCameraPreview> {

    @Override
    protected GlCameraPreview createPreview(Context context, ViewGroup parent) {
        return new GlCameraPreview(context, parent);
    }

    @Override
    protected float getCropScaleY() {
        return 1F / preview.mCropScaleY;
    }

    @Override
    protected float getCropScaleX() {
        return 1F / preview.mCropScaleX;
    }

    @Test
    public void testSetFilter() {
        Filter filter = Filters.BLACK_AND_WHITE.newInstance();
        preview.setFilter(filter);
        assertEquals(filter, preview.getCurrentFilter());
    }
}
