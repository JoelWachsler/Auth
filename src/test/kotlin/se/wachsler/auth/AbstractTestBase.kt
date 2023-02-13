package se.wachsler.auth

import kotlinx.coroutines.runBlocking
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(TestConfig::class)
abstract class AbstractTestBase {

    @Autowired
    internal lateinit var tran: Transaction
}

fun AbstractTestBase.withRollback(body: suspend () -> Unit) {
    runBlocking {
        tran.withRollback {
            body()
        }
    }
}
