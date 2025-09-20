/*
 * *
 *
 *  * Copyright (c)  Ilabs 2025 . All rights reserved.
 *  * Last modified 19/09/25, 11:15 pm
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

import android.location.Location;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.exifinterface.media.ExifInterface;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

public class ExifUtils {
    /**
     * Writes EXIF metadata to a JPEG file. Only writes fields that are not already set by the camera/snapshot.
     *
     * @param jpegFile The JPEG file to update.
     * @param location The location to write, or null.
     * @param timestamp The timestamp to write, or null.
     * @param orientation The orientation to write, or null.
     */
    public static void writeExif(@NonNull File jpegFile, @Nullable Location location, @Nullable Date timestamp, @Nullable Integer orientation) throws IOException {
        ExifInterface exif = new ExifInterface(jpegFile.getAbsolutePath());
        if (location != null) {
            exif.setGpsInfo(location);
        }
        if (timestamp != null) {
            exif.setDateTime(timestamp.getTime());
        }
        if (orientation != null) {
            exif.setAttribute(ExifInterface.TAG_ORIENTATION, String.valueOf(orientation));
        }
        exif.saveAttributes();
    }

    /**
     * Writes a JPEG byte array to a file.
     */
    public static void writeJpegToFile(@NonNull byte[] jpegData, @NonNull File file) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(jpegData);
        }
    }
}

