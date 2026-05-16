package ly.david.musicsearch.shared.domain.tag

enum class VoteType {
    Upvote,
    Downvote,

    /**
     * Also represents no votes.
     */
    Withdraw,
    ;

    override fun toString(): String {
        return name.lowercase()
    }
}
