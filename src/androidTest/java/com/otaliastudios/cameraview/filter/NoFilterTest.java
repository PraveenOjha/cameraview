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


import androidx.annotation.NonNull;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.SmallTest;

import com.otaliastudios.cameraview.BaseTest;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


@RunWith(AndroidJUnit4.class)
@SmallTest
public class NoFilterTest extends BaseTest {

    public static class DummyFilter extends BaseFilter {
        @NonNull
        @Override
        public String getFragmentShader() {
            return "whatever";
        }
    }

    @Test
    public void testGetFragmentShader() {
        NoFilter filter = new NoFilter();
        String defaultFragmentShader = new DummyFilter().createDefaultFragmentShader();
        assertEquals(defaultFragmentShader, filter.getFragmentShader());
    }
}
