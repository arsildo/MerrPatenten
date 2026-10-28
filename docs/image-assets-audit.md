# WebP image migration

Completed 2026-10-28. Sizes use decimal MB (1 MB = 1,000,000 bytes).

## Result

Converted **495 shared PNGs to lossless WebP**. Kept `img_207.png` and all 15 JPEGs. Resource basenames, image IDs, dimensions, alpha, app code, and runtime dependencies are unchanged.

Assets belong to `shared-core/design-system/src/commonMain/composeResources/drawable`. Shared raster storage fell from **12,205,972 bytes to 5,280,965 bytes**: **6,925,007 bytes / 56.7% saved**. Android launcher icons already used WebP. XML vectors, documentation screenshots, and the iOS app icon are unchanged.

## Measured release packages

Built release packages before migration and after the final clean rebuild:

| Package | Before | After | Reduction |
| --- | ---: | ---: | ---: |
| Release AAB | 22,967,952 (22.97 MB) | 16,606,403 (16.61 MB) | 6.36 MB / 27.7% |
| Release APK | 24,414,009 (24.41 MB) | 17,489,840 (17.49 MB) | 6.92 MB / 28.4% |

Both final packages contain exactly **495 WebPs, one PNG, and 15 JPEGs**, each byte-identical to its final source asset. No replaced PNGs or obsolete `img_207.webp` remain packaged. Baseline app version was 3.0.4; the final build includes the existing local version bump to 3.0.5, so total package differences also include version metadata. AAB archive size is not the Play download size.

The initial incremental build retained deleted PNGs in generated resource directories. A clean rebuild removed them. Perform a clean build after switching asset extensions; do not rely on an existing incremental output directory to prove package savings.

```sh
./gradlew clean :androidApp:bundleRelease :androidApp:assembleRelease :desktopApp:jar
```

## Quality and compatibility

All 496 original PNGs were trial-converted with installed `cwebp` 1.6.0, without resizing:

```sh
cwebp -quiet -lossless -q 100 -m 6 -exact -metadata all input.png -o output.webp
```

`-q 100` controls effort in lossless mode. `-exact` preserves RGB under transparent pixels. `-metadata all` copies supported ICC/EXIF/XMP metadata.

The trial WebPs matched every original dimension and RGBA sample when independently decoded with libpng/libwebp. All 103 embedded ICC profiles were copied byte-for-byte. PNG gamma chunks all used approximate sRGB gamma (45455); PNG density metadata is not retained, but Compose derives density from resource qualifiers.

**Desktop rendering exposed one exception:** `img_207.png` is grayscale with a grayscale ICC profile. Copying that profile into RGB WebP changes how Skia interprets the image, producing channel differences up to 23/255. Keeping the original PNG avoids the color change and gives up only 2,515 bytes of compression savings. Preserve this exception in future optimizations unless a color-managed conversion is separately validated.

All **495 final WebPs** were decoded with the desktop app's Skia 0.156.0 classpath. Alpha and opaque RGB matched exactly. For 38 images, translucent RGB differed by at most 1/255 in premultiplied output, consistent with the decoders' alpha multiplication rounding. The check rejected any alpha difference, opaque-pixel difference, or larger translucent difference. Stored unpremultiplied samples remain lossless. The converted assets retain 102 ICC profiles; the grayscale profile stays in the original PNG.

`getImageResource(id)` and generated `Res.drawable` accessors retain their names. Resource generation and Android/JVM compilation passed. The inspected Compose Resources version (`1.13.10-alpha01+dev5041`) decodes with Android `BitmapFactory` and iOS/desktop Skia, both supporting WebP. No persisted filename or database migration is required.

## Desktop verification

Ran the JVM app with **Compose Hot Reload 1.3.0-beta02** and JetBrains Runtime 25:

```sh
./gradlew :desktopApp:hotRun --auto
./gradlew --no-daemon --quiet --console=plain :desktopApp:hotMcpServer
```

Used Hot Reload's MCP semantic tree and click actions to inspect catalog cards, open a sign detail, and open/close the full-screen image viewer. Screenshots showed correctly rendered signs, transparent edges, and dark backgrounds. Runtime status reported `connected: true`, `buildContinuous: true`, `reloadState: ok`, and no UI errors. A JVM decoder check covered every final WebP, beyond the sampled screens.

The Android release APK/AAB and desktop JAR build passed. An iOS simulator build also succeeded during migration; final visual QA used desktop JVM as requested. No frame-time, memory, or mobile UI benchmark was performed.

## Runtime limits and rollback

Smaller files reduce storage and encoded bytes read. They do not reduce decoded bitmap memory or GPU texture dimensions. Several images are 2000 × 2000 (about 16 MB each in RGBA). `painterResource` caches images but does not downsample to catalog-card bounds. Size-aware decoding or thumbnails would be a separate measured optimization; keep full resolution for zoom.

Revert the dedicated asset commit and clean/rebuild to roll back. Installed older versions continue using their own bundled assets; image IDs remain stable. Investigation results, original backups, decoder helpers, build logs, and desktop screenshots are disposable artifacts under `/tmp/merrpatenten-image-audit`.
