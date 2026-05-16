package ly.david.musicsearch.shared.domain.tag

import ly.david.musicsearch.shared.domain.parcelize.CommonParcelable
import ly.david.musicsearch.shared.domain.parcelize.Parcelize

@Parcelize
sealed interface GenreOrTag : CommonParcelable {
    val count: Int
    val name: String
    val fullName: String
    val voteType: VoteType
}

@Suppress("MagicNumber")
fun GenreOrTag.getNewVoteCount(newVoteType: VoteType): Int {
    val currentVoteType = this.voteType
    return this.count +
        when (newVoteType) {
            VoteType.Upvote -> {
                when (currentVoteType) {
                    VoteType.Downvote -> 2
                    VoteType.Withdraw -> 1
                    VoteType.Upvote -> 0
                }
            }

            VoteType.Downvote -> {
                when (currentVoteType) {
                    VoteType.Upvote -> -2
                    VoteType.Withdraw -> -1
                    VoteType.Downvote -> 0
                }
            }

            VoteType.Withdraw -> {
                when (currentVoteType) {
                    VoteType.Upvote -> -1
                    VoteType.Downvote -> 1
                    VoteType.Withdraw -> 0
                }
            }
        }
}
