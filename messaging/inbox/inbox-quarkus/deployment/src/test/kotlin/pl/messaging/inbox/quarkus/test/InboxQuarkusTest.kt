package pl.messaging.inbox.quarkus.test

import io.quarkus.test.QuarkusUnitTest
import org.jboss.shrinkwrap.api.ShrinkWrap
import org.jboss.shrinkwrap.api.spec.JavaArchive
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension

class InboxQuarkusTest {

    @Test
    fun writeYourOwnUnitTest() {
        // Write your unit tests here - see the testing extension guide https://quarkus.io/guides/writing-extensions#testing-extensions for more information
        Assertions.assertTrue(true, "Add some assertions to " + javaClass.name)
    }

    companion object {
        // Start unit test with your extension loaded
        @JvmStatic
        @RegisterExtension
        val unitTest: QuarkusUnitTest = QuarkusUnitTest()
            .setArchiveProducer { ShrinkWrap.create(JavaArchive::class.java) }
    }
}