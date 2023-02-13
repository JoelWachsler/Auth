package se.wachsler.auth

import com.natpryce.hamkrest.Matcher
import se.wachsler.auth.api.User

fun User.hasUsername(username: String) = this.username == username

fun User.hasStatus(status: Status) = this.status == status

fun isTrue() = Matcher(Boolean::equals, true)
