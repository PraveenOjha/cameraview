/*
 * *
 *
 *  * Copyright (c)  Ilabs 2025 . All rights reserved.
 *  * Last modified 20/09/25, 5:12 am
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

package com.otaliastudios.cameraview.streaming;

import android.view.Surface;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.otaliastudios.cameraview.CameraView;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Minimal streaming manager that exposes a simple API for starting/stopping
 * a preview stream over various protocols. This is a stub implementation
 * that can be wired to concrete encoders later.
 */
public class StreamingManager {

    public enum StreamingProtocol {
        MJPEG,
        RTMP,
        WEBRTC,
        TCP_RAW
    }

    public enum StreamingQuality {
        LOW,
        MEDIUM,
        HIGH
    }

    /**
     * Select how frames are fed to the encoder/transport layer.
     * FRAME_PROCESSOR_YUV: feed YUV bytes via FrameProcessor callbacks (CPU path).
     * DIRECT_GL_SURFACE: render GL preview (with filters) directly into an encoder input Surface (GPU path, zero-copy).
     */
    public enum RenderSource {
        FRAME_PROCESSOR_YUV,
        DIRECT_GL_SURFACE
    }

    public static class StreamingStats {
        public final StreamingProtocol protocol;
        public final int serverPort;
        public final long frameCount;
        public final long bytesTransferred;

        public StreamingStats(StreamingProtocol protocol, int serverPort, long frameCount, long bytesTransferred) {
            this.protocol = protocol;
            this.serverPort = serverPort;
            this.frameCount = frameCount;
            this.bytesTransferred = bytesTransferred;
        }
    }

    public interface StreamingListener {
        default void onStreamingStarted(StreamingProtocol protocol, int port) {}
        default void onStreamingStopped(StreamingProtocol protocol) {}
        default void onClientConnected(String clientAddress) {}
        default void onClientDisconnected(String clientAddress) {}
        default void onStreamingError(Exception error) {}
        default void onFrameStreamed(long frameCount, long bytesTransferred) {}
    }

    private final CameraView cameraView;
    private final List<StreamingListener> listeners = new CopyOnWriteArrayList<>();

    private StreamingProtocol protocol = StreamingProtocol.MJPEG;
    private StreamingQuality quality = StreamingQuality.MEDIUM;
    private RenderSource renderSource = RenderSource.FRAME_PROCESSOR_YUV;
    private int serverPort = 8080;
    private boolean running = false;

    // Stats
    private long frameCount = 0;
    private long bytesTransferred = 0;

    // Direct surface input (if using GPU path)
    @Nullable private Surface encoderInputSurface;

    public StreamingManager(@NonNull CameraView cameraView) {
        this.cameraView = cameraView;
    }

    public void addStreamingListener(@NonNull StreamingListener listener) {
        listeners.add(listener);
    }

    public void removeStreamingListener(@NonNull StreamingListener listener) {
        listeners.remove(listener);
    }

    public void setStreamingProtocol(@NonNull StreamingProtocol protocol) {
        this.protocol = protocol;
    }

    public void setStreamingQuality(@NonNull StreamingQuality quality) {
        this.quality = quality;
    }

    public void setRenderSource(@NonNull RenderSource source) {
        this.renderSource = source;
    }

    public void setServerPort(int port) {
        this.serverPort = port;
    }

    /**
     * Prepare direct GL surface rendering (zero-copy). Pass the encoder input Surface obtained from
     * your RootEncoder/MediaCodec setup. StreamingManager will render the CameraView GL preview
     * with filters into this surface when {@link RenderSource#DIRECT_GL_SURFACE} is active.
     *
     * Note: This is a stub. Wire this to your EGL shared-context renderer.
     */
    public void prepareDirectGlSurfaceMode(@NonNull Surface inputSurface) {
        this.encoderInputSurface = inputSurface;
        // TODO: Implement GL/EGL bridge. For now this is a placeholder to enable public API usage.
    }

    public synchronized void startStreaming() {
        if (running) return;
        running = true;
        // In a real implementation we'd start the chosen encoder here and bind to CameraView frames or GL surface.
        for (StreamingListener l : listeners) {
            try { l.onStreamingStarted(protocol, serverPort); } catch (Exception ignored) {}
        }
    }

    public synchronized void stopStreaming() {
        if (!running) return;
        running = false;
        for (StreamingListener l : listeners) {
            try { l.onStreamingStopped(protocol); } catch (Exception ignored) {}
        }
    }

    public boolean isStreaming() { return running; }

    @NonNull
    public StreamingStats getStreamingStats() {
        return new StreamingStats(protocol, serverPort, frameCount, bytesTransferred);
    }

    public void destroy() {
        stopStreaming();
        listeners.clear();
        encoderInputSurface = null;
    }

    // Internal helpers to update stats from encoders (not used in stub)
    void notifyFrameStreamed(int bytes) {
        frameCount++;
        bytesTransferred += Math.max(bytes, 0);
        for (StreamingListener l : listeners) {
            try { l.onFrameStreamed(frameCount, bytesTransferred); } catch (Exception ignored) {}
        }
    }

    void notifyClientConnected(@Nullable String addr) {
        for (StreamingListener l : listeners) {
            try { l.onClientConnected(addr == null ? "" : addr); } catch (Exception ignored) {}
        }
    }

    void notifyClientDisconnected(@Nullable String addr) {
        for (StreamingListener l : listeners) {
            try { l.onClientDisconnected(addr == null ? "" : addr); } catch (Exception ignored) {}
        }
    }

    void notifyError(@NonNull Exception e) {
        for (StreamingListener l : listeners) {
            try { l.onStreamingError(e); } catch (Exception ignored) {}
        }
    }
}
