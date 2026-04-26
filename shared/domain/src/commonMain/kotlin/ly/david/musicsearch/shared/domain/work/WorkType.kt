package ly.david.musicsearch.shared.domain.work

// TODO: search results doesn't include type_id, so need to use its name
//  as a closed class, we shouldn't ever have two types with the exact same name anyways
enum class WorkType(val id: String) {
    Aria("ae801f48-7a7f-3af6-91c7-456f82dae8a9"),
    AudioDrama("40ed00fb-cd1d-3de5-afcc-4d08720d63e7"), // Audio drama
    Ballet("6a90744c-1e07-3b88-b394-cd44cd68bd63"),
    BeijingOpera("e233d483-62f0-3379-81d7-b83911aee9b8"), // Beijing opera
    Cantata("0db2f555-15f9-393f-af4c-739db5711146"),
    Concerto("dad81031-607b-3039-bd5b-80c0f4575272"),
    Etude("01846fb2-42a6-332f-8bd0-2e6d80b4df7b"), // Étude
    IncidentalMusic("3cd7c402-444a-3d04-a154-4fa7d13e4ec6"), // Incidental music
    Madrigal("45c0b285-6de1-33c5-a45b-ac980f2b0129"),
    Mass("9fe2ca27-80b6-3a27-8c2b-895ec7f87917"),
    Motet("785ab71d-f748-32d1-8839-83d2b29e41f0"),
    Musical("9ca5e067-acf7-3cd6-baa4-92bf1975bf24"),
    Opera("d8059ee8-cb37-3351-9ca4-23978567339f"),
    Operetta("6d51f760-9d53-3856-a92f-e53d9d82ae5b"),
    Oratorio("308d1000-e97c-3629-b954-68505cf0aa30"),
    Overture("7b72b6c1-0d76-3262-9925-aedfab92ef01"),
    Partita("c5270f0f-9383-370c-997d-5d8fc51e5681"),
    Play("db708738-f2b6-3e4e-8f23-1661a3395947"),
    Poem("66b8026e-7ce8-36f8-a8d5-7c346c7b9a88"),
    Prose("78a8e727-edc2-35b9-8829-a46111ef6df9"),
    Quartet("30d4080a-d195-3f03-88e3-585aae505398"),
    Sonata("294ff9f2-3d64-3483-96fb-f362b5fce2a0"),
    Song("f061270a-2fd6-32f1-a641-f0f8676d14e6"),
    SongCycle("0f24ce27-96cc-3746-a16c-e6617d7a80f3"), // Song-cycle
    Soundtrack("66d7962b-6377-364f-a0b4-b200febc510e"),
    Suite("212bf9fb-ce4f-396e-87ca-3db671c085fb"),
    SymphonicPoem("01ef2ab4-d58d-3ef9-b309-eef98b58435f"), // Symphonic poem
    Symphony("174314aa-0aa4-30cf-96a6-50b281d8d208"),
    Zarzuela("d57f4af2-fa06-3218-99bf-001fc2362127"),
    ;

    companion object {
        private val ID_MAP = entries.associateBy { it.id }

        fun fromId(id: String?): WorkType? = ID_MAP[id]
    }
}
