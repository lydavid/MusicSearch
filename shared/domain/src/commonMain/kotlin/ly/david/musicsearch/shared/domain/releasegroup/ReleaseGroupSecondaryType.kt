package ly.david.musicsearch.shared.domain.releasegroup

enum class ReleaseGroupSecondaryType(val id: String) {
    AudioDrama("0eb547c2-8783-43e4-8f81-751c680e7b04"),
    Audiobook("499a387e-6195-333e-91c0-9592bfec535e"),
    Compilation("dd2a21e1-0c00-3729-a7a0-de60b84eb5d1"),
    DJMix("0d47f47a-3fe5-3d69-ac9d-d566c23968bf"),
    Demo("81598169-0d6c-3bce-b4be-866fa658eda3"),
    FieldRecording("dbb7cf7d-47c6-42d1-a621-ab84849bc7b7"),
    Interview("12af3f5e-ce2a-3941-8b93-d482884031e5"),
    Live("6fd474e2-6b58-3102-9d17-d6f7eb7da0a0"),
    MixtapeStreet("15c1b1f5-d893-3375-a1db-e180c5ae15ed"),
    Remix("0c60f497-ff81-3818-befd-abfc84a4858b"),
    Soundtrack("22a628ad-c082-3c4f-b1b6-d41665107b88"),
    Spokenword("66b8a13e-43ff-3ac0-ac6c-73659d3817c6"),
    ;

    companion object {
        private val ID_MAP = entries.associateBy { it.id }

        fun fromId(id: String?): ReleaseGroupSecondaryType? = ID_MAP[id]
    }
}
