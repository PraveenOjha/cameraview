/*
 * *
 *
 *  * Copyright (c)  Ilabs 2025 . All rights reserved.
 *  * Last modified 20/09/25, 2:02 am
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

import android.media.AudioFormat;

import androidx.annotation.NonNull;

import com.otaliastudios.cameraview.CameraView;

/**
 * Audio configuration to be passed as input to the constructor
 * of an {@link AudioMediaEncoder}.
 */
@SuppressWarnings("WeakerAccess")
public class AudioConfig {

    // Configurable options
    public int bitRate; // ENCODED bit rate
    public int channels = 1;
    public String encoder;
    public String mimeType = "audio/mp4a-latm";
    public int samplingFrequency = 44100; // samples/sec

    // Not configurable options (for now)
    final int encoding = AudioFormat.ENCODING_PCM_16BIT; // Determines the sampleSizePerChannel
    // The 44.1KHz frequency is the only setting guaranteed to be available on all devices.
    // If sampleSizePerChannel changes, review noise introduction
    final int sampleSizePerChannel = 2; // byte/sample/channel [16bit].
    final int byteRatePerChannel = samplingFrequency * sampleSizePerChannel; // byte/sec/channel

    // Audio input selection
    private CameraView.AudioInput audioInput = CameraView.AudioInput.DEFAULT;

    // Volume percentage 0..100 (100 = original signal)
    private int volumePercent = 100;

    public void setAudioInput(@NonNull CameraView.AudioInput input) {
        this.audioInput = input;
    }

    @NonNull
    public CameraView.AudioInput getAudioInput() {
        return audioInput;
    }

    /**
     * Sets the desired input volume for audio recording. 0 disables audio, 100 preserves original gain.
     */
    public void setVolumePercent(int percent) {
        if (percent < 0) percent = 0;
        if (percent > 100) percent = 100;
        this.volumePercent = percent;
    }

    /**
     * Returns current volume percentage.
     */
    public int getVolumePercent() {
        return volumePercent;
    }

    @NonNull
    AudioConfig copy() {
        AudioConfig config = new AudioConfig();
        config.bitRate = bitRate;
        config.channels = channels;
        config.encoder = encoder;
        config.mimeType = mimeType;
        config.samplingFrequency = samplingFrequency;
        config.audioInput = audioInput;
        config.volumePercent = volumePercent;
        return config;
    }

    int byteRate() { // RAW byte rate
        return byteRatePerChannel * channels; // byte/sec
    }

    @SuppressWarnings("unused")
    int bitRate() { // RAW bit rate
        return byteRate() * 8; // bit/sec
    }

    int audioFormatChannels() {
        if (channels == 1) {
            return AudioFormat.CHANNEL_IN_MONO;
        } else if (channels == 2) {
            return AudioFormat.CHANNEL_IN_STEREO;
        }
        throw new RuntimeException("Invalid number of channels: " + channels);
    }

    /**
     * We call FRAME here the chunk of data that we want to read at each loop cycle.
     *
     * When this number is HIGH, the AudioRecord might be unable to keep a good pace and
     * we might end up skip some frames.
     *
     * When this number is LOW, we pull a bigger number of frames and this might end up
     * delaying our recorder/encoder balance (more frames means more encoding operations).
     * In the end, this means that the recorder will skip some frames to restore the balance.
     *
     * @return the frame size
     */
    int frameSize() {
        return 1024 * channels;
    }

    /**
     * Number of frames contained in the {@link android.media.AudioRecord} buffer.
     * In theory, the higher this value is, the safer it is to delay reading as the
     * audioRecord will hold the recorded samples anyway and return to us next time we read.
     *
     * Should be coordinated with {@link #frameSize()}.
     *
     * @return the number of frames
     */
    int audioRecordBufferFrames() {
        return 50;
    }

    /**
     * We allocate buffers of {@link #frameSize()} each, which is not much.
     *
     * This value indicates the maximum number of these buffers that we can allocate at a given
     * instant. This value is the number of runnables that the encoder thread is allowed to be
     * 'behind' the recorder thread. It's not safe to have it very large or we can end encoding
     * A LOT AFTER the actual recording. It's better to reduce this and skip recording at all.
     *
     * Should be coordinated with {@link #frameSize()}.
     *
     * @return the buffer pool max size
     */
    int bufferPoolMaxSize() {
        return 500;
    }
}
