# These rules will be added to the consuming modules

# Right now we're keeping this entire module,
# could we try organizing such that we only keep pojos?
-keep class ly.david.data.** { *; }
-keepclassmembers class ly.david.data.** { *; }
