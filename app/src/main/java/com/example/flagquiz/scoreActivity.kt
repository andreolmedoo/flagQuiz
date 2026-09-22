package com.example.flagquiz

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class scoreActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_score)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val playerName = findViewById<TextView>(R.id.textView5)
        val scoreText = findViewById<TextView>(R.id.textView6)
        val backButton = findViewById<Button>(R.id.button4)

        // Recupera os dados enviados pela QuizActivity via Intent
        val bundle = intent.extras
        if (bundle != null) {
            playerName.text = "Jogador: ${bundle.getString("player_name")}"
            scoreText.text = "Pontuação final: ${bundle.getInt("score")} de 100 pontos"
        }

        // Volta para a tela inicial para recomeçar o jogo
        backButton.setOnClickListener {
            val main = Intent(this, MainActivity::class.java)
            startActivity(main)
            finish()
        }
    }
}
