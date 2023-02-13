package se.wachsler.auth

import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository
import se.wachsler.auth.api.User
import java.util.logging.Logger

@Repository
interface UserRepository : CoroutineCrudRepository<User, Int> {

    suspend fun findByUsername(username: String): User?
}

suspend fun UserRepository.findByIdEx(userId: Int): User {
    return findById(userId) ?: managedError("Invalid username or password")
}

suspend fun UserRepository.findByUsernameEx(username: String): User {
    return findByUsername(username.toLowerCase()) ?: managedError("Invalid username or password")
}

@Repository
interface SensitiveUserDataRepository : CoroutineCrudRepository<SensitiveUserData, Int> {

    suspend fun findByUserId(userId: Int): SensitiveUserData
}
