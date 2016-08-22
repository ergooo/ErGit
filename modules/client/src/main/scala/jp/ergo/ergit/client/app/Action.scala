package jp.ergo.ergit.client.app

object Action {

  case object None extends Action

  case object Add extends Action

  case object Remove extends Action

}

sealed abstract class Action
