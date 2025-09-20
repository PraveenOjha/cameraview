/*
 * *
 *
 *  * Copyright (c)  Ilabs 2025 . All rights reserved.
 *  * Last modified 20/09/25, 6:08 am
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

import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Build;

import com.otaliastudios.cameraview.controls.Facing;
import com.otaliastudios.cameraview.controls.PictureFormat;
import com.otaliastudios.cameraview.metadata.ExifMetadata;
import com.otaliastudios.cameraview.size.Size;

import java.io.File;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Wraps the picture captured by {@link CameraView#takePicture()} or
 * {@link CameraView#takePictureSnapshot()}.
 */
@SuppressWarnings("unused")
public class PictureResult {

    /**
     * A result stub, for internal use only.
     */
    public static class Stub {

        Stub() {}

        public boolean isSnapshot;
        public Location location;
        public int rotation;
        public Size size;
        public Facing facing;
        public byte[] data;
        public PictureFormat format;
        // Enhanced metadata support
        public ExifMetadata exifMetadata;
        public String filterUsed;
        public String deviceInfo;
        public float focusDistance = -1f;
        public float aperture = -1f;
        public int iso = -1;
        public long exposureTime = -1L;
    }

    private final boolean isSnapshot;
    private final Location location;
    private final int rotation;
    private final Size size;
    private final Facing facing;
    private final byte[] data;
    private final PictureFormat format;
    // Enhanced metadata fields
    private final ExifMetadata exifMetadata;
    private final String filterUsed;
    private final String deviceInfo;
    private final float focusDistance;
    private final float aperture;
    private final int iso;
    private final long exposureTime;

    PictureResult(@NonNull Stub builder) {
        isSnapshot = builder.isSnapshot;
        location = builder.location;
        rotation = builder.rotation;
        size = builder.size;
        facing = builder.facing;
        data = builder.data;
        format = builder.format;
        // Enhanced metadata
        exifMetadata = builder.exifMetadata;
        filterUsed = builder.filterUsed;
        deviceInfo = builder.deviceInfo;
        focusDistance = builder.focusDistance;
        aperture = builder.aperture;
        iso = builder.iso;
        exposureTime = builder.exposureTime;
    }

    /**
     * Returns whether this result comes from a snapshot.
     *
     * @return whether this is a snapshot
     */
    public boolean isSnapshot() {
        return isSnapshot;
    }

    /**
     * Returns geographic information for this picture, if any.
     * If it was set, it is also present in the file metadata.
     *
     * @return a nullable Location
     */
    @Nullable
    public Location getLocation() {
        return location;
    }

    /**
     * Returns the clock-wise rotation that should be applied to the
     * picture before displaying. If it is non-zero, it is also present
     * in the EXIF metadata.
     *
     * @return the clock-wise rotation
     */
    public int getRotation() {
        return rotation;
    }

    /**
     * Returns the size of the picture after the rotation is applied.
     *
     * @return the Size of this picture
     */
    @NonNull
    public Size getSize() {
        return size;
    }

    /**
     * Returns the facing value with which this video was recorded.
     *
     * @return the Facing of this video
     */
    @NonNull
    public Facing getFacing() {
        return facing;
    }

    /**
     * Returns the raw compressed, ready to be saved to file,
     * in the given format.
     *
     * @return the compressed data stream
     */
    @NonNull
    public byte[] getData() {
        return data;
    }

    /**
     * Returns the format for {@link #getData()}.
     *
     * @return the format
     */
    @NonNull
    public PictureFormat getFormat() {
        return format;
    }

    /**
     * Returns the enhanced EXIF metadata for this picture.
     *
     * @return the enhanced EXIF metadata
     */
    @Nullable
    public ExifMetadata getExifMetadata() {
        return exifMetadata;
    }

    /**
     * Returns the name of the filter used for this picture.
     *
     * @return the filter name, or null if no filter was applied
     */
    @Nullable
    public String getFilterUsed() {
        return filterUsed;
    }

    /**
     * Returns device information for this picture.
     *
     * @return device info string
     */
    @Nullable
    public String getDeviceInfo() {
        return deviceInfo;
    }

    /**
     * Returns the focus distance used for this picture.
     *
     * @return focus distance, or -1 if not available
     */
    public float getFocusDistance() {
        return focusDistance;
    }

    /**
     * Returns the aperture value used for this picture.
     *
     * @return aperture value, or -1 if not available
     */
    public float getAperture() {
        return aperture;
    }

    /**
     * Returns the ISO value used for this picture.
     *
     * @return ISO value, or -1 if not available
     */
    public int getIso() {
        return iso;
    }

    /**
     * Returns the exposure time used for this picture in nanoseconds.
     *
     * @return exposure time in nanoseconds, or -1 if not available
     */
    public long getExposureTime() {
        return exposureTime;
    }

    /**
     * Shorthand for {@link CameraUtils#decodeBitmap(byte[], int, int, BitmapCallback)}.
     * Decodes this picture on a background thread and posts the result in the UI thread using
     * the given callback.
     *
     * @param maxWidth the max. width of final bitmap
     * @param maxHeight the max. height of final bitmap
     * @param callback a callback to be notified of image decoding
     */
    public void toBitmap(int maxWidth, int maxHeight, @NonNull BitmapCallback callback) {
        if (format == PictureFormat.JPEG) {
            CameraUtils.decodeBitmap(getData(), maxWidth, maxHeight, new BitmapFactory.Options(),
                    rotation, callback);
        } else if (format == PictureFormat.DNG && Build.VERSION.SDK_INT >= 24) {
            // Apparently: BitmapFactory added DNG support in API 24.
            // https://github.com/aosp-mirror/platform_frameworks_base/blob/nougat-mr1-release/core/jni/android/graphics/BitmapFactory.cpp
            CameraUtils.decodeBitmap(getData(), maxWidth, maxHeight, new BitmapFactory.Options(),
                    rotation, callback);
        } else {
            throw new UnsupportedOperationException("PictureResult.toBitmap() does not support "
                    + "this picture format: " + format);
        }
    }

    /**
     * Shorthand for {@link CameraUtils#decodeBitmap(byte[], BitmapCallback)}.
     * Decodes this picture on a background thread and posts the result in the UI thread using
     * the given callback.
     *
     * @param callback a callback to be notified of image decoding
     */
    public void toBitmap(@NonNull BitmapCallback callback) {
        toBitmap(-1, -1, callback);
    }

    /**
     * Shorthand for {@link CameraUtils#writeToFile(byte[], File, FileCallback)}.
     * This writes this picture to file on a background thread and posts the result in the UI
     * thread using the given callback.
     *
     * @param file the file to write into
     * @param callback a callback
     */
    public void toFile(@NonNull File file, @NonNull FileCallback callback) {
        CameraUtils.writeToFile(getData(), file, callback);
    }
}
