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

package com.otaliastudios.cameraview.filter;


import android.content.res.TypedArray;

import androidx.annotation.NonNull;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.SmallTest;

import com.otaliastudios.cameraview.BaseTest;
import com.otaliastudios.cameraview.R;

import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@RunWith(AndroidJUnit4.class)
@SmallTest
public class FilterParserTest extends BaseTest {

    @Test
    public void testFallback() {
        TypedArray array = mock(TypedArray.class);
        when(array.hasValue(R.styleable.CameraView_cameraFilter)).thenReturn(false);
        when(array.getString(R.styleable.CameraView_cameraFilter)).thenReturn(null);
        FilterParser parser = new FilterParser(array);
        assertNotNull(parser.getFilter());
        assertTrue(parser.getFilter() instanceof NoFilter);
    }
    @Test
    public void testConstructor() {
        TypedArray array = mock(TypedArray.class);
        when(array.hasValue(R.styleable.CameraView_cameraFilter)).thenReturn(true);
        when(array.getString(R.styleable.CameraView_cameraFilter)).thenReturn(MyFilter.class.getName());
        FilterParser parser = new FilterParser(array);
        assertNotNull(parser.getFilter());
        assertTrue(parser.getFilter() instanceof MyFilter);
    }

    public static class MyFilter extends BaseFilter {
        @NonNull
        @Override
        public String getFragmentShader() {
            return createDefaultFragmentShader();
        }
    }
}
