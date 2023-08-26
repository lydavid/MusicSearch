# Production Release

## Steps
1. Merge `beta` into `master`
1. If the last commit was from the nightly beta release (eg. "chore: bump version [skip ci]"), then our [publish_production.yml](../.github/workflows/publish-production.yml) workflow won't run automatically. In which case, dispatch it from https://github.com/lydavid/MusicSearch/actions/workflows/publish-production.yml
   - Otherwise, it will run automatically
   - This workflow will publish a new build from master directly to production.
We publish again rather than promote the beta build so that we can change `VERSION_NAME` to match our semantic release version/tag, which on production will not include `-beta.x` suffix.

## Notes
Rather than using a GitHub PR, we should rebase `beta` onto `master` using our command line.
Using a PR will either:
- create an unnecessary merge commit when using the "Create a merge commit" option
- hide our commits from `beta` when using "Squash and merge"
- create duplicate commits when using "Rebase and merge"

Pulling `beta` into `master` with Android Studio's GUI will have the same effect, as long as the branches haven't diverge (ie no hotfixes are merged in between production releases).
