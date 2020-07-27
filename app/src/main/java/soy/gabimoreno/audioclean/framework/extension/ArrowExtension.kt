package soy.gabimoreno.audioclean.framework.extension

import arrow.core.Either
import arrow.core.left
import arrow.core.right

inline fun <A> Either.Companion.unsafeCatch(f: () -> A): Either<Throwable, A> = try {
    f().right()
} catch (ex: Exception) {
    ex.left()
}
