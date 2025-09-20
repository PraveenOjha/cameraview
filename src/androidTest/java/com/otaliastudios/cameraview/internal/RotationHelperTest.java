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

package com.otaliastudios.cameraview.internal;


import android.graphics.ImageFormat;
import android.graphics.YuvImage;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.SmallTest;

import com.otaliastudios.cameraview.BaseTest;
import com.otaliastudios.cameraview.internal.RotationHelper;
import com.otaliastudios.cameraview.size.Size;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(AndroidJUnit4.class)
@SmallTest
public class RotationHelperTest extends BaseTest {

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidRotation1() {
        RotationHelper.rotate(new byte[10], new Size(1, 1), -1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidRotation2() {
        RotationHelper.rotate(new byte[10], new Size(1, 1), -90);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidRotation3() {
        RotationHelper.rotate(new byte[10], new Size(1, 1), 360);
    }

    @Test
    public void testRotate() {
        // Just test that nothing happens.
        Size inputSize = new Size(160, 90);
        int inputSizeBits = inputSize.getWidth() * inputSize.getHeight() * ImageFormat.getBitsPerPixel(ImageFormat.NV21);
        int inputSizeBytes = (int) Math.ceil(inputSizeBits / 8.0d);
        byte[] input = new byte[inputSizeBytes];
        byte[] output = RotationHelper.rotate(input, inputSize, 90);
        assertEquals(input.length, output.length);

        Size outputSize = inputSize.flip();
        YuvImage image = new YuvImage(output, ImageFormat.NV21, outputSize.getWidth(), outputSize.getHeight(), null);
        assertNotNull(image);
    }
}
