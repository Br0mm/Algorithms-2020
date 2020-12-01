package lesson4

import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

class TrieTest : AbstractTrieTest() {

    override fun create(): MutableSet<String> =
        Trie()

    @Test
    @Tag("Example")
    fun generalTestJava() {
        doGeneralTest()
    }

    @Test
    @Tag("7")
    fun iteratorTestJava() {
        doBonusIteratorTest()
        doMoreBonusIteratorTest()
        doIteratorTest()
    }

    @Test
    @Tag("8")
    fun iteratorRemoveTestJava() {
        doBonusIteratorRemoveTest()
        doMoreBonusIteratorRemoveTest()
        doOneMoreBonusIteratorRemoveTest()
        doIteratorRemoveTest()
    }

}