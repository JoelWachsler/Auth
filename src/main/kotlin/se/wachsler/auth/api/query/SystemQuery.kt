package se.wachsler.auth.api.query

import com.expediagroup.graphql.execution.GraphQLContext
import com.expediagroup.graphql.spring.operations.Query
import org.springframework.stereotype.Component
import se.wachsler.auth.authenticatedCallback

@Suppress("unused")
@Component
class SystemQuery : Query {

    suspend fun systemInstallation(context: GraphQLContext) = authenticatedCallback(context) {
        "0.0.1"
    }
}