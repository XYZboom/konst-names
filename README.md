# Konst Names

**Generate Compile-Time Constant Names for Kotlin Declarations**.

## Key Features

✨ **Zero Runtime Overhead** Generates constants at compile time via KSP, eliminating reflection.

💡 **IDE-Friendly** - Auto-generated code visible immediately with navigation support

📦 **Full Symbol Coverage** - Supports classes, properties, functions, and objects

## Quick Start

### 1. Add Dependencies

```kotlin
plugins {
    id("com.google.devtools.ksp") version "2.1.20-1.0.32"
}

dependencies {
    implementation("io.github.xyzboom:konst-names:0.1.0")
    ksp("io.github.xyzboom:konst-names:0.1.0")
}
```

### 2. Apply Annotation

```kotlin

```

