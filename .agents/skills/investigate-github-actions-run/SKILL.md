---
name: investigate-github-actions-run
description: Use this to investigate GitHub Actions run. Use whenever given a url like https://github.com/lydavid/MusicSearch/actions/runs/x
---

# Investigate GitHub Actions run

Given https://github.com/lydavid/MusicSearch/actions/runs/x, run `gh run view <x> --log-failed` to see log for all failed jobs.

Given https://github.com/lydavid/MusicSearch/actions/runs/x/job/y, run `gh run view --log-failed --job=<y>` to see log for failed job.
