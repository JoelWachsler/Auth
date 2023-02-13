package se.wachsler.auth.api

import se.wachsler.auth.Role

data class ParsedToken(val userId: Int, val role: Role)
