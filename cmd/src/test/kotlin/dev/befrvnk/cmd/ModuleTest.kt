package dev.befrvnk.cmd

import dev.befrvnk.cmd.module.moduleAccessor
import dev.befrvnk.cmd.module.replaceVariables
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class ModuleTest : FunSpec({
    test("replaceVariables()") {
        val variables = mapOf("test_variable" to "test")
        "Blabla {test_variable}".replaceVariables(variables) shouldBe "Blabla test"
    }

    test("moduleAccessor()") {
        moduleAccessor(":feature-sorted:app-main") shouldBe "featureSorted.appMain"
    }
})