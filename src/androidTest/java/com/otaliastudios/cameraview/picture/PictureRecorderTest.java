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

package com.otaliastudios.cameraview.picture;


import com.otaliastudios.cameraview.BaseTest;
import com.otaliastudios.cameraview.PictureResult;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.SmallTest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import java.lang.reflect.Constructor;

import static org.junit.Assert.assertNull;


@RunWith(AndroidJUnit4.class)
@SmallTest
public class PictureRecorderTest extends BaseTest {

    @Test
    public void testRecorder() throws Exception {
        PictureResult.Stub result = createStub();
        PictureRecorder.PictureResultListener listener = Mockito.mock(PictureRecorder.PictureResultListener.class);
        PictureRecorder recorder = new PictureRecorder(result, listener) {
            public void take() {
                dispatchResult();
            }
        };
        recorder.take();
        Mockito.verify(listener, Mockito.times(1)).onPictureResult(result, null);
        assertNull(recorder.mListener);
        assertNull(recorder.mResult);
    }

    private PictureResult.Stub createStub() throws Exception {
        Constructor<PictureResult.Stub> constructor = PictureResult.Stub.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        return constructor.newInstance();
    }
}
