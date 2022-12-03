package io.olkkani

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
open class CleanCodeLevelTestApplication

fun main(args: Array<String>) {
	runApplication<CleanCodeLevelTestApplication>(*args)
}
