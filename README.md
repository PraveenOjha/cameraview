# CameraView Library + Streaming Integration (RootEncoder by PedroSG94)

CameraView is an app-agnostic Android camera library module you can drop into any Android app (native or React Native). It provides picture/video capture, gestures, filters (GL preview), enhanced EXIF support, audio routing, and a small StreamingManager facade designed to be wired to PedroSG94’s RootEncoder or RTMP/RTSP/WebRTC clients.

Important:
- This repo provides a library only. There’s nothing tied to any "StableCam" app.
- Use the `cameraview` module directly in your app, or fetch it from GitHub.

---

## Get the library

You have two options:

- Vendored module (recommended during development)
  - Copy the `cameraview/` folder into your app repo (for RN, under `android/cameraview`).
  - In `android/settings.gradle`:
    ```groovy
    include ':cameraview'
    project(':cameraview').projectDir = new File(rootProject.projectDir, 'cameraview')
    ```
  - In `android/app/build.gradle`:
    ```groovy
    dependencies {
      implementation project(':cameraview')
    }
    ```

- Git dependency
  - Clone or reference the library repo: <https://github.com/PraveenOjha/cameraview.git>
  - Import the module in Android Studio, or vendor it as above.

Minimum versions:
- minSdk 21, targetSdk 34, Java 8+

Permissions (AndroidManifest):
```xml
<uses-permission android:name="android.permission.CAMERA" />
<uses-permission android:name="android.permission.RECORD_AUDIO" />
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
<uses-permission android:name="android.permission.WAKE_LOCK" />
```

---

## Why RootEncoder (don’t write your own encoder)
RootEncoder by PedroSG94 provides efficient, proven audio/video encoders and surfaces we can render to. Our StreamingManager is a thin facade so your app code stays stable while the internals delegate to RootEncoder or other transports.

Add JitPack and dependencies if you’ll stream:
```groovy
// settings.gradle (or dependencyResolutionManagement)
repositories {
  maven { url 'https://jitpack.io' }
}

// app/build.gradle
implementation 'com.github.pedroSG94:RootEncoder:<version>'
// Optional transports:
implementation 'com.github.pedroSG94:rtmp-rtsp-stream-client-java:<version>'
```

---

## Basic CameraView usage

Layout:
```xml
<com.otaliastudios.cameraview.CameraView
    android:id="@+id/camera"
    android:layout_width="match_parent"
    android:layout_height="match_parent"/>
```

Activity/Fragment:
```java
CameraView camera = findViewById(R.id.camera);
camera.setLifecycleOwner(this);
camera.setEngine(Engine.CAMERA2);
camera.setMode(Mode.VIDEO);
camera.setExperimental(true);
camera.setPreviewFrameRate(30f);

// Optional: GL filter on preview (ensure GL surface preview)
camera.setFilter(new ScreenFilter());

// Audio controls
camera.setAudio(Audio.STEREO);                       // OFF, ON, MONO, STEREO
camera.setAudioInput(CameraView.AudioInput.DEFAULT); // DEFAULT, BLUETOOTH, USB, NONE
camera.setAudioVolume(100);                          // 0..100
```

Captures:
```java
// Picture (full quality)
camera.takePicture();
// Snapshot (fast, GL preview)
camera.takePictureSnapshot();
// Video
tmpFile = ...
camera.takeVideo(tmpFile);
// Stop
camera.stopVideo();
```

---

## Streaming with StreamingManager (RootEncoder-first)

We expose `com.otaliastudios.cameraview.streaming.StreamingManager` for a simple, stable API:
- start/stop streaming
- change protocol (MJPEG, RTMP, WEBRTC, TCP_RAW)
- change quality (LOW, MEDIUM, HIGH)
- select render source (CPU YUV vs. Direct GL Surface)
- observe events (started/stopped, client connect, errors)

Quickstart:
```java
StreamingManager sm = new StreamingManager(camera);
sm.setStreamingProtocol(StreamingManager.StreamingProtocol.RTMP);
sm.setStreamingQuality(StreamingManager.StreamingQuality.MEDIUM);
sm.setRenderSource(StreamingManager.RenderSource.FRAME_PROCESSOR_YUV); // default
sm.startStreaming();
// ... later
sm.stopStreaming();
```

### Change audio at runtime
```java
camera.setAudio(Audio.OFF);                    // mute / unmute
camera.setAudio(Audio.STEREO);
camera.setAudioInput(CameraView.AudioInput.BLUETOOTH); // route mic
camera.setAudioVolume(60);                     // live gain 0-100
```

### Change streaming quality at runtime
```java
sm.setStreamingQuality(StreamingManager.StreamingQuality.HIGH);
```

### CPU path (YUV via FrameProcessor)
Feed preview frames into RootEncoder if you don’t need GL filters or can accept CPU copies.
```java
camera.addFrameProcessor(frame -> {
  // Get YUV data from frame (NV21/YUV420)
  ByteBuffer y = frame.getData(0);
  // Convert as needed and push to RootEncoder video input.
  // rootEncoderVideo.queueVideo(y, width, height, timestampUs);
});
```

### Zero‑copy Direct GL Surface (render your filtered surface directly)
Render the GL preview (with your active filter) straight into the encoder input Surface.
```java
// 1) Activate your GL filter
camera.setFilter(new ScreenFilter());

// 2) Create video encoder using RootEncoder and obtain its input Surface
Surface encoderInputSurface = /* RootEncoder video input surface */;

// 3) Wire StreamingManager to render directly to that surface
sm.setRenderSource(StreamingManager.RenderSource.DIRECT_GL_SURFACE);
sm.prepareDirectGlSurfaceMode(encoderInputSurface);
sm.setStreamingProtocol(StreamingManager.StreamingProtocol.RTMP);
sm.startStreaming();
```
Notes:
- This is the preferred path for performance and to preserve your GL effects.
- Manage EGL shared context and surface lifecycle in your integration code.

---

## Filters: preview, video, and picture snapshot

CameraView applies the current GL filter to:
- Preview (what you see on screen)
- Video recording (both full video and video snapshot)
- Picture snapshot (fast GL capture)

You can use a different filter for picture snapshots only via a dedicated override. By default:
- Video uses whatever filter is active on the preview when recording starts.
- Picture snapshot uses the current preview filter, unless you set a snapshot-override filter.

Set the preview/video filter:
```java
// Affects preview immediately and will be used for video recording
displayFilter = new GrayscaleFilter();
camera.setFilter(displayFilter);
```

Use a different filter for picture snapshots only:
```java
import com.otaliastudios.cameraview.picture.SnapshotGlPictureRecorder;

// Apply a custom filter only when taking picture snapshots
SnapshotGlPictureRecorder.setOverrideFilter(new LomoishFilter());

// Later, to restore default behavior (use the preview filter for snapshots):
SnapshotGlPictureRecorder.setOverrideFilter(null);
```

If you need a different filter during video recording (distinct from preview), a simple pattern is:
```java
Filter old = displayFilter;                  // current preview filter
Filter recordFilter = new DocumentaryFilter();
camera.setFilter(recordFilter);              // switch just before recording
camera.takeVideo(file);
// ... when you stop:
camera.stopVideo();
camera.setFilter(old);                       // restore preview filter
```

Note: Video snapshot and full video both consume the active preview filter unless you temporarily change it as shown above.

### How to write a custom Filter
A filter is an OpenGL shader pair with a tiny lifecycle. Prefer extending BaseFilter.

Requirements:
- Public no-arg constructor
- Implement copy() to clone parameters
- Provide vertex and fragment shader strings (or reuse BaseFilter defaults)

Minimal example:
```java
public class MyCoolFilter extends BaseFilter {
  private float intensity = 0.5f; // 0..1

  public MyCoolFilter() { }

  @NonNull @Override
  public String getFragmentShader() {
    // Sample: multiply color by intensity
    return "precision mediump float;\n" +
           "varying vec2 vTextureCoord;\n" +
           "uniform sampler2D sTexture;\n" +
           "uniform float uIntensity;\n" +
           "void main(){ vec4 c = texture2D(sTexture, vTextureCoord);\n" +
           "  gl_FragColor = vec4(c.rgb * uIntensity, c.a); }";
  }

  @Override public void onCreate(int programHandle) {
    super.onCreate(programHandle);
    // e.g. fetch uniform locations, set defaults
    setUniform1f("uIntensity", intensity);
  }

  @Override public void setSize(int width, int height) {
    super.setSize(width, height);
    // update any size-dependent uniforms here
  }

  @NonNull @Override public Filter copy() {
    MyCoolFilter f = new MyCoolFilter();
    f.intensity = this.intensity;
    return f;
  }

  // Optional setter the app can call at runtime
  public void setIntensity(float value01) {
    intensity = Math.max(0f, Math.min(1f, value01));
    setUniform1f("uIntensity", intensity);
  }
}
```
Use it:
```java
camera.setFilter(new MyCoolFilter());
```
Tip: See built-in examples under `com.otaliastudios.cameraview.filters.*` for more patterns.

---

## Recording video and video snapshot (Mode.VIDEO required)

Set video mode before recording:
```java
camera.setMode(Mode.VIDEO);
```

Full video recording:
```java
File file = new File(getExternalFilesDir(null), "video.mp4");
camera.takeVideo(file);
// ... when done
camera.stopVideo();
```

Video snapshot (fast, GL-based):
```java
// Requires API 18+ and GL preview surface
camera.setPreview(Preview.GL_SURFACE);
File file = new File(getExternalFilesDir(null), "video-snap.mp4");
camera.takeVideoSnapshot(file);
// ... when done
camera.stopVideo();
```
Notes:
- Video snapshot and full video both use the active preview filter at record time.
- Picture snapshot uses the active preview filter unless overridden via `SnapshotGlPictureRecorder.setOverrideFilter()`.

---

## In‑app Receiver (host a stream you can remote‑control)
You can host a lightweight in‑app receiver so other clients can connect to your device and view/control the stream. There are multiple approaches:

- MJPEG over HTTP (simple, works everywhere)
  - Run a tiny HTTP server (e.g., NanoHTTPD) and push JPEGs from a FrameProcessor at an interval.
  - Expose simple REST endpoints for remote control (start/stop, switch camera, flash, filters).

- TCP_RAW (custom binary)
  - Stream raw H264/HEVC or YUV frames over a TCP socket from a FrameProcessor.
  - Pair with a matching desktop/web client.

- RTMP/RTSP receiver (advanced)
  - This library does not ship an RTMP/RTSP server. If you truly need an in‑app RTMP/RTSP receiver, integrate a third‑party server or implement one, then plug it into StreamingManager events.
  - For most apps, prefer MJPEG/TCP for local control or use an external RTMP server (NMS, Wowza, etc.).

Example (MJPEG sketch):
```java
sm.setStreamingProtocol(StreamingManager.StreamingProtocol.MJPEG);
sm.setRenderSource(StreamingManager.RenderSource.FRAME_PROCESSOR_YUV);
// Use a small HTTP server to serve multipart/x-mixed-replace with JPEG frames from a FrameProcessor.
sm.startStreaming();
```

Remote control ideas:
- Define HTTP endpoints: `/start`, `/stop`, `/swap`, `/flash/on|off`, `/audio/off|stereo`, `/quality/low|med|high`.
- Inside handlers, call into CameraView and StreamingManager (same methods shown above).

---

## React Native (Android) quickstart (LLM-friendly)
These steps let a tool or developer scaffold a RN app that uses this library.

1. Add module under `android/`
```bat
REM Assuming your RN app is at MyApp/
cd MyApp\android
git clone https://github.com/PraveenOjha/cameraview.git cameraview
```

2. Wire settings.gradle
```groovy
// android/settings.gradle
include ':cameraview'
project(':cameraview').projectDir = new File(rootProject.projectDir, 'cameraview')
```

3. Add dependency
```groovy
// android/app/build.gradle
dependencies {
  implementation project(':cameraview')
  // Streaming deps if needed
  implementation 'com.github.pedroSG94:RootEncoder:<version>'
  implementation 'com.github.pedroSG94:rtmp-rtsp-stream-client-java:<version>'
}
```

4. Create a native UI ViewManager that hosts CameraView (Android)
- Java example outline:
```java
public class RNCameraViewManager extends SimpleViewManager<CameraView> {
  @NonNull @Override public String getName() { return "RNCameraView"; }
  @NonNull @Override protected CameraView createViewInstance(@NonNull ThemedReactContext c) {
    CameraView v = new CameraView(c);
    v.setLifecycleOwner((LifecycleOwner) c.getCurrentActivity());
    v.setEngine(Engine.CAMERA2);
    v.setMode(Mode.VIDEO);
    return v;
  }
}
```

5. Create a NativeModule for streaming controls
```java
public class CameraModule extends ReactContextBaseJavaModule {
  private StreamingManager sm; private CameraView cv;
  @NonNull @Override public String getName() { return "CameraModule"; }
  @ReactMethod public void init() { /* obtain cv reference; sm = new StreamingManager(cv); */ }
  @ReactMethod public void start(String url) { sm.setStreamingProtocol(StreamingManager.StreamingProtocol.RTMP); sm.startStreaming(); }
  @ReactMethod public void stop() { sm.stopStreaming(); }
  @ReactMethod public void setQuality(String q) { /* map to LOW/MEDIUM/HIGH */ }
  @ReactMethod public void setAudio(String mode) { /* OFF/STEREO -> camera.setAudio(...) */ }
}
```

6. JS usage
```javascript
import { requireNativeComponent, NativeModules } from 'react-native';
const RNCameraView = requireNativeComponent('RNCameraView');
const { CameraModule } = NativeModules;

<RNCameraView style={{flex:1}} />
<Button title="Start" onPress={() => CameraModule.start('rtmp://server/app/stream')} />
<Button title="Stop" onPress={() => CameraModule.stop()} />
```

Build on Windows (from MyApp/android):
```bat
gradlew.bat clean assembleDebug
```

Notes:
- For zero‑copy GL streaming, keep EGL/Surface work in native code; expose simple JS commands only.
- Make sure to request CAMERA and RECORD_AUDIO permissions at runtime.

---

## Enhanced EXIF metadata (optional)
```java
ExifMetadata meta = new ExifMetadata();
meta.setDeviceInfo(Build.MANUFACTURER, Build.MODEL);
meta.setTimestamp(System.currentTimeMillis());
meta.setLocation(camera.getLocation());
meta.setFacing(camera.getFacing());
meta.setWhiteBalance(camera.getWhiteBalance().toString());
meta.setFlashMode(camera.getFlash().toString());
meta.setCameraSettings(2.0f, 1.8f, 100, 1_000_000L);
meta.addCustomMetadata("AudioInput", camera.getAudioInput().toString());
meta.addCustomMetadata("AudioVolume", String.valueOf(camera.getAudioVolume()));

File pictureFile = ...
ExifInterface exif = new ExifInterface(pictureFile.getAbsolutePath());
meta.applyToExifInterface(exif);
```

Set a location to embed in future results:
```java
Location loc = new Location("CameraView");
loc.setLatitude(...);
loc.setLongitude(...);
loc.setTime(System.currentTimeMillis());
camera.setLocation(loc);
```

---

## Features
- Picture & video capture, snapshots.
- Gesture handling (tap, pinch, scroll).
- Device orientation handling.
- GL filters on preview.
- Audio routing (DEFAULT/BLUETOOTH/USB/NONE) + live volume.
- Streaming facade (protocol/quality, YUV and Direct GL modes) to integrate with PedroSG94.
- Hooks for building in‑app receivers (MJPEG/TCP) and remote control endpoints.

## Roadmap (library)
- Provide default RootEncoder-backed implementations for RTMP/RTSP.
- Optional WebRTC and SRT transports.
- Sample endpoint configuration UI and an MJPEG receiver sample.

## Contribution
PRs welcome. Keep code documented and tested.

## License
MIT (or the license specified in this repository).
