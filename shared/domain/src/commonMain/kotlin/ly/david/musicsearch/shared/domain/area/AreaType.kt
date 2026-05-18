package ly.david.musicsearch.shared.domain.area

enum class AreaType(val id: String) {
    Country("06dd0ae4-8c74-30bb-b43d-95dcedf961de"),
    Subdivision("fd3d44c5-80a1-3842-9745-2c4972d35afa"),
    County("bcecec27-8bdb-3e00-8254-d948dda502fa"),
    Municipality("17246454-5ac4-36a1-b81a-4753eb2dab20"),
    City("6fd8f29a-3d0a-32fc-980d-ea697b69da78"),
    District("84039871-5e47-38ca-a66a-45e512c8290f"),
    Island("3f8e7b66-058b-369b-9834-ffa5fcba5641"),
    MilitaryBase("bbb27bc1-f21c-42a9-89ce-e9d986d60b46"),
    IndigenousTerritory("9bdda430-e4d4-464a-94a9-084a452ea8ea"),
    ;

    companion object {
        private val ID_MAP = entries.associateBy { it.id }

        fun fromId(id: String?): AreaType? = ID_MAP[id]
    }
}
