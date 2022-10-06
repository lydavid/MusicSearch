# MusicBrainzJetpackCompose

## Implemented features

| Resource | Search | Lookup | Browse by |
|---|---|---|---|
| area | x | x |
| artist | x | x |
| event | x | x |
| genre | | x |
| instrument | x | x |
| label | x | x |
| place | x | x |
| recording | x | x |
| release | x | x | label, release-group |
| release-group | x | x | artist |
| series | x | WIP |
| work | x | x |
| url | | x |

Implemented search means we can query for that resource using text.

Implemented lookup means we can click that resource and go to a screen designed for it.
That screen would have multiple tabs, which may include:
- Relationships: its relation with other resources
- Stats: information about its other tabs such as how many relationships it has with each resource
- Releases: release resources associated with it

## Generate unit test coverage

`Run anything` -> `gradle app:createDebugUnitTestCoverageReport`
