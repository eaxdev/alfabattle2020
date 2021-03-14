package io.github.eaxdev

import org.springframework.util.ResourceUtils
import java.io.IOException
import java.nio.charset.StandardCharsets
import java.nio.file.Files

@Throws(IOException::class)
fun readFileAsString(filePath: String): String {
    val file = ResourceUtils.getFile("classpath:$filePath")
    val fileBytes = Files.readAllBytes(file.toPath())
    return String(fileBytes, StandardCharsets.UTF_8)
}