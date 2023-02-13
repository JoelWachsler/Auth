package se.wachsler.auth.service

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.DecodedJWT
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import se.wachsler.auth.Role
import se.wachsler.auth.api.ParsedToken

const val ID_CLAIM = "id"
const val ROLE_CLAIM = "role"

@Service
class JwtService {

    @Value("\${jwt.secret}")
    private lateinit var jwtSecret: String

    fun parseToken(token: String): DecodedJWT {
        val verifier = JWT.require(jwtAlgorithm()).build()
        return verifier.verify(token)
    }

    fun generateToken(userId: Int, role: Role): String = JWT.create()
        .withClaim(ID_CLAIM, userId)
        .withClaim(ROLE_CLAIM, role.name)
        .sign(jwtAlgorithm())

    private fun jwtAlgorithm() = Algorithm.HMAC256(jwtSecret)
}

fun DecodedJWT.toParsedToken(): ParsedToken {
    return ParsedToken(
        userId = getClaim(ID_CLAIM).asInt(),
        role = Role.valueOf(getClaim(ROLE_CLAIM).asString())
    )
}