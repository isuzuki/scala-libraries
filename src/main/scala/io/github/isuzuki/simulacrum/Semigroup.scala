package io.github.isuzuki.simulacrum

import simulacrum._

import scala.language.implicitConversions

object Macro {
  @typeclass trait Semigroup[A] {
    @op("|+|") def append(x: A, y: A): A
  }
}

object NonMacro {
  trait Semigroup[A] {
    def append(x: A, y: A): A
  }

  object Semigroup {
    def apply[A](implicit instance: Semigroup[A]): Semigroup[A] = instance

    trait Ops[A] {
      def typeClassInstance: Semigroup[A]
      def self: A
      def |+|(y: A): A = typeClassInstance.append(self, y)
    }

    trait ToSemigroupOps {
      implicit def toSemigroupOps[A](target: A)(implicit tc: Semigroup[A]): Ops[A] = new Ops[A] {
        val self = target
        val typeClassInstance = tc
      }
    }

    object nonInheritedOps extends ToSemigroupOps

    trait AllOps[A] extends Ops[A] {
      def typeClassInstance: Semigroup[A]
    }

    object ops {
      implicit def toAllSemigroupOps[A](target: A)(implicit tc: Semigroup[A]): AllOps[A] = new AllOps[A] {
        val self = target
        val typeClassInstance = tc
      }
    }
  }
}

object SemigroupApp extends App {
  implicit val semigroupInt: Macro.Semigroup[Int] = new Macro.Semigroup[Int] {
    override def append(x: Int, y: Int): Int = x + y
  }

  import Macro.Semigroup.ops._
  assert((1 |+| 2) == 3)
}