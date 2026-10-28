# Smart Al-Quran — Android source

This is an early Android reader implementation, not a completed Quran learning application. It uses an Android WebView with bundled interface files and retrieves content over HTTPS from Al Quran Cloud. Its Android package is `com.smartalquran.app`.

## Build an APK using GitHub Actions

1. Extract the ZIP and upload the **contents of its top-level folder** to the root of a new GitHub repository. The repository root must contain `settings.gradle`, `app/`, and `.github/`.
2. In GitHub, open **Actions → Build Android APK → Run workflow** (or push a commit to trigger it). If prompted, enable Actions on the repository.
3. Once the build succeeds, open the workflow run and download the `smart-al-quran-debug-apk` artifact. Extract it and install `app-debug.apk` on an Android device. Android may require permission to install apps from your chosen source.

The APK is a debug build signed by the build environment; it is not a Play Store release. A production release requires a separately managed signing key and release build. Do not commit signing keys or credentials. This Android project does not build an iPhone app or provide a PWA installation link.

## What is implemented

- A mobile reader with green light and dark themes, transitions, reduced-motion support, a surah index and surah-name search.
- Online loading of Arabic Uthmani text and optionally an English, Urdu, or Sindhi edition via the provider; unavailable editions produce an error instead of invented content.
- Bookmarks and last-opened surah stored locally. Loaded content is cached on-device where storage permits, enabling subsequent offline reads of that content. A first load needs internet access. Clearing app data removes local data.

## Limitations and verification

The APK has not been compiled or tested on an Android device in the authoring environment, which lacks Android SDK/Gradle and cannot reach the remote content service. GitHub Actions is intended to perform the build; its outcome is not yet known. The API response, edition IDs, Arabic font rendering, offline cache behavior and translation licensing must be verified on an actual device before release. System Arabic fonts are used; a licensed Uthmani-compatible font is not bundled. The launcher uses a system icon.

No audio, Juz index, notes, word morphology, full-text search, quizzes, AI tutor, accounts, PWA service worker, or release signing is included. The app is not a substitute for independently verifying source content, accuracy and rights. The original brief's complete functionality remains unfinished.

Sources: Al Quran Cloud API (https://alquran.cloud/api) and terms (https://alquran.cloud/terms-and-conditions); Arabic Quran text attributed by that service to its source providers, including the Tanzil Project (https://tanzil.net/). Translations are attributed by edition in Settings. Verify the rights of each edition before redistribution. Cached content is saved exactly as received in API data; display uses text nodes rather than generated text.

## Local checks

`node --check app/src/main/assets/app.js`

`node tests/smoke.cjs`

If Android SDK 35, JDK 17 and Gradle 8.10.2 are installed, build with `gradle assembleDebug`.
