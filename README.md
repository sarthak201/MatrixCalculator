MatrixCalculatorApp

MatrixCalculatorApp is an Android app for performing matrix operations (addition, subtraction, multiplication, division) on square and non-square matrices. Built with Kotlin and native C++ (via JNI), it features a modern Material Design UI.

Features





Supports square (e.g., 2x2) and non-square (e.g., 2x3) matrices.



Operations: Add, Subtract, Multiply, Divide (element-wise).



Validates matrix compatibility and input sizes.



Clean UI with input fields, colored buttons, and a card-based result display.



Error handling for invalid inputs.

Implementation





UI: Uses ConstraintLayout, TextInputLayout, and CardView for a responsive, modern interface.



Logic: Kotlin for input parsing, validation, and result formatting; C++ for efficient matrix computations.



Build: Gradle with Material Components; CMake for native library.

Prerequisites





Android Studio (Koala | 2024.1.1 or later)



NDK for native C++ build



JDK 17+



Min SDK: 21, Target SDK: 34

Setup





Clone the repository: git clone https://github.com/sarthak201/MatrixCalculatorApp.git



Open in Android Studio and sync with Gradle.



Install NDK via SDK Tools in Android Studio.



Build and run on a device or emulator.

Usage





Enter rows and columns for Matrix A and B (e.g., 2x2).



Input matrix elements (e.g., 1 2 3 4 for a 2x2 matrix).



Select an operation (Add, Subtract, Multiply, Divide).



View the result in a card, with errors for invalid inputs.

Testing





Test square matrices (e.g., 2x2: 1 2 3 4, 2 3 4 5).



Test non-square matrices (e.g., 2x3 multiplied by 3x2).



Verify error messages for invalid inputs or incompatible dimensions.

Debugging





Use Logcat (filter: MatrixCalculator) to check input parsing, operation results, and errors.



Ensure result display is visible in the UI.
