package ly.david.musicsearch.shared.domain.tag

data class TagChip(
    override val name: String,
    override val count: Int,
    override val voteType: VoteType = VoteType.Withdraw,
) : GenreOrTag {
    override val fullName: String
        get() = name
}
