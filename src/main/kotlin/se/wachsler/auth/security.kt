package se.wachsler.auth

import com.expediagroup.graphql.execution.GraphQLContext
import graphql.execution.DataFetcherResult

/**
 * Helper method for handling user authentication. The callback will be called if the user is authenticated and admin.
 */
suspend fun <T> adminCallback(
    context: GraphQLContext,
    callback: suspend (AuthenticatedContext) -> T
): DataFetcherResult<T?> {
    return authenticatedCallback(context) { authenticatedContext ->
        if (authenticatedContext.role == Role.ADMIN) {
            callback(authenticatedContext)
        } else {
            managedError("User is not admin.")
        }
    }
}

/**
 * Helper method for handling user authentication. The callback will be called if the user is authenticated.
 */
suspend fun <T> authenticatedCallback(
    context: GraphQLContext,
    callback: suspend (AuthenticatedContext) -> T
): DataFetcherResult<T?> {
    return catchManagedErrors {
        if (context is AuthenticatedContext) {
            callback(context)
        } else {
            managedError("User is not authenticated.")
        }
    }
}

suspend fun <T> catchManagedErrors(call: suspend () -> T): DataFetcherResult<T?> {
    return try {
        DataFetcherResult.newResult<T>()
            .data(call())
            .build()
    } catch (e: ManagedError) {
        DataFetcherResult.newResult<T>()
            .error(e.graphQLError())
            .build()
    }
}
