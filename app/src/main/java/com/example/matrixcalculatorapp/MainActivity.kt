package com.example.matrixcalculatorapp

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.util.Locale

class MainActivity : AppCompatActivity() {

    // Load the native library
    companion object {
        init {
            System.loadLibrary("matrixlib") // Matches CMakeLists.txt library name
        }
    }

    // Native method declarations
    private external fun addMatrices(matrixA: DoubleArray, matrixB: DoubleArray, rows: Int, cols: Int): DoubleArray
    private external fun subtractMatrices(matrixA: DoubleArray, matrixB: DoubleArray, rows: Int, cols: Int): DoubleArray
    private external fun multiplyMatrices(matrixA: DoubleArray, matrixB: DoubleArray, rowsA: Int, colsA: Int, rowsB: Int, colsB: Int): DoubleArray
    private external fun divideMatrices(matrixA: DoubleArray, matrixB: DoubleArray, rows: Int, cols: Int): DoubleArray

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize UI components
        val rowsAEditText = findViewById<EditText>(R.id.rowsAEditText)
        val colsAEditText = findViewById<EditText>(R.id.colsAEditText)
        val rowsBEditText = findViewById<EditText>(R.id.rowsBEditText)
        val colsBEditText = findViewById<EditText>(R.id.colsBEditText)
        val matrixAEditText = findViewById<EditText>(R.id.matrixAEditText)
        val matrixBEditText = findViewById<EditText>(R.id.matrixBEditText)
        val resultTextView = findViewById<TextView>(R.id.resultTextView)

        val addButton = findViewById<Button>(R.id.addButton)
        val subtractButton = findViewById<Button>(R.id.subtractButton)
        val multiplyButton = findViewById<Button>(R.id.multiplyButton)
        val divideButton = findViewById<Button>(R.id.divideButton)

        // Set button click listeners
        addButton.setOnClickListener {
            performOperation(rowsAEditText, colsAEditText, rowsBEditText, colsBEditText, matrixAEditText, matrixBEditText, resultTextView, "add")
        }
        subtractButton.setOnClickListener {
            performOperation(rowsAEditText, colsAEditText, rowsBEditText, colsBEditText, matrixAEditText, matrixBEditText, resultTextView, "subtract")
        }
        multiplyButton.setOnClickListener {
            performOperation(rowsAEditText, colsAEditText, rowsBEditText, colsBEditText, matrixAEditText, matrixBEditText, resultTextView, "multiply")
        }
        divideButton.setOnClickListener {
            performOperation(rowsAEditText, colsAEditText, rowsBEditText, colsBEditText, matrixAEditText, matrixBEditText, resultTextView, "divide")
        }
    }

    private fun performOperation(
        rowsAEditText: EditText,
        colsAEditText: EditText,
        rowsBEditText: EditText,
        colsBEditText: EditText,
        matrixAEditText: EditText,
        matrixBEditText: EditText,
        resultTextView: TextView,
        operation: String
    ) {
        // Clear previous result or error message
        resultTextView.text = ""

        val rowsA = rowsAEditText.text.toString().toIntOrNull()
        val colsA = colsAEditText.text.toString().toIntOrNull()
        val rowsB = rowsBEditText.text.toString().toIntOrNull()
        val colsB = colsBEditText.text.toString().toIntOrNull()
        val matrixAString = matrixAEditText.text.toString().trim()
        val matrixBString = matrixBEditText.text.toString().trim()

        // Log inputs for debugging
        Log.d("MatrixCalculator", "Matrix A: ${rowsA}x${colsA}, Matrix B: ${rowsB}x${colsB}")
        Log.d("MatrixCalculator", "Matrix A Input: '$matrixAString'")
        Log.d("MatrixCalculator", "Matrix B Input: '$matrixBString'")

        // Validate dimensions
        if (rowsA == null || colsA == null || rowsB == null || colsB == null || rowsA <= 0 || colsA <= 0 || rowsB <= 0 || colsB <= 0) {
            resultTextView.text = "Invalid dimensions. Enter positive integers for rows and columns."
            return
        }

        // Validate matrix inputs
        if (matrixAString.isEmpty() || matrixBString.isEmpty()) {
            resultTextView.text = "Matrix inputs cannot be empty."
            return
        }

        // Validate operation compatibility
        when (operation) {
            "add", "subtract", "divide" -> {
                if (rowsA != rowsB || colsA != colsB) {
                    resultTextView.text = "For $operation, matrices must have the same dimensions (A: ${rowsA}x${colsA}, B: ${rowsB}x${colsB})."
                    return
                }
            }
            "multiply" -> {
                if (colsA != rowsB) {
                    resultTextView.text = "For multiplication, columns of A ($colsA) must equal rows of B ($rowsB)."
                    return
                }
            }
        }

        try {
            // Parse matrix inputs
            val matrixATokens = matrixAString.replace(",", " ").trim().split("\\s+".toRegex())
            val matrixBTokens = matrixBString.replace(",", " ").trim().split("\\s+".toRegex())

            // Log tokens for debugging
            Log.d("MatrixCalculator", "Matrix A Tokens: ${matrixATokens.joinToString()}")
            Log.d("MatrixCalculator", "Matrix B Tokens: ${matrixBTokens.joinToString()}")

            val matrixA = matrixATokens
                .filter { it.isNotEmpty() }
                .map {
                    try {
                        it.toDouble()
                    } catch (e: NumberFormatException) {
                        throw NumberFormatException("Invalid number: $it")
                    }
                }
                .filter { it.isFinite() }
                .toDoubleArray()
            val matrixB = matrixBTokens
                .filter { it.isNotEmpty() }
                .map {
                    try {
                        it.toDouble()
                    } catch (e: NumberFormatException) {
                        throw NumberFormatException("Invalid number: $it")
                    }
                }
                .filter { it.isFinite() }
                .toDoubleArray()

            Log.d("MatrixCalculator", "Parsed Matrix A: ${matrixA.joinToString()}")
            Log.d("MatrixCalculator", "Parsed Matrix B: ${matrixB.joinToString()}")

            // Validate matrix sizes
            val expectedSizeA = rowsA * colsA
            val expectedSizeB = rowsB * colsB
            if (matrixA.size != expectedSizeA || matrixB.size != expectedSizeB) {
                resultTextView.text = "Matrix size mismatch. Expected $expectedSizeA elements for A (${rowsA}x${colsA}), $expectedSizeB for B (${rowsB}x${colsB}), got ${matrixA.size} for A, ${matrixB.size} for B."
                return
            }

            // Perform operation
            val result = when (operation) {
                "add" -> addMatrices(matrixA, matrixB, rowsA, colsA)
                "subtract" -> subtractMatrices(matrixA, matrixB, rowsA, colsA)
                "multiply" -> multiplyMatrices(matrixA, matrixB, rowsA, colsA, rowsB, colsB)
                "divide" -> {
                    val divResult = divideMatrices(matrixA, matrixB, rowsA, colsA)
                    if (divResult.any { !it.isFinite() }) {
                        resultTextView.text = "Division error: Division by zero detected."
                        DoubleArray(rowsA * colsA) // Return empty array
                    } else {
                        divResult
                    }
                }
                else -> {
                    resultTextView.text = "Invalid operation."
                    DoubleArray(0)
                }
            }

            // Log result for debugging
            Log.d("MatrixCalculator", "Operation: $operation, Result: ${result.joinToString()}")

            // Format and display result
            if (result.isNotEmpty()) {
                val resultRows = if (operation == "multiply") rowsA else rowsA
                val resultCols = if (operation == "multiply") colsB else colsA
                val formattedResult = formatMatrix(result, resultRows, resultCols)
                Log.d("MatrixCalculator", "Formatted Result: '$formattedResult'")
                resultTextView.text = formattedResult
                resultTextView.invalidate() // Force UI refresh
            } else {
                Log.d("MatrixCalculator", "Result is empty, not updating TextView")
                resultTextView.text = "No result returned from operation."
            }
        } catch (e: NumberFormatException) {
            Log.e("MatrixCalculator", "NumberFormatException: ${e.message}")
            resultTextView.text = "Invalid number format. Use space or comma-separated numbers (e.g., 1.5, -2)."
        } catch (e: Exception) {
            Log.e("MatrixCalculator", "Unexpected error: ${e.message}")
            resultTextView.text = "An unexpected error occurred. Please check your inputs."
        }
    }

    private fun formatMatrix(matrix: DoubleArray, rows: Int, cols: Int): String {
        val builder = StringBuilder()
        for (i in matrix.indices) {
            builder.append(String.format(Locale.US, "%.2f", matrix[i])).append("\t")
            if ((i + 1) % cols == 0) {
                builder.append("\n")
            }
        }
        return builder.toString()
    }
}