package jp.ergo.ergit.app

import java.io.File

class ErGitClient {

}

object ErGitClient {
  def main(args: Array[String]): Unit = {


    val parser = new scopt.OptionParser[Config]("Checkout-kun") {
      head("Checkout-kun", "0.1")

      opt[File]('r', "repositories").action( (x, c) =>
        c.copy(repositories = x) ).text("repositories is a File property")

      opt[String]('b', "branch").action( (x,c) =>
        c.copy(branch = x) ).text("branch is a target branch")

      opt[Unit]('s', "status").action( (x,c) =>
        c.copy(status = true) ).text("status")

      opt[Unit]('c', "checkout").action( (x,c) =>
        c.copy(checkout = true) ).text("checkout")

    }

    // parser.parse returns Option[C]
    parser.parse(args, Config()) match {
      case Some(config) =>
      // do stuff

      case None =>
      // arguments are bad, error message will have been displayed
    }

  }




}