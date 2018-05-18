package dk.edutor.core.view

open class ChallengeIdentifier(val id: Int)

class ChallengeSummary(id: Int, val description: String) : ChallengeIdentifier(id)

open class StringChallengeDetail(id: Int,
  var description: String,
  var question: String
  )