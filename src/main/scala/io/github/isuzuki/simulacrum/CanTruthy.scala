package io.github.isuzuki.simulacrum

import simulacrum._

import scala.language.implicitConversions

/**
 * reference: http://eed3si9n.com/herding-cats/ja/making-our-own-typeclass-with-simulacrum.html
 */

@typeclass trait CanTruthy[A] {
  def truthy(a: A): Boolean
}

object CanTruthy {
  def fromTruthy[A](f: A => Boolean): CanTruthy[A] = new CanTruthy[A] {
    def truthy(a: A): Boolean = f(a)
  }
}

object CanTruthyApp extends App {
  implicit val intCanTruthy: CanTruthy[Int] = CanTruthy.fromTruthy({
    case 0 => false
    case _ => true
  })

  import CanTruthy.ops._
  assert(10.truthy)
}
