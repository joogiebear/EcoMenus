# EcoMenus Build Instructions

## Building the Plugin

Due to network restrictions in the current environment, the plugin cannot be built here. However, you can easily build it locally on your machine.

### Prerequisites
- Java 21 or later
- Git

### Build Steps

1. **Clone or pull the latest changes:**
   ```bash
   git pull origin claude/ecomenus-modernize-quality-011CV4BMkXYcJtWGoYuNWpLB
   ```

2. **Build the plugin:**
   ```bash
   cd EcoMenus
   ./gradlew clean shadowJar
   ```

3. **Find the built JAR:**
   The plugin JAR will be located at:
   ```
   build/libs/EcoMenus-0.6.0.jar
   ```

### What Was Updated

This modernized version includes:

- **Kotlin**: 1.7.10 → 1.9.25
- **Paper API**: 1.19.3 → 1.21.8
- **Java**: 17 → 21
- **Dependencies**: Updated Caffeine, JetBrains Annotations, Shadow plugin
- **Code Quality**: Fixed unsafe casts, added comprehensive KDoc documentation
- **Safety**: Better null handling and exception safety

### Testing

After building, copy the JAR to your Paper server's `plugins` folder and restart the server.

**Requirements:**
- Paper 1.21.3 or later
- Java 21 or later
- eco plugin (dependency)
- libreforge plugin (optional, for advanced features)

### Troubleshooting

If you encounter any build issues:

1. Ensure you have Java 21 installed:
   ```bash
   java -version
   ```

2. Clean the Gradle cache if needed:
   ```bash
   ./gradlew clean --refresh-dependencies
   ```

3. Make sure you're on the correct branch:
   ```bash
   git branch
   # Should show: claude/ecomenus-modernize-quality-011CV4BMkXYcJtWGoYuNWpLB
   ```

## GitHub Actions Alternative

If you don't want to build locally, you can also set up GitHub Actions to build the plugin automatically. The repository could benefit from a CI/CD workflow that builds and publishes artifacts on push.
