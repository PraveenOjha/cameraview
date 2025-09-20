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

import androidx.annotation.NonNull;

/**
 * Base video configuration to be passed as input to the constructor
 * of a {@link VideoMediaEncoder}.
 */
public class VideoConfig {
    public int width;
    public int height;
    public int bitRate;
    public int frameRate;
    public int rotation;
    public String mimeType;
    public String encoder;

    protected <C extends VideoConfig> void copy(@NonNull C output) {
        output.width = this.width;
        output.height = this.height;
        output.bitRate = this.bitRate;
        output.frameRate = this.frameRate;
        output.rotation = this.rotation;
        output.mimeType = this.mimeType;
        output.encoder = this.encoder;
    }
}
