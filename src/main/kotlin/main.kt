import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.runBlocking
import kotlin.system.measureTimeMillis

class TreeNode(private val left: TreeNode? = null, private val right: TreeNode? = null) {
    fun itemCheck(): Int =
        if (left == null) 1 else 1 + left.itemCheck() + right!!.itemCheck()

    companion object {
        val empty = TreeNode()

    }
}

fun bottomUpTree(depth: Int): TreeNode =
    if (depth > 0)
        TreeNode(bottomUpTree(depth - 1), bottomUpTree(depth - 1))
    else
        TreeNode.empty

fun main(args: Array<String>) = runBlocking {
    val MIN_DEPTH = 4

    measureTimeMillis {
        val n = if (args.isEmpty()) 0 else args[0].toInt()
        val maxDepth = if (n < MIN_DEPTH + 2) MIN_DEPTH + 2 else n
        val stretchDepth = maxDepth + 1

        println("stretch tree of depth $stretchDepth\t check: ${bottomUpTree(stretchDepth).itemCheck()}")

        val longLivedTree = bottomUpTree(maxDepth)

        (MIN_DEPTH..maxDepth step 2).map { depth ->
            async {
                val iterations = 1 shl maxDepth - depth + MIN_DEPTH
                val check = (1..iterations).map { bottomUpTree(depth).itemCheck() }.sum()
                "$iterations\t trees of depth $depth\t check: $check"
            }
        }.map { it.await() }.forEach(::println)

        println("long lived tree of depth $maxDepth\t check: ${longLivedTree.itemCheck()}")
    }.let { println("Elapsed $it") }
}
