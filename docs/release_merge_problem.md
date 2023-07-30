# Release merge problem (v0.13.0)

## The problem
On July 30, `publish_beta.yml` workflow released [v0.13.0-beta.9](https://github.com/lydavid/MusicSearch/releases/tag/v0.13.0-beta.9).
But I had already published v0.13.0 on `master`. So, there was a problem.

```sh
$ git show v0.13.0
commit 2836e49592af0d8ca8ad87d25bfdff23932f2248 (tag: v0.13.0, origin/master-backup, master-backup)
Author: lydavid <lydavid@users.noreply.github.com>
Date:   Tue Jul 18 23:36:57 2023 +0000

    chore: bump version [skip ci]

diff --git a/gradle.properties b/gradle.properties
index 26d869a8..1431fc00 100644
--- a/gradle.properties
+++ b/gradle.properties
@@ -6,5 +6,5 @@ kotlin.code.style=official
```

When I released v0.13.0, I first did it through a [PR](https://github.com/lydavid/MusicSearch/pull/323) and chose to rebase.
I had thought this would be equivalent to:
```sh
git checkout master
git fetch
git rebase origin/beta
git push
```
But quickly found out that wasn't the case. So I reverted the merge, forced push to `master` and `beta`, rewriting history,
then rebased on my own machine and pushed those changes.
This allowed `master` and `beta` to keep a linear history, and the same history.

What I did not realize at the time was this would throw semantic-release out of sync.
It would not notice that it had released v0.13.0, so a new feature on `beta` would become v0.13.0-beta.9 instead of v0.14.0-beta.1.

## The fix (attempt 1)

I just rebased `beta` on top of `master` from my machine, and pushed it.
`publish_production.yml` [ran](https://github.com/lydavid/MusicSearch/actions/runs/5706639782) and submitted the new v0.13.0 build to Google Play.
But it failed to publish to GitHub.
```
fatal: tag 'v0.13.0' already exists
```
I forgot to delete the tag.

## The fix (attempt 2)

This time, I deleted the release from GitHub. And ran this locally:
```sh
git tag -d v0.13.0
git push --delete origin v0.13.0
npx semantic-release --no-ci
```

This worked, but it failed to include an apk in the release.

## The fix (attempt 3)

This time, I will delete the previous release, and merge `beta` into `master` on my machine, push it, and let `publish_production.yml` handle the release.
