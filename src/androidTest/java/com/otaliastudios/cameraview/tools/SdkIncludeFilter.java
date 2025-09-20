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

package com.otaliastudios.cameraview.tools;


import android.os.Build;

import androidx.annotation.Nullable;
import androidx.test.internal.runner.filters.ParentFilter;

import org.junit.runner.Description;

/**
 * Filter for {@link SdkInclude}, based on
 * {@link androidx.test.internal.runner.TestRequestBuilder}'s SdkSuppressFilter.
 */
public class SdkIncludeFilter extends ParentFilter {

    protected boolean evaluateTest(Description description) {
        SdkInclude annotation = getAnnotationForTest(description);
        if (annotation != null) {
            if ((!annotation.emulatorOnly() || Emulator.isEmulator())
                    && Build.VERSION.SDK_INT >= annotation.minSdkVersion()
                    && Build.VERSION.SDK_INT <= annotation.maxSdkVersion()) {
                return true; // run the test
            }
            return false; // drop the test
        }
        return true; // no annotation, run the test
    }

    @Nullable
    private SdkInclude getAnnotationForTest(Description description) {
        final SdkInclude s = description.getAnnotation(SdkInclude.class);
        if (s != null) {
            return s;
        }
        final Class<?> testClass = description.getTestClass();
        if (testClass != null) {
            return testClass.getAnnotation(SdkInclude.class);
        }
        return null;
    }

    @Override
    public String describe() {
        return "Skip tests annotated with SdkInclude";
    }
}
