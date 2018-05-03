package dk.edutor.core.view

open class ChallengeIdentifier(val id: Int)

class ChallengeSummary(id: Int) : ChallengeIdentifier(id)

open class ChallengeDetail(id: Int,
  var description: String
  )