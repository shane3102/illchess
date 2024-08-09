package pl.messaging.quarkus.test

import io.quarkus.test.QuarkusDevModeTest
import org.jboss.shrinkwrap.api.ShrinkWrap
import org.jboss.shrinkwrap.api.spec.JavaArchive
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension

class InboxOutboxQuarkusDevModeTest {

    @Test
    fun writeYourOwnDevModeTest() {
        // Write your dev mode tests here - see the testing extension guide https://quarkus.io/guides/writing-extensions#testing-hot-reload for more information
        Assertions.assertTrue(true, "Add dev mode assertions to " + javaClass.name)
    }

    companion object {
        // Start hot reload (DevMode) test with your extension loaded
        @JvmStatic
        @RegisterExtension
        val devModeTest: QuarkusDevModeTest = QuarkusDevModeTest()
            .setArchiveProducer { ShrinkWrap.create(JavaArchive::class.java) }
    }
}