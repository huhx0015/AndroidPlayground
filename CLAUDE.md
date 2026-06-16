# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## What this is

AndroidPlayground is a sandbox app where **each screen demonstrates one Android/Kotlin concept in isolation** (RecyclerView diffing, coroutine cancellation, Compose lists, debounced search, etc.). It is not one cohesive product — treat each feature package as a standalone demo. `doc/AndroidPlaygroundTopicList.md` is the authoritative tracker of which topics are implemented vs. planned; consult and update it when adding a topic.

## Build & test

```bash
./gradlew :app:assembleDebug          # build debug APK
./gradlew test                        # all JVM unit tests (all modules)
./gradlew :app:testDebugUnitTest      # app module unit tests only
./gradlew :app:testDebugUnitTest --tests "com.huhx0015.androidplayground.feature.android.lazylist.LazyListViewModelTest"   # single test class
./gradlew :app:connectedDebugAndroidTest   # instrumented Compose/Espresso tests (needs device/emulator)
./gradlew lint                        # Android lint
```

JDK 11+, compile/target SDK 36, min SDK 24.

## Module layout

Multi-module Gradle project (see `settings.gradle.kts`):

- `app` — all feature/topic screens (the only Android *application* module)
- `core:architecture` — MVI primitives: `BaseViewModel`, `BaseState`, `BaseIntent`, `BaseEvent`
- `core:domain` — shared domain models (e.g. `DataItem`)
- `core:data` — data layer, currently a near-empty placeholder intended to grow
- `practice` — pure-JVM home of the functional-test interview katas (skeleton + paired `solutions/` + `runTest` tests, under `practice/.../implementation` and `.../debugging`). Skeleton/buggy tests are `@Ignore`'d; solution/fixed tests stay active. See `practice/README.md`. The app's `FunctionalTestPracticeActivity` runner (`app/.../practice/runner/`) depends on this module and surfaces the same exercises in a live PASS/FAIL UI; the Android `lazylist` UI practice stays in `app/.../practice/android`.

The version catalog at `gradle/libs.versions.toml` is the single source for dependency versions — add libraries there, not inline.

## Two ViewModel patterns coexist — match the one already in the feature

This codebase deliberately demonstrates two approaches. Do **not** unify them; follow whichever the feature you're touching already uses.

1. **MVI pattern** (`core:architecture`): extend `BaseViewModel<S, I, E>`. UI sends `sendIntent(intent)`; intents flow through an unbounded `Channel` to `processIntent`. State is exposed as `StateFlow<S>`, one-off effects as a `events: Flow<E>`. Used by `entrance`, `kotlin/coroutine`, `android/recyclerview`. Files: `*State.kt`, `*Intent.kt`, `*Event.kt`, `*ViewModel.kt`.

2. **Plain `ViewModel`/`AndroidViewModel`** with a `MutableStateFlow` exposed via `asStateFlow()`, mutated through `_state.update { }`, plus `SavedStateHandle` for config-change survival and (where needed) a `MutableSharedFlow` for events. Used by the newer feature flows: `android/lazylist`, `android/transactionhistory`, `android/paymentworkflow`, `android/offersdashboard`.

`SavedStateHandle` keys are stored as `companion object` constants and read in the VM's `init`/constructor to restore selection, query, and filter state.

## Feature & navigation conventions

- Every topic is its own `Activity` (`*Activity.kt`) declared in `app/src/main/AndroidManifest.xml`. `MainActivity` (`feature/entrance`, the launcher) is the hub — its ViewModel emits `EntranceEvent.StartActivity` events that the Activity collects to launch each topic via `Intent`.
- Newer Compose flows nest a list+detail navigation graph **inside one Activity** rather than using multiple Activities. Convention per feature: `composables/` (screens, all with `@Preview`), `navigation/` (a `*Route`/`*NavKey`), and the Activity just sets `AndroidPlaygroundTheme { FeatureNavigationScreen(onBackClick = ::finish) }`.
- Navigation libraries in play are mixed by design: Activity `Intent`s (entrance), Navigation Compose, and **Navigation 3** (`navigation3-runtime`/`-ui`). Older screens also use View/XML + `viewBinding` (e.g. `recyclerview`).
- Collect `events`/effects from Activities inside `repeatOnLifecycle(STARTED)` (see `MainActivity`).

## Other notes

- JSON demos use `kotlinx.serialization`; `app/.../utils/JsonUtils.kt` loads asset files (e.g. `data.json`) off `Dispatchers.IO`.
- Some demo VMs (e.g. `LazyListViewModel`) toggle between JSON-backed and mock-data sources via commented-out calls — these are intentional demo switches, not dead code to delete.
- Unit tests live in `app/src/test/...` (mirror the feature package; use `kotlinx-coroutines-test`); instrumented Compose tests in `app/src/androidTest/...`. Each `core` module has a placeholder `ExampleUnitTest`.
