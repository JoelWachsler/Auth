package se.wachsler.auth

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import javax.annotation.processing.Generated

@Table("sensitive_user_data")
data class SensitiveUserData(
    @Id
    @Generated
    val id: Int? = null,
    @Column("wachsler_user")
    val userId: Int,
    val password: String,
)

enum class Status {
    /**
     * This is the default status when new accounts are created.
     * In order for them to be able to login an admin account has to activate them.
     */
    INACTIVE,

    /**
     * An active account should have this status. It means that they're allowed to login.
     */
    ACTIVE,
}

enum class Role {
    /**
     * Basic login functionality.
     */
    USER,

    MODERATOR,

    /**
     * Allows an account to modify other accounts and perform other admin related mutations.
     */
    ADMIN,
}
