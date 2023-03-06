# Switch from Navigation Drawer to Bottom Navigation Bar

In this [commit](https://github.com/lydavid/MusicSearch/commit/25de0d69e764d5fdc7279da91f8de59ea9ebbc01), I changed the app's top-level navigation from a [navigation drawer](https://m3.material.io/components/navigation-drawer/overview) to a [navigation bar](https://m3.material.io/components/navigation-bar/overview).

Main reasons for the switch
- App does not have enough top-level destinations to warrant a drawer
- Swipe gesture conflicts with our plan to use [Accompanist's HorizontalPager](https://google.github.io/accompanist/pager/) for our tabs
  - See issue tracker: https://issuetracker.google.com/issues/167408603

