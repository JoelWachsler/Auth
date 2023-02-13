package se.wachsler.auth.api.query

import com.expediagroup.graphql.execution.GraphQLContext
import com.expediagroup.graphql.spring.operations.Query
import org.springframework.stereotype.Component
import se.wachsler.auth.api.ParsedToken
import se.wachsler.auth.authenticatedCallback
import se.wachsler.auth.service.JwtService
import se.wachsler.auth.service.UserService
import se.wachsler.auth.service.toParsedToken

@Suppress("unused")
@Component
class UserQuery(
    private val jwtService: JwtService,
    private val userService: UserService,
) : Query {

    suspend fun currentUser(context: GraphQLContext) = authenticatedCallback(context) { authenticatedContext ->
        userService.getUser(authenticatedContext.userId)
    }

    suspend fun parseToken(token: String): ParsedToken {
        return jwtService
            .parseToken(token)
            .toParsedToken()
    }
}