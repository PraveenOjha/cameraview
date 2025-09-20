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

import java.nio.ByteBuffer;

/**
 * Represents an input buffer, which means,
 * raw data that should be encoded by MediaCodec.
 */
@SuppressWarnings("WeakerAccess")
public class InputBuffer {
    public ByteBuffer data;
    public ByteBuffer source;
    public int index;
    public int length;
    public long timestamp;
    public boolean isEndOfStream;
}
