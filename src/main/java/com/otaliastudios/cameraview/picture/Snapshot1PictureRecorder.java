package com.otaliastudios.cameraview.picture;

import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;

import com.otaliastudios.cameraview.PictureResult;
import com.otaliastudios.cameraview.engine.Camera1Engine;
import com.otaliastudios.cameraview.engine.offset.Reference;
import com.otaliastudios.cameraview.internal.CropHelper;
import com.otaliastudios.cameraview.internal.ExifUtils;
import com.otaliastudios.cameraview.internal.RotationHelper;
import com.otaliastudios.cameraview.internal.WorkerHandler;
import com.otaliastudios.cameraview.size.AspectRatio;
import com.otaliastudios.cameraview.size.Size;

import androidx.annotation.NonNull;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.Date;

/**
 * A {@link PictureRecorder} that uses standard APIs.
 */
public class Snapshot1PictureRecorder extends SnapshotPictureRecorder {

    private Camera1Engine mEngine1;
    private Camera mCamera;
    private AspectRatio mOutputRatio;
    private int mFormat;

    public Snapshot1PictureRecorder(
            @NonNull PictureResult.Stub stub,
            @NonNull Camera1Engine engine,
            @NonNull Camera camera,
            @NonNull AspectRatio outputRatio) {
        super(stub, engine);
        mEngine1 = engine;
        mCamera = camera;
        mOutputRatio = outputRatio;
        mFormat = camera.getParameters().getPreviewFormat();
    }

    @Override
    public void take() {
        mCamera.setOneShotPreviewCallback(new Camera.PreviewCallback() {
            @Override
            public void onPreviewFrame(@NonNull final byte[] yuv, Camera camera) {
                dispatchOnShutter(false);

                // Got to rotate the preview frame, since byte[] data here does not include
                // EXIF tags automatically set by camera. So either we add EXIF, or we rotate.
                // Adding EXIF to a byte array, unfortunately, is hard.
                final int sensorToOutput = mResult.rotation;
                final Size outputSize = mResult.size;
                final Size previewStreamSize = mEngine1.getPreviewStreamSize(Reference.SENSOR);
                if (previewStreamSize == null) {
                    throw new IllegalStateException("Preview stream size " +
                            "should never be null here.");
                }
                WorkerHandler.execute(new Runnable() {
                    @Override
                    public void run() {
                        // Rotate the picture, because no one will write EXIF data,
                        // then crop if needed. In both cases, transform yuv to jpeg.
                        //noinspection deprecation
                        byte[] data = RotationHelper.rotate(yuv, previewStreamSize, sensorToOutput);
                        YuvImage yuv = new YuvImage(data, mFormat, outputSize.getWidth(),
                                outputSize.getHeight(), null);

                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        Rect outputRect = CropHelper.computeCrop(outputSize, mOutputRatio);
                        yuv.compressToJpeg(outputRect, 90, stream);
                        data = stream.toByteArray();

                        // Write EXIF metadata to snapshot JPEG
                        File tempFile = null;
                        try {
                            tempFile = File.createTempFile("snapshot", ".jpg");
                            ExifUtils.writeJpegToFile(data, tempFile);
                            ExifUtils.writeExif(
                                tempFile,
                                mResult.location,
                                new Date(), // Use current time as timestamp
                                mResult.rotation // Orientation
                            );
                            // Read back the file with EXIF
                            try (FileInputStream fis = new FileInputStream(tempFile)) {
                                byte[] exifData = new byte[(int) tempFile.length()];
                                fis.read(exifData);
                                data = exifData;
                            }
                        } catch (Exception ex) {
                            // If EXIF writing fails, fallback to original data
                        } finally {
                            if (tempFile != null) tempFile.delete();
                        }

                        mResult.data = data;
                        mResult.size = new Size(outputRect.width(), outputRect.height());
                        mResult.rotation = 0;
                        dispatchResult();
                    }
                });

                // It seems that the buffers are already cleared here, so we need to allocate again.
                camera.setPreviewCallbackWithBuffer(null); // Release anything left
                camera.setPreviewCallbackWithBuffer(mEngine1); // Add ourselves
                mEngine1.getFrameManager().setUp(mFormat, previewStreamSize, mEngine1.getAngles());
            }
        });
    }

    @Override
    protected void dispatchResult() {
        mEngine1 = null;
        mCamera = null;
        mOutputRatio = null;
        mFormat = 0;
        super.dispatchResult();
    }
}
