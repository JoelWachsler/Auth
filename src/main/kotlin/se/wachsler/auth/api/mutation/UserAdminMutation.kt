package se.wachsler.auth.api.mutation

import com.expediagroup.graphql.execution.GraphQLContext
import com.expediagroup.graphql.spring.operations.Mutation
import org.springframework.stereotype.Service
import se.wachsler.auth.Role
import se.wachsler.auth.Status
import se.wachsler.auth.adminCallback
import se.wachsler.auth.service.UserService

@Suppress("unused")
@Service
class UserAdminMutation(
    private val userService: UserService,
) : Mutation {

    suspend fun changePassword(context: GraphQLContext, userId: Int, password: String) =
        adminCallback(context) {
            userService.updatePassword(userId, password)
            true
        }

    suspend fun changeStatus(context: GraphQLContext, userId: Int, status: Status) = adminCallback(context) {
        userService.setUserStatus(userId, status)
    }

    suspend fun changeRole(context: GraphQLContext, userId: Int, role: Role) = adminCallback(context) {
        userService.setUserRole(userId, role)
    }
}