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

package com.otaliastudios.cameraview.video.encoding;

import android.media.MediaCodec;

import java.nio.ByteBuffer;

/**
 * Represents an output buffer, which means,
 * an encoded buffer of data that should be passed
 * to the muxer.
 */
@SuppressWarnings("WeakerAccess")
public class OutputBuffer {
    public MediaCodec.BufferInfo info;
    public int trackIndex;
    public ByteBuffer data;
}
