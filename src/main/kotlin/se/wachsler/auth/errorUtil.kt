package se.wachsler.auth

import graphql.ErrorClassification
import graphql.ErrorType
import graphql.GraphQLError
import graphql.language.SourceLocation

class ManagedGraphQLError(private val message: String) : GraphQLError {

    override fun getMessage(): String = message

    override fun getLocations(): MutableList<SourceLocation> = mutableListOf()

    override fun getErrorType(): ErrorClassification = ErrorType.ValidationError
}

class ManagedError(private val error: String) : RuntimeException() {

    fun graphQLError(): GraphQLError {
        return ManagedGraphQLError(error)
    }

    override fun toString(): String {
        return error
    }
}

fun managedError(error: String): Nothing {
    throw ManagedError(error)
}
