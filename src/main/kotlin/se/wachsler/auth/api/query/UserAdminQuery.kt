package se.wachsler.auth.api.query

import com.expediagroup.graphql.execution.GraphQLContext
import com.expediagroup.graphql.spring.operations.Query
import kotlinx.coroutines.flow.toList
import org.springframework.stereotype.Component
import se.wachsler.auth.adminCallback
import se.wachsler.auth.service.UserService

@Suppress("unused")
@Component
class UserAdminQuery(
    private val userService: UserService
) : Query {

    suspend fun findAllUsers(context: GraphQLContext) = adminCallback(context) {
        userService.findAllUsers().toList()
    }
}