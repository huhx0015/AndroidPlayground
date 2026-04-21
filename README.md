# AndroidPlayground

AndroidPlayground is a personal Android/Kotlin test bed for experimenting with focused topics in UI, architecture, and concurrency.  
The project is intentionally structured as a sandbox where each screen demonstrates one concept clearly, so ideas can be tested quickly and revisited later.

## Project Summary

- **Primary goal:** test and validate Android and Kotlin patterns in a real app context
- **Style:** small, isolated topic demos rather than one large production feature
- **Use case:** learning, prototyping, and reference implementations for future projects

## Current Focus Areas

The app currently includes demos across both classic Android and Kotlin topics:

- **RecyclerView**
  - Diff two lists and update only changed rows efficiently
  - `DiffUtil.ItemCallback` with `areItemsTheSame` and `areContentsTheSame`
- **Coroutines**
  - Parallel work with `async` + `awaitAll`
  - Canceling previous jobs before launching new work
- **Jetpack Compose / Android UI**
  - Compose-based entrance/navigation screens
  - Lazy list practice and topic-oriented UI entry points

See `doc/AndroidPlaygroundTopicList.md` for the authoritative status of each topic.

## Planned Topics (Roadmap)

The topic list also tracks work that is planned but not yet implemented, including:

- RecyclerView pagination (infinite scroll)
- Repository + local cache + offline fallback patterns
- Debounced search and explicit UI state modeling
- JSON transformation pipelines (parse, flatten, group, sort, filter)
- Configuration change handling and input validation
- Offline-first architecture and threading patterns
- Services and flow-based state/event handling (`StateFlow`, `SharedFlow`)
- Additional Compose layouts (Flexbox-style and grid)
- Fragment back stack navigation examples

## Module Structure

This repo is a multi-module Gradle project:

- `app` - Android application module with feature/topic screens
- `core:architecture` - shared architecture primitives (`BaseViewModel`, intents, state, events)
- `core:domain` - domain models
- `core:data` - data-layer module (currently lightweight, intended to grow with topic coverage)

## Tech Stack

- Kotlin
- Android SDK (min SDK 24, target/compile SDK 36)
- Jetpack Compose + Material 3
- AndroidX Lifecycle / ViewModel
- Navigation Compose / Navigation 3 runtime-ui
- Kotlinx Serialization
- Coroutines

## Getting Started

### Prerequisites

- Android Studio (latest stable recommended)
- JDK 11+
- Android SDK for API 36

### Run the app

1. Open the project in Android Studio
2. Sync Gradle dependencies
3. Run the `app` configuration on an emulator or device

### Useful commands

From the repo root:

- Build debug APK: `./gradlew :app:assembleDebug`
- Run unit tests: `./gradlew test`

## How to Navigate the Project

- Start at `MainActivity` (`feature/entrance`) to access topic screens
- Explore each feature package for isolated demos
- Use `doc/AndroidPlaygroundTopicList.md` as the topic backlog and implementation tracker

## Notes

This project is intentionally iterative. Some modules/topics are complete, while others are placeholders for upcoming experiments.
