package day07

import java.io.File

private fun findThoseDirectory(directory: FileSystem.Directory): Sequence<FileSystem.Directory> =
    iterateByEntries(directory).filterIsInstance<FileSystem.Directory>().filter { it.size() <= 100_000 }


fun main() {
    val fileSystem = createFileSystem(File("input/day07/input.txt").bufferedReader())

    println(findThoseDirectory(fileSystem.root).sumOf { it.size() })
}
