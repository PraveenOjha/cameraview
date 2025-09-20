# CameraView Library + Streaming Integration (RootEncoder by PedroSG94)

CameraView is an app-agnostic Android camera library. It provides picture/video capture, gestures, filters (GL preview), enhanced EXIF metadata support, audio routing, and a StreamingManager facade you can wire to PedroSG94’s RootEncoder or RTMP/RTSP/WebRTC clients.

Use this library directly in your apps. There’s no dependency on any “StableCam” app; you only need the `cameraview` module.

---

## Getting it

You have two options:

- Vendored module (recommended during development)
  - Copy the `cameraview/` folder into your app repo under `android/cameraview`.
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
  - Clone or reference the library repo: https://github.com/PraveenOjha/cameraview.git
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

## Basic usage

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

// Optional: GL filter on preview (ensure GL_SURFACE preview)
camera.setFilter(new ScreenFilter());

// Audio
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

## Enhanced EXIF metadata

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

## Streaming (integrate with PedroSG94)

We expose a `StreamingManager` facade so your app code stays stable. Wire the internals to PedroSG94’s RootEncoder or RTMP/RTSP/WebRTC clients for production streaming.

Add JitPack and dependencies in your app if you’ll stream:
```groovy
// settings.gradle / dependencyResolutionManagement: add maven { url 'https://jitpack.io' }

// app/build.gradle
implementation 'com.github.pedroSG94:RootEncoder:<version>'
// or
implementation 'com.github.pedroSG94:rtmp-rtsp-stream-client-java:<version>'
```

YUV/CPU path (simpler): feed frames via FrameProcessor into encoder.

Zero-copy GPU path (best perf): render GL preview (with filters) into the encoder input Surface.

### StreamingManager quickstart
```java
StreamingManager sm = new StreamingManager(camera);
sm.setStreamingProtocol(StreamingManager.StreamingProtocol.RTMP);
sm.setStreamingQuality(StreamingManager.StreamingQuality.MEDIUM);
// CPU path default: RenderSource.FRAME_PROCESSOR_YUV
sm.startStreaming();
// ... later
sm.stopStreaming();
```

### Zero-copy Direct GL Surface
```java
// 1) Ensure GL filter is active
camera.setFilter(new ScreenFilter());

// 2) Create encoder and obtain input surface
Surface encoderInputSurface = /* MediaCodec.createInputSurface() or RootEncoder API */;

// 3) Use direct GL rendering
sm.setRenderSource(StreamingManager.RenderSource.DIRECT_GL_SURFACE);
sm.prepareDirectGlSurfaceMode(encoderInputSurface);
sm.setStreamingProtocol(StreamingManager.StreamingProtocol.RTMP);
sm.startStreaming();
```

Notes:
- Keep EGL context sharing and surface lifecycle management in your integration layer.
- The provided facade is a stub—add the actual RootEncoder wiring inside.

---

## React Native (Android) integration

Create a native UI component to host CameraView and expose start/stop streaming via a NativeModule.

- Vendor/import `cameraview/` into `android/` and add `implementation project(':cameraview')`.
- Create `CameraViewManager` (SimpleViewManager) to return a `new CameraView(context)`.
- Create `StableCamModule` (or `CameraModule`) exposing `startStreaming()` / `stopStreaming()` that call the manager.
- Register with a `ReactPackage` and add to `MainApplication`.
- JS usage:
  ```javascript
  const RNCameraView = requireNativeComponent('RNCameraView');
  const { StableCam } = NativeModules; // or CameraModule
  
  <RNCameraView style={{flex:1}} audioVolume={100} previewFps={30} />
  <Button title="Start" onPress={() => StableCam.startStreaming()} />
  <Button title="Stop" onPress={() => StableCam.stopStreaming()} />
  ```

Tip: For the zero‑copy path, keep EGL/Surface logic in native code and expose only simple JS commands.

---

## Features
- Picture & video capture, snapshots.
- Gesture handling (tap, pinch, scroll).
- Device orientation handling.
- GL filters on preview.
- Audio routing (DEFAULT/BLUETOOTH/USB/NONE) + live volume.
- Enhanced EXIF metadata utility.
- Streaming facade (protocol/quality, YUV and Direct GL modes) to integrate with PedroSG94.

---

## Roadmap (library)
- Provide default RootEncoder-backed implementations for RTMP/RTSP.
- Optional WebRTC and SRT transports.
- Sample endpoint configuration UI.

## Contribution
- PRs welcome. Keep code documented and tested.

## License
MIT (or the license specified in this repository).
