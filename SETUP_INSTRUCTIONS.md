# Setup Instructions - Project Grand Line

## Quick Start

This guide will help you set up the development environment for Project Grand Line.

### Prerequisites

Before starting, ensure you have:

1. **Java Development Kit (JDK) 21**
   - Download from: https://adoptium.net/ (recommended)
   - Or: https://www.oracle.com/java/technologies/downloads/
   - Verify: `java -version` should show version 21

2. **Git** (already initialized in this project)
   - Download from: https://git-scm.com/
   - Verify: `git --version`

3. **An IDE** (choose one):
   - IntelliJ IDEA Community/Ultimate (recommended)
   - Visual Studio Code with Java extensions
   - Eclipse

---

## Step 1: Install Java 21

### Windows
1. Download JDK 21 from https://adoptium.net/
2. Run the installer
3. Add to PATH if not automatic
4. Verify: Open new terminal and run `java -version`

### Linux
```bash
sudo apt install openjdk-21-jdk  # Ubuntu/Debian
```

### macOS
```bash
brew install openjdk@21
```

---

## Step 2: Initialize Gradle Wrapper

The Gradle wrapper ensures everyone uses the same Gradle version.

### Option A: Using Installed Gradle (if you have it)
```bash
gradle wrapper --gradle-version 8.8
```

### Option B: Download Gradle Wrapper Manually
1. Download from: https://services.gradle.org/distributions/gradle-8.8-bin.zip
2. Extract to `gradle/wrapper/` directory
3. Or use your IDE to sync the Gradle project (it will download automatically)

### Option C: Use IDE Integration (Easiest)
- IntelliJ IDEA: Open project → It will auto-download Gradle
- VS Code: Open folder → Java extension will handle it

---

## Step 3: Import Project in IDE

### IntelliJ IDEA (Recommended)

1. **Open IntelliJ IDEA**
2. **File → Open**
3. **Select the project folder** containing `build.gradle`
4. **Wait for Gradle sync** (bottom right corner shows progress)
5. **Trust the project** if prompted
6. **Wait for indexing** to complete

The IDE will automatically:
- Download Gradle 8.8
- Download all dependencies
- Set up Minecraft development environment
- Configure run configurations

### Visual Studio Code

1. **Install Extensions**:
   - Extension Pack for Java
   - Gradle for Java

2. **Open Folder**: Select the project folder

3. **Wait for Setup**: Java extension will download dependencies

### Eclipse

1. **File → Import → Gradle → Existing Gradle Project**
2. **Select project folder**
3. **Finish and wait for build**

---

## Step 4: Build the Project

Once Gradle is set up:

### Using Terminal
```bash
# Windows
.\gradlew.bat build

# Linux/Mac
./gradlew build
```

### Using IDE
- IntelliJ: Click "Build" → "Build Project"
- VS Code: Run task → "build"

**Expected Output**:
```
BUILD SUCCESSFUL in 30s
```

---

## Step 5: Run Tests

### Using Terminal
```bash
# Windows
.\gradlew.bat test

# Linux/Mac
./gradlew test
```

### Using IDE
- Right-click on `src/test/java` → Run All Tests

**Expected**: All 20 tests should pass ✅

---

## Step 6: Run Minecraft

### Client (with GUI)
```bash
.\gradlew.bat runClient
```

Or in IntelliJ: Run configuration "Minecraft Client"

### Dedicated Server
```bash
.\gradlew.bat runServer
```

**First Launch**: Will take 2-5 minutes to download Minecraft and set up

---

## Troubleshooting

### "Could not find or load main class GradleWrapperMain"
**Solution**: The Gradle wrapper JAR needs to be downloaded
- Use Option C (IDE integration) from Step 2
- Or manually run `gradle wrapper` if you have Gradle installed

### "Java version mismatch"
**Solution**: Ensure JAVA_HOME points to JDK 21
```bash
# Windows (PowerShell)
$env:JAVA_HOME = "C:\Path\To\JDK21"

# Linux/Mac
export JAVA_HOME=/path/to/jdk21
```

### "Fabric API not found"
**Solution**: Run `./gradlew build` first - it will download all dependencies

### IDE not recognizing code
**Solution**: 
1. File → Invalidate Caches → Invalidate and Restart (IntelliJ)
2. Reimport Gradle project
3. Ensure Java 21 SDK is configured in IDE

### Build is slow
**Solution**: Add to `gradle.properties`:
```properties
org.gradle.daemon=true
org.gradle.parallel=true
org.gradle.caching=true
org.gradle.jvmargs=-Xmx4G
```

---

## Verification

Your setup is complete when:

- ✅ `./gradlew build` succeeds
- ✅ `./gradlew test` shows 20 passing tests
- ✅ IDE shows no errors in source files
- ✅ `./gradlew runClient` launches Minecraft
- ✅ Mod appears in mod list as "Project Grand Line v0.1.0"

---

## Next Steps

Once setup is complete:

1. **Read ARCHITECTURE.md** to understand the codebase
2. **Review Phase 0 Report** (`PHASE_0_REPORT.md`)
3. **Check README.md** for project overview
4. **Ready for Phase 1** development!

---

## Getting Help

- Check logs in `logs/` directory
- Review Gradle output for specific errors
- Ensure all prerequisites are installed
- Try cleaning: `./gradlew clean build`

---

**Setup Version**: 1.0  
**Last Updated**: Phase 0 Completion
