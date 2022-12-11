# MusicBrainzJetpackCompose

## Features

| Resource      | Search | Lookup | Browse by                             |
|---------------|--------|--------|---------------------------------------|
| area          | x      | x      |
| artist        | x      | x      |
| event         | x      | x      |
| genre         |        | x      |
| instrument    | x      | x      |
| label         | x      | x      |
| place         | x      | x      | area                                  |
| recording     | x      | x      | work                                  |
| release       | x      | x      | area, label, recording, release-group |
| release-group | x      | x      | artist                                |
| series        | x      | WIP    |
| work          | x      | x      |
| url           |        | x      |


## Code Coverage

Instrumented test coverage for `app`
`Run anything` -> `gradle app:createDebugAndroidTestCoverageReport`

Unit test coverage for `data`
`Run anything` -> `gradle data:createDebugUnitTestCoverageReport`

TODO: Merge instrumented and unit test coverage. And merge all module reports.
