# Images

This shows the data flow for image url and data.

```mermaid
flowchart TD
    
    subgraph External 
        subgraph API 
            MusicBrainzEndpoint["musicbrainz.org"]
            CoverArtArchiveEndpoint["coverartarchive.org"]
            SpotifyEndpoint["api.spotify.com"]
        end
    end

    subgraph Ktor
        MusicBrainzApi
        CoverArtArchiveApi
        SpotifyApi
    end
    
    subgraph SQLDelight
        subgraph Database
            artist
            release
            release_group
            mbid_image
        end
        
        ArtistDao
        ReleaseDao
        ReleaseGroupDao
        ImageUrlDao

        artist --> ArtistDao
        release --> ReleaseDao
        release_group --> ReleaseGroupDao
        
        mbid_image --> ArtistDao
        mbid_image --> ReleaseDao
        mbid_image --> ReleaseGroupDao
        mbid_image --> ImageUrlDao
    end

    CoverArtArchiveEndpoint --> CoverArtArchiveApi
    SpotifyEndpoint --> SpotifyApi
    MusicBrainzEndpoint --> MusicBrainzApi

    MusicBrainzApi --> ArtistRepository
    ArtistDao --> ArtistRepository

    MusicBrainzApi --> ReleaseRepository
    ReleaseDao --> ReleaseRepository

    MusicBrainzApi --> ReleaseGroupRepository
    ReleaseGroupDao --> ReleaseGroupRepository

    CoverArtArchiveApi --> ReleaseGroupImageRepository
    ImageUrlDao --> ReleaseGroupImageRepository

    CoverArtArchiveApi --> ReleaseImageRepository
    ImageUrlDao --> ReleaseImageRepository

    SpotifyApi --> ArtistImageRepository
    ImageUrlDao --> ArtistImageRepository
```

Image urls are stored in their own table `mbid_image`.
When we query an entity from their entity table (e.g. `release`, we join it with this table.

If there were no entries in `mbid_image` for a given `mbid`, a `*ImageRepository` would request for it,
then store it, so that the next time we query our database, we will have it.


```mermaid
flowchart TD
    subgraph External
        subgraph CDN
            InternetArchiveCdn["Internet Archive CDN"]
            SpotifyCdn["Spotify CDN"]
        end
    end
    InternetArchiveCdn --image data--> Coil
    SpotifyCdn --image data--> Coil

    subgraph Repositories
        ArtistImageRepository
        ReleaseImageRepository
        ReleaseGroupImageRepository

        ArtistRepository
        ReleaseRepository
        ReleaseGroupRepository
    end
    
    subgraph IntermediatePresenters
        ArtistsByEntityPresenter
        ReleasesByEntityPresenter
        ReleaseGroupsByEntityPresenter
    end
    
    subgraph Presenters 
        ArtistPresenter
        ReleasePresenter
        ReleaseGroupPresenter
    end

    ArtistImageRepository --> ArtistPresenter

    ReleaseImageRepository --> ReleasePresenter
    ReleaseImageRepository --> ReleasesByEntityPresenter
    
    ReleaseGroupImageRepository --> ReleaseGroupPresenter
    ReleaseGroupImageRepository --> ReleaseGroupsByEntityPresenter

    ArtistRepository --> ArtistPresenter
    ReleaseRepository --> ReleasePresenter
    ReleaseGroupRepository --> ReleaseGroupPresenter
    
    subgraph UI
        ArtistDetailsUi
        ReleaseDetailsUi
        ReleaseGroupDetailsUi

        ReleasesListScreen
        ReleaseGroupsListScreen
    end
    
    ArtistPresenter --> ArtistDetailsUi
    ReleasePresenter --> ReleaseDetailsUi
    ReleaseGroupPresenter --> ReleaseGroupDetailsUi
    
    ReleasesByEntityPresenter --> ReleasesListScreen
    ReleaseGroupsByEntityPresenter --> ReleaseGroupsListScreen
    
    UI -- mbid, url --> Coil
```

Given a url, Coil will fetch the image data from a CDN and render it.

`mbid` is used as `memoryCacheKey` and `placeholderMemoryCacheKey`, 
allowing us to reuse the thumbnail as a placeholder while Coil fetches the full-sized image. 

`*DetailsUi` displays a full-sized image given a `*ScaffoldModel`.
`*ListScreen` displays a thumbnail image for each `*ListItemModel`.
