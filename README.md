# Konst Names ![Maven Central Version](https://img.shields.io/maven-central/v/io.github.xyzboom.konst/io.github.xyzboom.konst.gradle.plugin)

**Generate Compile-Time Constant Names for Kotlin Declarations**.

## Key Features

✨ **Zero Runtime Overhead** Generates constants at compile time via compiler plugin, eliminating reflection.

📦 **Full Symbol Coverage** Supports classes, properties, functions, and objects (the newest version only supports classes)

## Quick Start

### 1. Add Dependencies and Setup Plugin

Add Maven Central into `setting.gradle.kts`

```kotlin
pluginManagement {
    repositories {
        mavenCentral() // add this line, if you do not have `pluginManagement` block, add this whole block
        gradlePluginPortal()
    }
}
```

Add dependencies into `build.gradle.kts`

```kotlin
plugins {
    kotlin("jvm") version "2.1.20"
    id("io.github.xyzboom.konst") version "0.3.0" // add this line
}
```

Setup plugin in `build.gradle.kts` using a `konst` block. If you are familiar with allopen plugin, this will be really easy.

```kotlin
konst {
    annotation("org.example.YourAnnotation")
}
```

### 2. Create and Apply Annotation

Create and apply the annotation `@YourAnnotation` on the `const val` property.

```kotlin
package org.example

annotation class YourAnnotation
```
```kotlin
import org.example.YourAnnotation

annotation class A(
    val value: String
)

class B

@YourAnnotation
const val a = "B simpleName: ${B::class.simpleName}; A qualifiedName: ${A::class.qualifiedName}"

@A(a)
fun main() {
    println(a)
}
```

Your IDE may complain "[CONST_VAL_WITH_NON_CONST_INITIALIZER] Const 'val' initializer should be a constant value" at `const val a` line and "[ANNOTATION_ARGUMENT_MUST_BE_CONST] An annotation argument must be a compile-time constant" at `@A(a)`. But this code compiles with konst plugin on.
