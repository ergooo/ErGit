package jp.ergo.ergit.client.infrastructure.utils

import java.io.Closeable


object Using {
  def using[T, E <: Closeable](s: E)(f: E => T): T = {
    try {
      f(s)
    } finally {
      s.close()
    }
  }

}
