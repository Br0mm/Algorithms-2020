package lesson4

import java.util.*
import kotlin.math.abs
import ru.spbstu.kotlin.generate.util.nextString
import kotlin.test.*

abstract class AbstractTrieTest {

    abstract fun create(): MutableSet<String>

    private fun <R> implementationTest(function: () -> R) {
        try {
            function()
        } catch (e: Error) {
            if (e is NotImplementedError) {
                throw e
            }
        } catch (e: Exception) {
            // let it slide for now
        }
    }

    protected fun doGeneralTest() {
        val random = Random()
        for (iteration in 1..100) {
            val trie = create()
            assertEquals(0, trie.size)
            assertFalse("some" in trie)
            var wordCounter = 0
            val wordList = mutableSetOf<String>()
            val removeIndex = random.nextInt(15) + 1
            var toRemove = ""
            for (i in 1..15) {
                val string = random.nextString("abcdefgh", 1, 15)
                wordList += string
                if (i == removeIndex) {
                    toRemove = string
                }
                if (trie.add(string)) {
                    wordCounter++
                }
                assertTrue(
                    string in trie,
                    "An element wasn't added to trie when it should've been."
                )
                if (string.length != 1) {
                    val substring = string.substring(0, random.nextInt(string.length - 1))
                    if (substring !in wordList) {
                        assertTrue(
                            substring !in trie,
                            "An element is considered to be in trie when it should not be there."
                        )
                    }
                }
            }
            assertEquals(wordCounter, trie.size)
            trie.remove(toRemove)
            assertEquals(wordCounter - 1, trie.size)
            assertFalse(
                toRemove in trie,
                "A supposedly removed element is still considered to be in trie."
            )
            trie.clear()
            assertEquals(0, trie.size)
            assertFalse("some" in trie)
        }
    }

    protected fun doIteratorTest() {
        implementationTest { create().iterator().hasNext() }
        implementationTest { create().iterator().next() }
        val random = Random()
        for (iteration in 1..100) {
            val controlSet = mutableSetOf<String>()
            for (i in 1..15) {
                val string = random.nextString("abcdefgh", 1, 15)
                controlSet.add(string)
            }
            println("Control set: $controlSet")
            val trieSet = create()
            assertFalse(
                trieSet.iterator().hasNext(),
                "Iterator of an empty set should not have any next elements."
            )
            assertFailsWith<NoSuchElementException> {
                trieSet.iterator().next()
            }
            for (element in controlSet) {
                trieSet += element
            }
            val iterator1 = trieSet.iterator()
            val iterator2 = trieSet.iterator()
            println("Checking if calling hasNext() changes the state of the iterator...")
            var counter = 0
            while (iterator1.hasNext()) {
                counter++
                assertEquals(
                    iterator2.next(), iterator1.next(),
                    "Calling TrieIterator.hasNext() changes the state of the iterator."
                )
            }
            assertEquals(
                controlSet.size, counter
            )
            val trieIter = trieSet.iterator()
            println("Checking if the iterator traverses the entire set...")
            while (trieIter.hasNext()) {
                controlSet.remove(trieIter.next())
            }
            assertTrue(
                controlSet.isEmpty(),
                "TrieIterator doesn't traverse the entire set."
            )
            assertFailsWith<NoSuchElementException>("Something was supposedly returned after the elements ended") {
                trieIter.next()
            }
            println("All clear!")
        }
    }

    protected fun doBonusIteratorTest() {
        implementationTest { create().iterator().hasNext() }
        implementationTest { create().iterator().next() }

        val controlSet = mutableSetOf<String>()
        controlSet.add("egbeeed")
        controlSet.add("hd")
        controlSet.add("feeadadgahe")
        controlSet.add("fcgdgfgg")
        controlSet.add("fbchbafhadhfc")
        controlSet.add("accbfbhchged")
        controlSet.add("cg")
        controlSet.add("ddgdhcadche")
        controlSet.add("hgeeahcdafd")
        controlSet.add("bffheghhabeebf")
        controlSet.add("adbh")
        controlSet.add("gfbabeabhddghg")
        controlSet.add("fhhcebece")
        controlSet.add("acdcgbdae")
        controlSet.add("cgfbabddgdec")
        println("Control set: $controlSet")
        val trieSet = create()
        assertFalse(
            trieSet.iterator().hasNext(),
            "Iterator of an empty set should not have any next elements."
        )
        assertFailsWith<NoSuchElementException> {
            trieSet.iterator().next()
        }
        for (element in controlSet) {
            trieSet += element
        }
        val iterator1 = trieSet.iterator()
        val iterator2 = trieSet.iterator()
        println("Checking if calling hasNext() changes the state of the iterator...")
        var counter = 0
        while (iterator1.hasNext()) {
            counter++
            var test = iterator2.next()
            println(test)
            assertEquals(
                test, iterator1.next(),
                "Calling TrieIterator.hasNext() changes the state of the iterator."
            )
        }
        assertEquals(
            controlSet.size, counter
        )
        val trieIter = trieSet.iterator()
        println("Checking if the iterator traverses the entire set...")
        while (trieIter.hasNext()) {
            controlSet.remove(trieIter.next())
        }
        assertTrue(
            controlSet.isEmpty(),
            "TrieIterator doesn't traverse the entire set."
        )
        assertFailsWith<NoSuchElementException>("Something was supposedly returned after the elements ended") {
            trieIter.next()
        }
        println("All clear!")
    }

    protected fun doMoreBonusIteratorTest() {
        implementationTest { create().iterator().hasNext() }
        implementationTest { create().iterator().next() }

        val controlSet = mutableSetOf<String>()
        controlSet.add("ahchchgbf")
        controlSet.add("agdd")
        controlSet.add("ed")
        controlSet.add("gabedhgb")
        controlSet.add("dfc")
        controlSet.add("eddgcbdgehfdb")
        controlSet.add("gdfafbddh")
        controlSet.add("chfdhfehdacad")
        controlSet.add("ahfhgagdaeh")
        controlSet.add("bchgfghbb")
        controlSet.add("bdadebefb")
        controlSet.add("ehghgdecdc")
        controlSet.add("ggegedaeh")
        controlSet.add("e")
        controlSet.add("cbchaa")
        println("Control set: $controlSet")
        val trieSet = create()
        assertFalse(
            trieSet.iterator().hasNext(),
            "Iterator of an empty set should not have any next elements."
        )
        assertFailsWith<NoSuchElementException> {
            trieSet.iterator().next()
        }
        for (element in controlSet) {
            trieSet += element
        }
        val iterator1 = trieSet.iterator()
        val iterator2 = trieSet.iterator()
        println("Checking if calling hasNext() changes the state of the iterator...")
        var counter = 0
        while (iterator1.hasNext()) {
            counter++
            var test = iterator2.next()
            println(test)
            assertEquals(
                test, iterator1.next(),
                "Calling TrieIterator.hasNext() changes the state of the iterator."
            )
        }
        assertEquals(
            controlSet.size, counter
        )
        val trieIter = trieSet.iterator()
        println("Checking if the iterator traverses the entire set...")
        while (trieIter.hasNext()) {
            controlSet.remove(trieIter.next())
        }
        assertTrue(
            controlSet.isEmpty(),
            "TrieIterator doesn't traverse the entire set."
        )
        assertFailsWith<NoSuchElementException>("Something was supposedly returned after the elements ended") {
            trieIter.next()
        }
        println("All clear!")
    }

    protected fun doIteratorRemoveTest() {
        implementationTest { create().iterator().remove() }
        assertFailsWith<IllegalStateException> {
            create().iterator().remove()
        }
        val random = Random()
        for (iteration in 1..100) {
            val controlSet = mutableSetOf<String>()
            val removeIndex = random.nextInt(15) + 1
            var toRemove = ""
            for (i in 1..15) {
                val string = random.nextString("abcdefgh", 1, 15)
                controlSet.add(string)
                if (i == removeIndex) {
                    toRemove = string
                }
            }
            println("Initial set: $controlSet")
            val trieSet = create()
            for (element in controlSet) {
                trieSet += element
            }
            controlSet.remove(toRemove)
            println("Control set: $controlSet")
            println("Removing element \"$toRemove\" from trie set through the iterator...")
            val iterator = trieSet.iterator()
            assertFailsWith<IllegalStateException>("Something was supposedly deleted before the iteration started") {
                iterator.remove()
            }
            var counter = trieSet.size
            while (iterator.hasNext()) {
                val element = iterator.next()
                counter--
                if (element == toRemove) {
                    iterator.remove()
                    assertFailsWith<IllegalStateException>("Trie.remove() was successfully called twice in a row.") {
                        iterator.remove()
                    }
                }
            }
            assertEquals(
                0, counter,
                "TrieIterator.remove() changed iterator position: ${abs(counter)} elements were ${if (counter > 0) "skipped" else "revisited"}."
            )
            assertEquals(
                controlSet.size, trieSet.size,
                "The size of the set is incorrect: was ${trieSet.size}, should've been ${controlSet.size}."
            )
            for (element in controlSet) {
                assertTrue(
                    trieSet.contains(element),
                    "Trie set doesn't have the element $element from the control set."
                )
            }
            for (element in trieSet) {
                assertTrue(
                    controlSet.contains(element),
                    "Trie set has the element $element that is not in control set."
                )
            }
            val iterator1 = trieSet.iterator()
            while (iterator1.hasNext()) {
                iterator1.next()
                iterator1.remove()
            }
            assertEquals(
                0, trieSet.size,
                "binarySet isn't empty"
            )
            println("All clear!")
        }
    }

    protected fun doBonusIteratorRemoveTest() {
        implementationTest { create().iterator().remove() }
        assertFailsWith<IllegalStateException> {
            create().iterator().remove()
        }
        val controlSet = mutableSetOf<String>()
        val toRemove = "abaafegafcaecg"
        controlSet.add("hhdbadegdfa")
        controlSet.add("dfegecghadg")
        controlSet.add("cbacagcha")
        controlSet.add("c")
        controlSet.add("abaafegafcaecg")
        controlSet.add("cgeeeggeb")
        controlSet.add("bcdgggbbhh")
        controlSet.add("begbbcahd")
        controlSet.add("ggdeecbacee")
        controlSet.add("efege")
        controlSet.add("geecdccaf")
        controlSet.add("bae")
        controlSet.add("cdaggaa")
        controlSet.add("becch")
        println("Initial set: $controlSet")
        val trieSet = create()
        for (element in controlSet) {
            trieSet += element
        }
        controlSet.remove(toRemove)
        println("Control set: $controlSet")
        println("Removing element \"$toRemove\" from trie set through the iterator...")
        val iterator = trieSet.iterator()
        assertFailsWith<IllegalStateException>("Something was supposedly deleted before the iteration started") {
            iterator.remove()
        }
        var counter = trieSet.size
        while (iterator.hasNext()) {
            val element = iterator.next()
            counter--
            if (element == toRemove) {
                iterator.remove()
                assertFailsWith<IllegalStateException>("Trie.remove() was successfully called twice in a row.") {
                    iterator.remove()
                }
            }
        }
        assertEquals(
            0, counter,
            "TrieIterator.remove() changed iterator position: ${abs(counter)} elements were ${if (counter > 0) "skipped" else "revisited"}."
        )
        assertEquals(
            controlSet.size, trieSet.size,
            "The size of the set is incorrect: was ${trieSet.size}, should've been ${controlSet.size}."
        )
        for (element in controlSet) {
            assertTrue(
                trieSet.contains(element),
                "Trie set doesn't have the element $element from the control set."
            )
        }
        for (element in trieSet) {
            assertTrue(
                controlSet.contains(element),
                "Trie set has the element $element that is not in control set."
            )
        }
        val iterator1 = trieSet.iterator()
        while (iterator1.hasNext()) {
            var test = iterator1.next()
            println(test)
            iterator1.remove()
        }
        assertEquals(
            0, trieSet.size,
            "binarySet isn't empty"
        )
        println("All clear!")
    }

    protected fun doMoreBonusIteratorRemoveTest() {
        implementationTest { create().iterator().remove() }
        assertFailsWith<IllegalStateException> {
            create().iterator().remove()
        }
        val controlSet = mutableSetOf<String>()
        val toRemove = "aafbghaedhca"
        controlSet.add("aafbghaedhca")
        controlSet.add("ef")
        controlSet.add("gf")
        controlSet.add("chccf")
        controlSet.add("heahbcbggafgc")
        controlSet.add("ba")
        controlSet.add("hebggcedecbh")
        controlSet.add("bfc")
        controlSet.add("gaeceed")
        controlSet.add("cfhefhgb")
        controlSet.add("ffccfea")
        controlSet.add("ggdc")
        controlSet.add("hcbabfddhfdd")
        controlSet.add("achac")
        controlSet.add("ad")
        println("Initial set: $controlSet")
        val trieSet = create()
        for (element in controlSet) {
            trieSet += element
        }
        controlSet.remove(toRemove)
        println("Control set: $controlSet")
        println("Removing element \"$toRemove\" from trie set through the iterator...")
        val iterator = trieSet.iterator()
        assertFailsWith<IllegalStateException>("Something was supposedly deleted before the iteration started") {
            iterator.remove()
        }
        var counter = trieSet.size
        while (iterator.hasNext()) {
            val element = iterator.next()
            counter--
            if (element == toRemove) {
                iterator.remove()
                assertFailsWith<IllegalStateException>("Trie.remove() was successfully called twice in a row.") {
                    iterator.remove()
                }
            }
        }
        assertEquals(
            0, counter,
            "TrieIterator.remove() changed iterator position: ${abs(counter)} elements were ${if (counter > 0) "skipped" else "revisited"}."
        )
        assertEquals(
            controlSet.size, trieSet.size,
            "The size of the set is incorrect: was ${trieSet.size}, should've been ${controlSet.size}."
        )
        for (element in controlSet) {
            assertTrue(
                trieSet.contains(element),
                "Trie set doesn't have the element $element from the control set."
            )
        }
        for (element in trieSet) {
            assertTrue(
                controlSet.contains(element),
                "Trie set has the element $element that is not in control set."
            )
        }
        val iterator1 = trieSet.iterator()
        while (iterator1.hasNext()) {
            var test = iterator1.next()
            println(test)
            iterator1.remove()
        }
        assertEquals(
            0, trieSet.size,
            "binarySet isn't empty"
        )
        println("All clear!")
    }

    protected fun doOneMoreBonusIteratorRemoveTest() {
        implementationTest { create().iterator().remove() }
        assertFailsWith<IllegalStateException> {
            create().iterator().remove()
        }
        val controlSet = mutableSetOf<String>()
        val toRemove = "eda"
        controlSet.add("ggfebhaeehffgf")
        controlSet.add("fhdaddhba")
        controlSet.add("chagfhec")
        controlSet.add("hfad")
        controlSet.add("cfdfdh")
        controlSet.add("gad")
        controlSet.add("g")
        controlSet.add("ah")
        controlSet.add("behchdaa")
        controlSet.add("eda")
        controlSet.add("gdgffggagegaf")
        controlSet.add("eebhdeah")
        controlSet.add("adcfga")
        controlSet.add("ghaeegabehb")
        println("Initial set: $controlSet")
        val trieSet = create()
        for (element in controlSet) {
            trieSet += element
        }
        controlSet.remove(toRemove)
        println("Control set: $controlSet")
        println("Removing element \"$toRemove\" from trie set through the iterator...")
        val iterator = trieSet.iterator()
        assertFailsWith<IllegalStateException>("Something was supposedly deleted before the iteration started") {
            iterator.remove()
        }
        var counter = trieSet.size
        while (iterator.hasNext()) {
            val element = iterator.next()
            counter--
            if (element == toRemove) {
                iterator.remove()
                assertFailsWith<IllegalStateException>("Trie.remove() was successfully called twice in a row.") {
                    iterator.remove()
                }
            }
        }
        assertEquals(
            0, counter,
            "TrieIterator.remove() changed iterator position: ${abs(counter)} elements were ${if (counter > 0) "skipped" else "revisited"}."
        )
        assertEquals(
            controlSet.size, trieSet.size,
            "The size of the set is incorrect: was ${trieSet.size}, should've been ${controlSet.size}."
        )
        for (element in controlSet) {
            assertTrue(
                trieSet.contains(element),
                "Trie set doesn't have the element $element from the control set."
            )
        }
        for (element in trieSet) {
            assertTrue(
                controlSet.contains(element),
                "Trie set has the element $element that is not in control set."
            )
        }
        val iterator1 = trieSet.iterator()
        while (iterator1.hasNext()) {
            var test = iterator1.next()
            println(test)
            iterator1.remove()
        }
        assertEquals(
            0, trieSet.size,
            "binarySet isn't empty"
        )
        println("All clear!")
    }
}