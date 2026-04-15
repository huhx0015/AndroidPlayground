# AndroidPlayground Topic List

This document tracks topics covered in AndroidPlayground, with a brief summary and implementation status.

## IMPLEMENTED

* **RecyclerView: Diff two lists and update UI efficiently**  
  Summary: Uses `ListAdapter` with item diffing so only changed rows are re-bound instead of refreshing the full list.  
  Where: `RecyclerViewActivity`, `RecyclerViewListAdapter`

* **RecyclerView: DiffUtil implementation**  
  Summary: Implements `DiffUtil.ItemCallback` (`areItemsTheSame`, `areContentsTheSame`) for efficient list updates.  
  Where: `RecyclerViewActivity`, `RecyclerViewListAdapter`

* **Coroutines: Fetch multiple APIs in parallel and combine results**  
  Summary: Demonstrates parallel coroutine execution using `async` + `awaitAll` (pattern applicable to combining API responses).  
  Where: `CoroutineActivity`, `CoroutineViewModel` (`runAsyncParallelDemo`)

* **Coroutines: Cancel previous requests when new ones start**  
  Summary: Cancels an active `Job` before starting new work to prevent overlapping requests/tasks.  
  Where: `CoroutineActivity`, `CoroutineViewModel` (`startLongRunningWork`)

## NOT IMPLEMENTED

* **RecyclerView: Pagination support (infinite scroll)**  
  Summary: Add paged data loading when the user approaches the end of the list.

* **Repository: Design repository that fetches from network, caches locally, returns cached data if offline**  
  Summary: Add a repository layer coordinating remote source + local cache with offline fallback.

* **Search: Debounce user input**  
  Summary: Delay search execution until typing pauses to avoid unnecessary processing/network calls.

* **State Management: Loading / Success / Error**  
  Summary: Expose explicit UI state classes/sealed models for consistent rendering and error handling.

* **JSON: Parse nested API response -> flatten into UI model**  
  Summary: Map nested DTOs into clean presentation models used by the UI.

* **JSON: Group / sort / filter data**  
  Summary: Implement transformation pipelines before rendering data in UI.

* **Screen orientation change handling**  
  Summary: Preserve and restore meaningful screen state during configuration changes.

* **Input validation**  
  Summary: Add client-side validation rules and error messaging for user inputs.

* **Offline first architecture**  
  Summary: Prioritize local data reads and background sync to improve resilience.

* **Threading**  
  Summary: Add explicit threading demonstrations or architecture patterns for UI/background workloads.

* **Services**  
  Summary: Add examples for started/bound services and lifecycle-safe long-running work.

* **StateFlow, SharedFlow**  
  Summary: Add dedicated topic/module covering hot flow patterns, replay, and one-off events.
