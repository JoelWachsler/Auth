package se.wachsler.auth.service

import com.natpryce.hamkrest.Matcher
import com.natpryce.hamkrest.and
import com.natpryce.hamkrest.assertion.assertThat
import kotlinx.coroutines.flow.toList
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import se.wachsler.auth.*
import se.wachsler.auth.api.User
import se.wachsler.auth.api.userId

class UserServiceTest(
    @Autowired
    val userRepository: UserRepository,
    @Autowired
    val userService: UserService,
) : AbstractTestBase() {

    val testUser = TestUser("test", "123")

    @Test
    fun `non existent user should not be able to login`() = withRollback {
        assertThrows<ManagedError> {
            loginUser(TestUser("invalid", "invalid"))
        }
    }

    @Test
    fun `de-activated user should not be able to login`() = withRollback {
        createUser(testUser)
        assertThrows<ManagedError> {
            loginUser(testUser)
        }
    }

    @Test
    fun `user should be able to register`() = withRollback {
        createUser(testUser)
        val user = userRepository.findByUsernameEx(testUser.username)
        // the user should exist, but is not active
        assertThat(
            user,
            Matcher(User::hasUsername, user.username) and Matcher(User::hasStatus, Status.INACTIVE)
        )
    }

    @Test
    fun `user should not be able to register with an occupied username`() = withRollback {
        createUser(testUser)
        // this user has already been registered
        assertThrows<ManagedError> {
            createUser(testUser)
        }
    }

    @Test
    fun `user should be able to login after activation`() = withRollback {
        val user = createUser(testUser)
        activateUser(user.userId())
        assertThat(loginUser(testUser), Matcher(String::isNotBlank))
    }

    @Test
    fun `empty username should throw exception`() = withRollback {
        assertThrows<ManagedError> {
            createUser(TestUser("", "123"))
        }
    }

    @Test
    fun `empty password should throw exception`() = withRollback {
        assertThrows<ManagedError> {
            createUser(TestUser("abc", ""))
        }
    }

    @Test
    fun `updating to empty password should throw exception`() = withRollback {
        val user = createUser(TestUser("abc", "abc"))

        assertThrows<ManagedError> {
            userService.updatePassword(user.id!!, "")
        }
    }

    @Test
    fun `should find all users`() = withRollback {
        createUser(TestUser("abc", "123"))
        createUser(TestUser("abc1", "124"))
        createUser(TestUser("abc2", "125"))

        val allUsers = userService.findAllUsers().toList()
            .map { it.username }
        assertThat(allUsers, List<String>::containsAll, listOf("abc", "abc1", "abc2"))
    }

    suspend fun createUser(testUser: TestUser): User {
        return userService.createUser(testUser.username, testUser.password)
    }

    suspend fun activateUser(userId: Int) {
        userService.setUserStatus(userId, Status.ACTIVE)
    }

    suspend fun loginUser(testUser: TestUser): String {
        return userService.login(testUser.username, testUser.password)
    }

    data class TestUser(val username: String, val password: String)
}