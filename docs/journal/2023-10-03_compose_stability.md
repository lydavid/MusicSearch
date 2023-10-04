# Compose Stability

https://developer.android.com/jetpack/compose/performance/stability/fix#multiple-modules

https://github.com/androidx/androidx/blob/androidx-main/compose/compiler/design/compiler-metrics.md#things-to-look-out-for

https://patilshreyas.github.io/compose-report-to-html/use/using-gradle-plugin/

## Add compose runtime to :domain

### Here's :ui:history before:
https://htmlpreview.github.io/?https://github.com/lydavid/MusicSearch/blob/da6b925ab40283daa959ede3acaa53fad8314c43/ui/history/index.html

HistoryListItem was unstable because we could not infer LookupHistoryListItemModel's stability
since it belonged to a module without the Compose runtime.

### And here's it after:
https://htmlpreview.github.io/?https://github.com/lydavid/MusicSearch/blob/b6ef5b63fffdd473d4dfbfd7bdecf191e660375f/ui/history/index.html

It actually says LookupHistoryListItemModel's stability is missing, but it considers
HistoryListItem to be skippable.

```
restartable skippable scheme("[androidx.compose.ui.UiComposable]") fun HistoryListItem(
```
