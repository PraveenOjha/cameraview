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

import android.opengl.EGL14;

import com.otaliastudios.opengl.core.EglCore;
import com.otaliastudios.opengl.surface.EglOffscreenSurface;
import com.otaliastudios.opengl.surface.EglSurface;

import org.junit.After;
import org.junit.Before;


@SuppressWarnings("WeakerAccess")
public abstract class BaseEglTest extends BaseTest {

    protected final static int WIDTH = 100;
    protected final static int HEIGHT = 100;

    protected EglCore eglCore;
    protected EglSurface eglSurface;

    @Before
    public void setUp() {
        eglCore = new EglCore(EGL14.EGL_NO_CONTEXT, EglCore.FLAG_RECORDABLE);
        eglSurface = new EglOffscreenSurface(eglCore, WIDTH, HEIGHT);
        eglSurface.makeCurrent();
    }

    @After
    public void tearDown() {
        eglSurface.release();
        eglSurface = null;
        eglCore.release();
        eglCore = null;
    }
}
