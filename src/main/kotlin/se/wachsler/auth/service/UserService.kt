package se.wachsler.auth.service

import kotlinx.coroutines.flow.Flow
import org.springframework.security.crypto.bcrypt.BCrypt
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import se.wachsler.auth.*
import se.wachsler.auth.api.User
import se.wachsler.auth.api.userId

/**
 * Service class handling most, if not all, user functionality.
 * N.B this class should probably be split into multiple classes some day.
 */
@Service
@Transactional
class UserService(
    private val userRepository: UserRepository,
    private val sensitiveUserDataRepository: SensitiveUserDataRepository,
    private val jwtService: JwtService,
) {

    suspend fun login(username: String, password: String): String {
        val validatedUser = validateCredentials(userRepository.findByUsernameEx(username), password)
        return jwtService.generateToken(validatedUser.userId(), validatedUser.role)
    }

    private suspend fun validateCredentials(user: User, password: String): User {
        val sensitiveUserData = sensitiveUserDataRepository.findByUserId(user.userId())

        return if (!checkPassword(password, sensitiveUserData.password)) {
            managedError("Invalid username or password")
        } else if (user.status == Status.INACTIVE) {
            managedError("Account is not activated.")
        } else {
            user
        }
    }

    suspend fun setUserStatus(userId: Int, status: Status): User {
        return updateUser(userId) { user ->
            user.copy(status = status)
        }
    }

    suspend fun setUserRole(userId: Int, role: Role): User {
        return updateUser(userId) { user ->
            user.copy(role = role)
        }
    }

    suspend fun updatePassword(userId: Int, newPassword: String): SensitiveUserData {
        validatePassword(newPassword)
        val sensitiveUserData = sensitiveUserDataRepository.findByUserId(userId)
        return sensitiveUserDataRepository.save(sensitiveUserData.copy(password = encryptPassword(newPassword)))
    }

    suspend fun createUser(username: String, password: String): User {
        validateUsername(username)
        validatePassword(password)

        if (userRepository.findByUsername(username.toLowerCase()) != null) {
            managedError("A user with the provided username already exists!")
        } else {
            return createValidatedUser(username, password)
        }
    }

    private fun validateUsername(username: String) {
        if (username.length < 3) {
            managedError("Username must be longer than 2 characters")
        }
    }

    private fun validatePassword(password: String) {
        if (password.length < 3) {
            managedError("Password must be longer than 2 characters")
        }
    }

    private suspend fun createValidatedUser(
        username: String,
        password: String
    ): User {
        val user = userRepository.save(User(username = username, role = Role.USER, status = Status.INACTIVE))

        sensitiveUserDataRepository.save(
            SensitiveUserData(
                userId = user.userId(),
                password = encryptPassword(password)
            )
        )

        return user
    }

    suspend fun updateUser(userId: Int, update: suspend (user: User) -> User): User {
        val user = userRepository.findByIdEx(userId)
        val updatedUser = update(user)
        validateUsername(updatedUser.username)
        return userRepository.save(updatedUser)
    }

    suspend fun findAllUsers(): Flow<User> {
        return userRepository.findAll()
    }

    suspend fun getUser(userId: Int): User {
        return userRepository.findByIdEx(userId)
    }
}

private fun checkPassword(password: String, encryptedPassword: String): Boolean {
    return BCrypt.checkpw(password, encryptedPassword)
}

private fun encryptPassword(password: String): String {
    return BCrypt.hashpw(password, BCrypt.gensalt())
}
