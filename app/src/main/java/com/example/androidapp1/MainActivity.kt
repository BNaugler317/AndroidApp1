package com.example.androidapp1

import kotlin.random.Random
import android.content.Context
import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val dakkaCountTextView = findViewById<TextView>(R.id.dakka_count_text_view)
        val dakkaButton = findViewById<Button>(R.id.dakka_button)
        val timeLeftTextView = findViewById<TextView>(R.id.time_left_text_view)
        val highScoreTextView = findViewById<TextView>(R.id.high_score_text_view)

        val waaaghSound = MediaPlayer.create(this, R.raw.whaag)

        val sharedPreferences = getSharedPreferences("OrkDakkaPrefs", Context.MODE_PRIVATE)

        var dakkaCount = 0
        var highScore = sharedPreferences.getInt("HIGH_SCORE", 0)

        highScoreTextView.text = "High Score: $highScore"

        var gameStarted = false

        val countDownTimer = object : CountDownTimer(20000, 1000) {

            override fun onTick(millisUntilFinished: Long) {
                val secondsLeft = millisUntilFinished / 1000

                timeLeftTextView.text = "Time Left: $secondsLeft"
            }

            override fun onFinish() {

                timeLeftTextView.text = "Time Left: 0"

                if (dakkaCount > highScore) {
                    highScore = dakkaCount
                    highScoreTextView.text = "High Score: $highScore"

                    sharedPreferences.edit()
                        .putInt("HIGH_SCORE", highScore)
                        .apply()
                }
                dakkaButton.translationX = 0f
                dakkaButton.text = "PLAY AGAIN"
                gameStarted = false
            }
        }

        dakkaButton.setOnClickListener {

            if (!gameStarted && dakkaButton.text == "PLAY AGAIN") {

                dakkaCount = 0

                dakkaCountTextView.text = "Dakka Fired: 0"

                timeLeftTextView.text = "Time Left: 20"

                dakkaButton.text = "DAKKA!"

                gameStarted = true

                waaaghSound.start()

                countDownTimer.start()

            } else {

                if (!gameStarted) {
                    countDownTimer.start()
                    gameStarted = true
                    waaaghSound.start()
                }

                dakkaCount++

                dakkaCountTextView.text = "Dakka Fired: $dakkaCount"

                val parentWidth = (dakkaButton.parent as android.view.View).width
                val buttonWidth = dakkaButton.width

                val maxMove = (parentWidth - buttonWidth) / 2f

                dakkaButton.translationX = Random.nextFloat() * (maxMove * 2) - maxMove

            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}