import kotlincommon.listOfInts
import kotlincommon.permutations
import kotlincommon.printed
import kotlincommon.test.shouldEqual
import org.junit.Test
import java.util.*
import kotlin.random.Random

class MergeSortTests {
    @Test fun `trivial examples`() {
        emptyList<Int>().mergeSort() shouldEqual emptyList()
        listOf(1).mergeSort() shouldEqual listOf(1)
    }

    @Test fun `sort list of 2 elements`() {
        listOf(1, 2).mergeSort() shouldEqual listOf(1, 2)
        listOf(2, 1).mergeSort() shouldEqual listOf(1, 2)
    }

    @Test fun `sort list of 3 elements`() {
        listOf(1, 2, 3).mergeSort() shouldEqual listOf(1, 2, 3)
        listOf(1, 3, 2).mergeSort() shouldEqual listOf(1, 2, 3)
        listOf(2, 1, 3).mergeSort() shouldEqual listOf(1, 2, 3)
        listOf(2, 3, 1).mergeSort() shouldEqual listOf(1, 2, 3)
        listOf(3, 1, 2).mergeSort() shouldEqual listOf(1, 2, 3)
        listOf(3, 2, 1).mergeSort() shouldEqual listOf(1, 2, 3)
    }

    @Test fun `sort list of 4 elements`() {
        listOf(1, 2, 3, 4).permutations().forEach {
            it.mergeSort() shouldEqual listOf(1, 2, 3, 4)
        }
    }

    @Test fun `can sort random list`() {
        fun List<Int>.isSorted() =
            windowed(size = 2).all { it[0] <= it[1] }

        val random = Random(seed = Random.nextInt().printed("seed="))
        val list = random.listOfInts(
            sizeRange = 0..100_000,
            valuesRange = 0..50
        )
        list.mergeSort().isSorted() shouldEqual true
        list.mergeSort().size shouldEqual list.size
    }
}

fun <E: Comparable<E>> List<E>.mergeSort(): List<E> {
    if (size <= 1) return this

    val queue = LinkedList<List<E>>()
    forEach { element -> queue.add(listOf(element)) }
    while (queue.size > 1) {
        val left = queue.removeFirst()
        val right = queue.removeFirst()
        queue.add(merge(left, right))
    }
    return queue.single()
}

fun <E: Comparable<E>> List<E>.mergeSort_recursive(): List<E> {
    if (size <= 1) return this
    return merge(
        subList(0, size / 2).mergeSort_recursive(),
        subList(size / 2, size).mergeSort_recursive()
    )
}

fun <E: Comparable<E>> merge(left: List<E>, right: List<E>): List<E> {
    val result = ArrayList<E>()
    var i = 0
    var j = 0
    while (i < left.size && j < right.size) {
        val minElement = if (left[i] <= right[j]) left[i++] else right[j++]
        result.add(minElement)
    }
    result.addAll(left.subList(i, left.size))
    result.addAll(right.subList(j, right.size))
    return result
}

fun <E: Comparable<E>> merge_recursive(left: List<E>, right: List<E>): List<E> {
    return when {
        left.isEmpty()      -> right
        right.isEmpty()     -> left
        left[0] <= right[0] -> listOf(left[0]) + merge_recursive(left.drop(1), right)
        else                -> listOf(right[0]) + merge_recursive(left, right.drop(1))
    }
}

