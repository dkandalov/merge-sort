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

    @Test fun `sort random list`() {
        fun List<Int>.isSorted() =
            windowed(size = 2).all { it[0] <= it[1] }

        val seed = Random.nextInt().printed("seed=")
        val list = Random(seed = seed).listOfInts(
            size = 100_000,
            valuesRange = 0..100
        )
        list.mergeSort().isSorted() shouldEqual true
    }
}

fun <E: Comparable<E>> List<E>.mergeSort(): List<E> {
    if (size <= 1) return this

    val queue = LinkedList<List<E>>()
    queue.addAll(map { listOf(it) })
    while (queue.size > 1) {
        val list1 = queue.removeFirst()
        val list2 = queue.removeFirst()
        queue.add(merge(list1, list2))
    }
    return queue.first
}

fun <E: Comparable<E>> List<E>.mergeSort_recursive(): List<E> {
    if (size <= 1) return this
    return merge(
        subList(0, size / 2).mergeSort(),
        subList(size / 2, size).mergeSort()
    )
}

fun <E: Comparable<E>> merge(left: List<E>, right: List<E>): List<E> {
    val result = ArrayList<E>()
    var i = 0
    var j = 0
    while (i < left.size && j < right.size) {
        val minElement = if (left[i] < right[j]) left[i++] else right[j++]
        result.add(minElement)
    }
    result.addAll(left.subList(i, left.size))
    result.addAll(right.subList(j, right.size))
    return result
}
