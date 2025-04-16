package io.github.xyzboom.konst.ksp

import com.google.devtools.ksp.processing.*
import com.google.devtools.ksp.symbol.*
import java.io.Closeable
import java.io.OutputStreamWriter
import java.io.Writer

class KonstSymbolProcessor(
    private val codeGen: CodeGenerator,
    private val logger: KSPLogger,
): SymbolProcessor {
    @JvmInline
    value class StringAndNewLine(val str: String)

    @Suppress("NOTHING_TO_INLINE")
    private inner class WriterWrapper(val writer: Writer): Closeable by writer {
        private val usedQName = mutableSetOf<String>()


        inline operator fun String.not(): StringAndNewLine {
            return StringAndNewLine(this)
        }

        inline operator fun String.unaryPlus() {
            writer.write(this)
        }

        inline operator fun StringAndNewLine.unaryPlus() {
            writer.write(this.str)
            writer.write("\n")
        }

        inline operator fun KSDeclaration.unaryPlus() {
            val qName = qualifiedName!!.asString()
            if (qName in usedQName) {
                logger.warn("$Konst_sName meets a same qualified name: $qName.", this)
                return
            }
            usedQName.add(qName)
            val sName = simpleName.getShortName()
            val pathName = qName.removePrefix(packageName.asString()).removePrefix(".").replace('.', '_')
            +!"const val ${pathName}_qName = \"\"\"$qName\"\"\""
            +!"const val ${pathName}_sName = \"\"\"$sName\"\"\""
            +!""
        }
    }

    private val writerCache = mutableMapOf<PackageName, WriterWrapper>()

    override fun process(resolver: Resolver): List<KSAnnotated> {
        val nodes = resolver.getSymbolsWithAnnotation(Konst_qName, true)
        for (node in nodes) {
            when (node) {
                is KSDeclaration -> {
                    if (node.qualifiedName == null) {
                        logger.error("Local declarations are not supported yet", node)
                    }
                    val containingFile = node.containingFile ?: continue
                    val packageName = PackageName.of(node)
                    val writer = writerCache.getOrPut(packageName) {
                        newFile(containingFile, packageName) {
                            if (packageName.value.isNotEmpty()) {
                                +!"package ${packageName.value}"
                            }
                            +!""
                        }
                    }
                    writer.apply {
                        +node
                    }
                }
            }
        }
        for (writer in writerCache.values) {
            writer.close()
        }
        return emptyList()
    }

    private inline fun newFile(
        containingFile: KSFile,
        packageName: PackageName,
        block: WriterWrapper.() -> Unit
    ): WriterWrapper {
        val stream = codeGen.createNewFile(
            Dependencies(false, containingFile),
            packageName.value,
            Konst.FILE_NAME
        )
        return WriterWrapper(OutputStreamWriter(stream)).apply(block)
    }
}