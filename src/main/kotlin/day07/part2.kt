package day07

import java.io.File

private const val TOTAL_SPACE = 70000000

private const val ENOUGH_SPACE = 30000000

fun main() {
    val fileSystem = createFileSystem(File("input/day07/input.txt").bufferedReader())

    val needDelete = ENOUGH_SPACE - (TOTAL_SPACE - fileSystem.root.size())

    println(findNeedToDelete(needDelete, fileSystem.root).size())
}

private fun findNeedToDelete(needDelete: Int, root: FileSystem.Directory) =
    iterateByEntries(root)
        .filterIsInstance<FileSystem.Directory>()
        .filter { it.size() >= needDelete }
        .sortedBy { it.size() }
        .first()
