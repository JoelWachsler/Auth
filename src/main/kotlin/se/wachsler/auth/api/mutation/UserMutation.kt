package se.wachsler.auth.api.mutation

import com.expediagroup.graphql.spring.operations.Mutation
import org.springframework.stereotype.Component
import se.wachsler.auth.catchManagedErrors
import se.wachsler.auth.service.UserService
import java.util.logging.Logger

@Suppress("unused")
@Component
class UserMutation(
    private val userService: UserService,
) : Mutation {

    suspend fun register(username: String, password: String) = catchManagedErrors {
        try {
            val user = userService.createUser(username, password)
            Logger.getLogger(this.javaClass.name).info("Successfully registered: $username")
            user
        } catch (e: Exception) {
            Logger.getLogger(this.javaClass.name).info("Failed to create user: $username, $e")
            throw e
        }
    }

    suspend fun login(username: String, password: String) = catchManagedErrors {
        try {
            val token = userService.login(username, password)
            Logger.getLogger(this.javaClass.name).info("Successfully authenticated: $username")
            token
        } catch(e: Exception) {
            Logger.getLogger(this.javaClass.name).info("Failed to authenticate: $username, $e")
            throw e
        }
    }
}