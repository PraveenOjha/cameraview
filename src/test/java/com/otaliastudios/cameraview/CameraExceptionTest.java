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

package com.otaliastudios.cameraview;


import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CameraExceptionTest {

    @Test
    public void testConstructor() {
        RuntimeException cause = new RuntimeException("Error");
        CameraException camera = new CameraException(cause);
        assertEquals(cause, camera.getCause());
    }
}
