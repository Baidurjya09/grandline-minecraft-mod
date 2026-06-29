# Project Grand Line

A multiplayer-first Minecraft Fabric mod featuring pirate adventure gameplay with custom abilities, fruits, and naval combat systems.

## Project Status

**Phase**: 0 - Foundation  
**Version**: 0.1.0  
**Minecraft**: 1.21  
**Status**: ✅ Foundation Complete

## Features (Phase 0)

- ✅ Complete Gradle + Fabric build system
- ✅ Mod initialization framework
- ✅ Configuration management system
- ✅ Network packet infrastructure
- ✅ Registry system foundation
- ✅ Logging and error handling
- ✅ Client/Server separation
- ✅ Multiplayer-ready architecture

## Requirements

- **Java 21** or higher
- **Minecraft 1.21**
- **Fabric Loader 0.16.5+**
- **Fabric API 0.102.0+**

## Setup

### For Development

1. **Clone the repository**
```bash
git clone <repository-url>
cd grandline
```

2. **Build the mod**
```bash
./gradlew build
```

3. **Run Minecraft (Client)**
```bash
./gradlew runClient
```

4. **Run Dedicated Server**
```bash
./gradlew runServer
```

5. **Generate IDE files**

For IntelliJ IDEA:
```bash
./gradlew idea
```

For Eclipse:
```bash
./gradlew eclipse
```

### For Players

1. Install **Fabric Loader** for Minecraft 1.21
2. Download **Fabric API** from Modrinth or CurseForge
3. Place the Grand Line mod JAR in your `mods` folder
4. Launch Minecraft with the Fabric profile

## Project Structure

```
grandline/
├── src/main/java/com/grandline/
│   ├── GrandLineMod.java              # Main mod entry point
│   ├── client/
│   │   └── GrandLineModClient.java    # Client initialization
│   ├── server/
│   │   └── GrandLineModServer.java    # Server initialization
│   └── core/
│       ├── GrandLineRegistry.java     # Content registration
│       ├── GrandLineUtil.java         # Utility methods
│       ├── config/
│       │   ├── ConfigManager.java     # Configuration loader
│       │   └── GrandLineConfig.java   # Configuration model
│       └── network/
│           └── NetworkManager.java    # Packet handling
├── src/main/resources/
│   ├── fabric.mod.json               # Mod metadata
│   ├── grandline.mixins.json         # Mixin configuration
│   └── assets/grandline/             # Resources (future)
├── build.gradle                      # Build configuration
├── gradle.properties                 # Project properties
└── settings.gradle                   # Gradle settings
```

## Configuration

Configuration file: `config/grandline.json`

The mod will automatically create a default configuration on first launch.

### Default Settings

```json
{
  "general": {
    "enableMod": true,
    "locale": "en_us"
  },
  "network": {
    "packetCompressionThreshold": 256,
    "maxPacketSize": 32767,
    "enablePacketLogging": false
  },
  "performance": {
    "tickRateHz": 20,
    "enableAsyncOperations": true,
    "maxCachedEntries": 1000
  },
  "debug": {
    "enableDebugLogging": false,
    "enablePerformanceMetrics": false,
    "enableStackTraces": true
  }
}
```

## Architecture

### Design Principles

1. **Multiplayer First**: All systems are designed for dedicated server compatibility
2. **Server Authoritative**: Server validates all gameplay actions
3. **Modular Design**: Clean separation of concerns with dependency injection
4. **Performance Focused**: Minimal allocations, efficient algorithms
5. **Extensible**: Plugin-friendly architecture for future features

### Key Systems

- **Registry System**: Centralized registration for all game content
- **Configuration System**: JSON-based configuration with validation and hot-reload
- **Network System**: Packet-based communication with compression support
- **Logging System**: SLF4J logging with configurable levels

## Development Roadmap

### ✅ Phase 0: Foundation (COMPLETE)
- Project structure
- Build system
- Core infrastructure
- Configuration
- Networking foundation

### 🔄 Phase 1: Core Framework (NEXT)
- Event bus system
- Command framework
- Permission system
- Data serialization
- Resource management

### 📋 Phase 2: Player Data System
- Stats tracking
- Experience system
- Persistent storage
- Synchronization

### 📋 Phase 3: Ability Framework
- Cooldown engine
- Animation controller
- Particle framework
- Status effects

### 📋 Future Phases
- Fruit system
- Haki system
- Crew mechanics
- Ship combat
- World generation
- Boss encounters

## Testing

Run all tests:
```bash
./gradlew test
```

Run specific test:
```bash
./gradlew test --tests "com.grandline.*"
```

## Building

Create production JAR:
```bash
./gradlew build
```

Output: `build/libs/grandline-0.1.0.jar`

## Contributing

This project follows strict engineering standards:

1. All code must compile without warnings
2. All features must work on dedicated servers
3. All public APIs must have JavaDoc
4. All changes must include tests
5. All commits must follow conventional commit format

## License

MIT License - See LICENSE file for details

## Credits

**Development Team**: Grand Line Development Team  
**Engine**: Minecraft + Fabric  
**Inspiration**: Pirate adventure themes

---

**Note**: This mod is in active development. Features and APIs may change.
