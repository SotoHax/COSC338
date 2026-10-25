package com.example.myapplication

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import android.graphics.Color
import android.graphics.Typeface
import android.view.Gravity
import android.widget.LinearLayout
class MainActivity : AppCompatActivity() {

    private val targetWord = FourLetterWordList.getRandomFourLetterWord()

    private var guessesMade = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        val guessInput = findViewById<EditText>(R.id.guessInput)
        val submitButton = findViewById<Button>(R.id.submitButton)
        val remainingText = findViewById<TextView>(R.id.remainingText)
        val guessHistoryLayout =
            findViewById<LinearLayout>(R.id.guessHistoryLayout)

        submitButton.setOnClickListener {

            val guess = guessInput.text.toString().uppercase()

            if (guess.length != 4 || !guess.all { it.isLetter() }) {
                Toast.makeText(
                    this,
                    "Please enter a 4-letter word",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener


            }

            guessesMade++

            val result = checkGuess(guess)
            displayColoredGuess(guess, result, guessHistoryLayout)

            val guessesRemaining = 3 - guessesMade

            remainingText.text = "Guesses remaining: $guessesRemaining"

            guessInput.text.clear()

            if (guessesMade >= 3) {
                submitButton.isEnabled = false
                remainingText.text = "Game Over! The word was $targetWord"
            }
        }
    }
    private fun checkGuess(guess: String): String {
        val result = CharArray(4) { 'X' }
        val remainingLetters = targetWord.toMutableList()

        for (i in guess.indices) {
            if (guess[i] == targetWord[i]) {
                result[i] = 'O'
                remainingLetters.remove(guess[i])
            }
        }

        for (i in guess.indices) {
            if (result[i] == 'O') continue

            if (remainingLetters.contains(guess[i])) {
                result[i] = '+'
                remainingLetters.remove(guess[i])
            }
        }

        return String(result)

    }
    private fun displayColoredGuess(
        guess: String,
        result: String,
        container: LinearLayout
    ) {
        // Create a horizontal row
        val row = LinearLayout(this)
        row.orientation = LinearLayout.HORIZONTAL
        row.gravity = Gravity.CENTER

        // Create a tile for each letter
        for (i in guess.indices) {

            val tile = TextView(this)

            tile.text = guess[i].toString()
            tile.textSize = 24f
            tile.setTextColor(Color.WHITE)
            tile.setTypeface(null, Typeface.BOLD)
            tile.gravity = Gravity.CENTER

            // Set the tile color based on correctness
            val tileColor = when (result[i]) {
                'O' -> Color.rgb(83, 141, 78)    // Green
                '+' -> Color.rgb(181, 159, 59)   // Yellow
                else -> Color.rgb(120, 124, 126) // Gray
            }

            tile.setBackgroundColor(tileColor)

            // Set tile size and spacing
            val params = LinearLayout.LayoutParams(
                (55 * resources.displayMetrics.density).toInt(),
                (55 * resources.displayMetrics.density).toInt()
            )

            val margin = (4 * resources.displayMetrics.density).toInt()
            params.setMargins(margin, margin, margin, margin)

            tile.layoutParams = params

            // Add tile to the row
            row.addView(tile)
        }

        // Add the completed row to the screen
        container.addView(row)
    }
}

