cmake_minimum_required(VERSION 3.4.1)

# Set the C++ standard
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=c++17")

# Define your native library
add_library(
        matrixlib               # Change library name to match System.loadLibrary
        SHARED
        native-lib.cpp)         # Keep the source file as is

# Link required libraries (C++ runtime)
find_library(
        log-lib
        log)

# Link against the C++ shared library for std::vector, std::string, etc.
target_link_libraries(
        matrixlib               # Update to match the library name
        ${log-lib}
        c++_shared)