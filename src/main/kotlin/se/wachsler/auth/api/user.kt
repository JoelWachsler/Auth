package se.wachsler.auth.api

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import se.wachsler.auth.Role
import se.wachsler.auth.Status
import javax.annotation.processing.Generated

@Table("wachsler_user")
data class User(
    @Id
    @Generated
    val id: Int? = null,
    val username: String,
    val role: Role,
    val status: Status,
)

fun User.userId() = id!!
