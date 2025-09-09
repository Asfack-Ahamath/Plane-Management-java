# Plane Management Application

This is a Java application for managing plane seat bookings with both CLI and GUI interfaces.

## Project Structure

The project has been reorganized into a clean package structure:

```
src/main/java/org/example/PlaneManagement/
├── UiSelector.java          # Main entry point - allows user to choose between CLI and GUI
├── cli/                     # Command Line Interface package
│   └── PlaneManagement.java # CLI implementation
├── gui/                     # Graphical User Interface package
│   ├── JavaFXUI.java       # JavaFX application launcher
│   └── FXController.java   # JavaFX controller
└── common/                  # Shared model classes
    ├── Person.java         # Person data model
    └── Ticket.java         # Ticket data model
```

## Resources Structure

```
src/main/resources/org/example/PlaneManagement/
└── gui/
    └── plane_managementUI.fxml  # JavaFX UI layout file
```

## How to Run

### Using Maven (Recommended)

1. **Run the UI Selector:**
   ```bash
   mvn clean javafx:run
   ```

2. **Or compile and run manually:**
   ```bash
   mvn clean compile
   java --module-path "path-to-javafx-libs" -m org.example.w2052789_PlaneManagement/org.example.PlaneManagement.UiSelector
   ```

### Using IntelliJ IDEA

1. **Set the main class** to `org.example.PlaneManagement.UiSelector`
2. **Add JavaFX modules** to the module path
3. **Run the application** - it will show the UI selector menu

### Using Maven Wrapper

If Maven is not installed globally:
```bash
./mvnw.cmd clean javafx:run  # Windows
./mvnw clean javafx:run      # Unix/Linux/Mac
```

## Application Flow

1. **Start the application** - The `UiSelector` class is the main entry point
2. **Choose Interface** - User selects between:
   - Console UI (CLI)
   - JavaFX UI (GUI)
   - Exit
3. **Use the Application** - The selected interface provides the same functionality:
   - Buy a seat
   - Cancel a seat
   - Find first available seat
   - Show seating plan
   - Print tickets information
   - Search for a ticket

## Features

### 🎨 **Modern Professional UI**
- **Material Design**: Beautiful JFoenix components with smooth animations
- **Professional Styling**: Custom CSS with gradient backgrounds and modern typography
- **Interactive Elements**: Animated buttons, hover effects, and visual feedback
- **FontAwesome Icons**: Professional icons throughout the interface
- **Responsive Design**: Adapts to different screen sizes

### 🚀 **Enhanced Functionality**
- **Dual Interface Support**: Choose between command-line and graphical interfaces
- **Real-time Validation**: Live input validation with visual feedback
- **Seat Management**: Book and cancel seats with enhanced validation
- **Pricing System**: Different prices based on seat location with real-time display
- **Ticket Persistence**: Tickets are saved to individual files
- **Search Functionality**: Find tickets by seat location
- **Seating Visualization**: Interactive seating plan display
- **Live Statistics**: Real-time dashboard showing seat availability
- **Enhanced Messages**: Professional success/error messages with emojis

### 🛠️ **Technical Features**
- **Modern Libraries**: ControlsFX, JFoenix, Ikonli integration
- **Animation System**: Smooth transitions and button animations
- **Input Validation**: Real-time email and form validation
- **Professional Layout**: Card-based design with proper spacing
- **Accessibility**: Clear visual hierarchy and user guidance

## Dependencies

- **Java 21+** - Modern Java runtime
- **JavaFX 21** - Core GUI framework (Controls & FXML)
- **Maven** - Build and dependency management

### 🎨 **Styling Approach**
The application uses **programmatic CSS loading** for maximum compatibility and reliability. All beautiful styling is applied through a custom CSS file loaded at runtime.

## Package Organization

- **`org.example.PlaneManagement`**: Root package containing the UI selector
- **`org.example.PlaneManagement.cli`**: Command-line interface implementation
- **`org.example.PlaneManagement.gui`**: Graphical user interface implementation
- **`org.example.PlaneManagement.common`**: Shared data models and utilities

This structure provides clear separation of concerns and makes the codebase more maintainable and extensible.
