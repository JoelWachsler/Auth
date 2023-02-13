package se.wachsler.auth

import com.expediagroup.graphql.execution.EmptyGraphQLContext
import com.expediagroup.graphql.execution.GraphQLContext
import com.expediagroup.graphql.spring.execution.GraphQLContextFactory
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.http.server.reactive.ServerHttpResponse
import org.springframework.stereotype.Component

class AuthenticatedContext(val userId: Int, val role: Role) : GraphQLContext

@Component
class ContextFactory : GraphQLContextFactory<GraphQLContext> {

    override suspend fun generateContext(request: ServerHttpRequest, response: ServerHttpResponse): GraphQLContext {
        val headers = request.headers
        val userId = headers.getFirst("user-id")
        val role = headers.getFirst("role")

        return if (userId != null && role != null) {
            AuthenticatedContext(userId = userId.toInt(), role = Role.valueOf(role))
        } else {
            EmptyGraphQLContext()
        }
    }
}