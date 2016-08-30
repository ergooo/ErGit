package jp.ergo.ergit.client.app

sealed abstract class Command

object Command {

  case object None extends Command

  case object Init extends Command

  case object Repository extends Command

  case object Status extends Command

  case object Checkout extends Command

  case object Branch extends Command

}