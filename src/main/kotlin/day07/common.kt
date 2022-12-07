package day07

import java.io.Reader

internal class FileSystem {
    val root: Directory = Directory("/", null)
    private var currentDirectory: Directory = root
    fun changeDirectory(name: String) {
        currentDirectory = when (name) {
            "/" -> root
            ".." -> currentDirectory.parent ?: throw IllegalArgumentException()
            else -> currentDirectory.children.findDirectory(name)
        }
    }

    fun createDirectory(name: String) {
        currentDirectory.children.add(Directory(name, currentDirectory))
    }

    fun createFile(name: String, size: Int) {
        currentDirectory.children.add(File(name, currentDirectory, size))
    }

    private fun Collection<FileSystemEntry>.findDirectory(name: String) = filterIsInstance<Directory>()
        .find { it.name == name } ?: throw IllegalArgumentException("$name doesn't exists or not directory")

    sealed interface FileSystemEntry {
        val name: String
        val parent: Directory?
        fun size(): Int
    }

    data class Directory(
        override val name: String,
        override val parent: Directory?
    ) : FileSystemEntry {
        val children: MutableCollection<FileSystemEntry> = mutableListOf()
        override fun size() = children.sumOf { it.size() }
    }

    class File(
        override val name: String,
        override val parent: Directory,
        private val size: Int
    ) : FileSystemEntry {
        override fun size() = size
    }
}

internal fun createFileSystem(commands: Reader): FileSystem {
    val fileSystem = FileSystem()
    var output = false
    commands.forEachLine {
        when {
            it.startsWith("$ cd ") -> {
                fileSystem.changeDirectory(it.substringAfter("$ cd "))
                output = false
            }

            it.startsWith("$ ls") -> output = true
            output && it.startsWith("dir ") -> fileSystem.createDirectory(it.substringAfter("dir "))
            output -> it.split(" ", limit = 2).let { (size, name) -> fileSystem.createFile(name, size.toInt()) }
        }
    }
    return fileSystem
}

internal fun iterateByEntries(directory: FileSystem.Directory): Sequence<FileSystem.FileSystemEntry> = sequence {
    directory.children.filterIsInstance<FileSystem.Directory>().forEach {
        yieldAll(iterateByEntries(it))
    }
    yieldAll(directory.children)
}
