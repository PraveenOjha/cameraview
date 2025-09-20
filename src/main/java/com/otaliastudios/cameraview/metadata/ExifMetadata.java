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

package com.otaliastudios.cameraview.metadata;

import android.location.Location;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.exifinterface.media.ExifInterface;

import com.otaliastudios.cameraview.controls.Facing;
import com.otaliastudios.cameraview.filter.Filter;
import com.otaliastudios.cameraview.size.Size;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Enhanced EXIF metadata handler for camera captures.
 * Provides comprehensive metadata including device info, camera settings, filters, and location data.
 */
public class ExifMetadata {

    private final Map<String, String> metadata;
    private Location location;
    private String deviceModel;
    private String deviceManufacturer;
    private float focusDistance;
    private float aperture;
    private int iso;
    private long exposureTime;
    private String filterName;
    private String whiteBalance;
    private String flashMode;
    private Facing facing;
    private Size imageSize;
    private int orientation;
    private long timestamp;

    public ExifMetadata() {
        this.metadata = new HashMap<>();
        this.timestamp = System.currentTimeMillis();
        this.deviceModel = Build.MODEL;
        this.deviceManufacturer = Build.MANUFACTURER;
    }

    /**
     * Set location data for GPS EXIF tags
     */
    public void setLocation(@Nullable Location location) {
        this.location = location;
        if (location != null) {
            metadata.put(ExifInterface.TAG_GPS_LATITUDE, convertLocationToDMS(location.getLatitude()));
            metadata.put(ExifInterface.TAG_GPS_LATITUDE_REF, location.getLatitude() >= 0 ? "N" : "S");
            metadata.put(ExifInterface.TAG_GPS_LONGITUDE, convertLocationToDMS(location.getLongitude()));
            metadata.put(ExifInterface.TAG_GPS_LONGITUDE_REF, location.getLongitude() >= 0 ? "E" : "W");
            metadata.put(ExifInterface.TAG_GPS_ALTITUDE, String.valueOf(Math.abs(location.getAltitude())));
            metadata.put(ExifInterface.TAG_GPS_ALTITUDE_REF, location.getAltitude() >= 0 ? "0" : "1");

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss", Locale.US);
            metadata.put(ExifInterface.TAG_GPS_DATESTAMP, dateFormat.format(new Date(location.getTime())));
        }
    }

    /**
     * Set device information
     */
    public void setDeviceInfo(@NonNull String manufacturer, @NonNull String model) {
        this.deviceManufacturer = manufacturer;
        this.deviceModel = model;
        metadata.put(ExifInterface.TAG_MAKE, manufacturer);
        metadata.put(ExifInterface.TAG_MODEL, model);
        metadata.put("DeviceInfo", manufacturer + " " + model);
    }

    /**
     * Set camera settings
     */
    public void setCameraSettings(float focusDistance, float aperture, int iso, long exposureTime) {
        this.focusDistance = focusDistance;
        this.aperture = aperture;
        this.iso = iso;
        this.exposureTime = exposureTime;

        if (aperture > 0) {
            metadata.put(ExifInterface.TAG_F_NUMBER, String.valueOf(aperture));
        }
        if (iso > 0) {
            metadata.put(ExifInterface.TAG_ISO_SPEED_RATINGS, String.valueOf(iso));
        }
        if (exposureTime > 0) {
            metadata.put(ExifInterface.TAG_EXPOSURE_TIME, String.valueOf(exposureTime / 1000000000.0));
        }
        if (focusDistance >= 0) {
            metadata.put("FocusDistance", String.valueOf(focusDistance));
        }
    }

    /**
     * Set filter information
     */
    public void setFilter(@Nullable Filter filter) {
        if (filter != null) {
            this.filterName = filter.getClass().getSimpleName();
            metadata.put("FilterUsed", filterName);
            metadata.put("FilterClass", filter.getClass().getName());
        } else {
            this.filterName = "None";
            metadata.put("FilterUsed", "None");
        }
    }

    /**
     * Set white balance mode
     */
    public void setWhiteBalance(@NonNull String whiteBalance) {
        this.whiteBalance = whiteBalance;
        metadata.put(ExifInterface.TAG_WHITE_BALANCE, whiteBalance);
    }

    /**
     * Set flash mode
     */
    public void setFlashMode(@NonNull String flashMode) {
        this.flashMode = flashMode;
        metadata.put(ExifInterface.TAG_FLASH, flashMode);
    }

    /**
     * Set camera facing direction
     */
    public void setFacing(@NonNull Facing facing) {
        this.facing = facing;
        metadata.put("CameraFacing", facing.name());
    }

    /**
     * Set image size
     */
    public void setImageSize(@NonNull Size size) {
        this.imageSize = size;
        metadata.put(ExifInterface.TAG_IMAGE_WIDTH, String.valueOf(size.getWidth()));
        metadata.put(ExifInterface.TAG_IMAGE_LENGTH, String.valueOf(size.getHeight()));
    }

    /**
     * Set orientation
     */
    public void setOrientation(int orientation) {
        this.orientation = orientation;
        int exifOrientation = getExifOrientation(orientation);
        metadata.put(ExifInterface.TAG_ORIENTATION, String.valueOf(exifOrientation));
    }

    /**
     * Set timestamp
     */
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss", Locale.US);
        String formattedDate = dateFormat.format(new Date(timestamp));
        metadata.put(ExifInterface.TAG_DATETIME, formattedDate);
        metadata.put(ExifInterface.TAG_DATETIME_ORIGINAL, formattedDate);
        metadata.put(ExifInterface.TAG_DATETIME_DIGITIZED, formattedDate);
    }

    /**
     * Add custom metadata
     */
    public void addCustomMetadata(@NonNull String key, @NonNull String value) {
        metadata.put(key, value);
    }

    /**
     * Apply all metadata to an ExifInterface
     */
    public void applyToExifInterface(@NonNull ExifInterface exifInterface) {
        // Set standard timestamp if not already set
        if (!metadata.containsKey(ExifInterface.TAG_DATETIME)) {
            setTimestamp(timestamp);
        }

        // Apply all metadata
        for (Map.Entry<String, String> entry : metadata.entrySet()) {
            try {
                exifInterface.setAttribute(entry.getKey(), entry.getValue());
            } catch (Exception e) {
                // Some custom attributes might not be supported, continue with others
            }
        }

        // StableCam specific metadata
        exifInterface.setAttribute("Software", "StableCam");
        exifInterface.setAttribute("ProcessingSoftware", "StableCam Video Stabilization");

        try {
            exifInterface.saveAttributes();
        } catch (IOException e) {
            // Handle save error if needed
        }
    }

    /**
     * Get all metadata as a map
     */
    @NonNull
    public Map<String, String> getAllMetadata() {
        return new HashMap<>(metadata);
    }

    /**
     * Convert decimal degrees to DMS format for GPS coordinates
     */
    private String convertLocationToDMS(double coordinate) {
        coordinate = Math.abs(coordinate);
        int degrees = (int) coordinate;
        coordinate = (coordinate - degrees) * 60;
        int minutes = (int) coordinate;
        double seconds = (coordinate - minutes) * 60;

        return degrees + "/1," + minutes + "/1," + (int)(seconds * 1000) + "/1000";
    }

    /**
     * Convert rotation degrees to EXIF orientation
     */
    private int getExifOrientation(int rotation) {
        switch (rotation) {
            case 0: return ExifInterface.ORIENTATION_NORMAL;
            case 90: return ExifInterface.ORIENTATION_ROTATE_90;
            case 180: return ExifInterface.ORIENTATION_ROTATE_180;
            case 270: return ExifInterface.ORIENTATION_ROTATE_270;
            default: return ExifInterface.ORIENTATION_NORMAL;
        }
    }

    // Getters for all properties
    @Nullable public Location getLocation() { return location; }
    @NonNull public String getDeviceModel() { return deviceModel; }
    @NonNull public String getDeviceManufacturer() { return deviceManufacturer; }
    public float getFocusDistance() { return focusDistance; }
    public float getAperture() { return aperture; }
    public int getIso() { return iso; }
    public long getExposureTime() { return exposureTime; }
    @Nullable public String getFilterName() { return filterName; }
    @Nullable public String getWhiteBalance() { return whiteBalance; }
    @Nullable public String getFlashMode() { return flashMode; }
    @Nullable public Facing getFacing() { return facing; }
    @Nullable public Size getImageSize() { return imageSize; }
    public int getOrientation() { return orientation; }
    public long getTimestamp() { return timestamp; }
}
