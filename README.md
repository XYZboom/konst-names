# Konst Names ![Maven Central Version](https://img.shields.io/maven-central/v/io.github.xyzboom/konst-names)

**Generate Compile-Time Constant Names for Kotlin Declarations**.

## Key Features

✨ **Zero Runtime Overhead** Generates constants at compile time via KSP, eliminating reflection.

💡 **IDE-Friendly** - Auto-generated code visible immediately with navigation support

📦 **Full Symbol Coverage** - Supports classes, properties, functions, and objects

## Quick Start

### 1. Add Dependencies

```kotlin
plugins {
    id("com.google.devtools.ksp") version "2.1.20-2.0.0"
}

dependencies {
    implementation("io.github.xyzboom:konst-names:0.1.3")
    ksp("io.github.xyzboom:konst-names:0.1.3")
}
```

### 2. Apply Annotation

Apply the annotation `@Konst` on the declarations that you want to use its const name.

```kotlin
import io.github.xyzboom.konst.ksp.Konst

@Konst
class MyClass {
    @Konst
    val myProperty: String = "sample property"

    @Konst
    fun myFunc() {
        println("called myFunc")
    }
}

fun main() {
    // qName suffix means qualified name, sName suffix means simple name
    println(MyClass_qName)
    println(MyClass_sName)
    println(MyClass_myProperty_qName)
    println(MyClass_myProperty_sName)
    println(MyClass_myFunc_qName)
    println(MyClass_myFunc_sName)
}
```

### 3. Enable IDE Support

Run `gradle kspKotlin` (or `gradle kspTestKotlin` if you are using Konst in test source). The generated code is at `build/generated/ksp`.
