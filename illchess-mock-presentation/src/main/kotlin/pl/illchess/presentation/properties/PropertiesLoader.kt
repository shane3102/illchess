package pl.illchess.presentation.properties

import java.io.IOException
import java.util.*


object PropertiesLoader {
    @Throws(IOException::class)
    fun loadProperties(): Properties {
        val configuration = Properties()
        val inputStream = PropertiesLoader::class.java
            .classLoader
            .getResourceAsStream("application.properties")
        configuration.load(inputStream)
        inputStream.close()
        return configuration
    }
}