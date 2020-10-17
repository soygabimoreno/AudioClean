package soy.gabimoreno.audioclean.data

import soy.gabimoreno.audioclean.domain.UserSession

class DefaultUserSession : UserSession {

    private var foo = false

    override fun foo(): Boolean = foo
}
