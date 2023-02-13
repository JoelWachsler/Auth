package se.wachsler.auth

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.transaction.reactive.TransactionalOperator
import org.springframework.transaction.reactive.executeAndAwait
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono

@Component
class Transaction(
    @Autowired
    val to: TransactionalOperator
) {
    fun <T> withRollback(publisher: Flux<T>): Flux<T> {
        return to.execute { tx ->
            tx.setRollbackOnly()
            publisher
        }
    }

    fun <T> withRollback(publisher: Mono<T>): Mono<T> {
        return to.execute { tx ->
            tx.setRollbackOnly()
            publisher
        }.toMono()
    }

    suspend fun <T> withRollback(publisher: suspend () -> T) {
        to.executeAndAwait { tx ->
            tx.setRollbackOnly()
            publisher()
        }
    }
}