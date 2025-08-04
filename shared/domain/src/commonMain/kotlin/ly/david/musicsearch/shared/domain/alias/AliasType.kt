package ly.david.musicsearch.shared.domain.alias

enum class AliasType(val id: String) {
    AREA_NAME("0b5b3497-d5d9-34e7-a61b-9a6c18aa7b29"),
    AREA_SEARCH_HINT("7090dd35-e32e-3422-8a48-224821c2468b"),
    AREA_FORMAL_NAME("b280c712-f676-342e-a8f2-e5c5fe0159b4"),

    ARTIST_NAME("894afba6-2816-3c24-8072-eadb66bd04bc"),
    ARTIST_LEGAL_NAME("d4dcd0c0-b341-3612-a332-c0ce797b25cf"),
    ARTIST_SEARCH_HINT("1937e404-b981-3cb7-8151-4c86ebfc8d8e"),

    EVENT_NAME("412aac48-424b-3052-a314-1f926e8018c8"),
    EVENT_SEARCH_HINT("9b7e72d0-ef3f-3c75-908c-f94c48eb6484"),

    INSTRUMENT_NAME("2322fc94-fbf3-3c09-b23c-aa5ec8d14fcd"),
    INSTRUMENT_SEARCH_HINT("7d5ef40f-4856-3000-8667-aa13b9db547d"),
    INSTRUMENT_BRAND_NAME("9a75677e-768a-3307-808c-a08c3d8ecd68"),

    LABEL_NAME("3a1a0c48-d885-3b89-87b2-9e8a483c5675"),
    LABEL_SEARCH_HINT("829662f2-a781-3ec8-8b46-fbcea6196f81"),

    PLACE_NAME("fb68f9a2-622c-319b-83b0-bbff4127cdc5"),
    PLACE_SEARCH_HINT("0a438b9c-1850-32de-b4bb-7f58f5048ea3"),

    RECORDING_NAME("5d564c8f-97de-3572-94bb-7f40ad661499"),
    RECORDING_SEARCH_HINT("ba0dbaab-25c6-30a8-9da4-8568020ecdf3"),

    RELEASE_NAME("df187855-059b-3514-9d5e-d240de0b4228"),
    RELEASE_SEARCH_HINT("02939c89-0e48-3357-bf41-bf8e4162c874"),

    RELEASE_GROUP_NAME("156e24ca-8746-3cfc-99ae-0a867c765c67"),
    RELEASE_GROUP_SEARCH_HINT("abc2db8a-7386-354d-82f4-252c0213cbe4"),

    SERIES_NAME("0c615dbc-c7d6-39b3-b8da-bef465ce3046"),
    SERIES_SEARCH_HINT("8950366b-5ea3-32f2-bf74-ee482474c18b"),

    WORK_NAME("a18cab3f-0ae2-3978-8f75-dd9c09702b25"),
    WORK_SEARCH_HINT("02238bc1-dfd8-39a8-bbf8-c697747291ec"),
    ;

    companion object {
        private val ID_MAP = entries.associateBy { it.id }

        fun fromId(id: String?): AliasType? = ID_MAP[id]
    }
}
