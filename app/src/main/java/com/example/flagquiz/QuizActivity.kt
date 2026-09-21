package com.example.flagquiz

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.text.Normalizer
import java.util.Locale

class QuizActivity : AppCompatActivity() {
    private val flags = listOf(
        R.drawable.flag_br to "Brasil",
        R.drawable.flag_ar to "Argentina",
        R.drawable.flag_ca to "Canadá",
        R.drawable.flag_cl to "Chile",
        R.drawable.flag_uy to "Uruguai",
        R.drawable.flag_pe to "Peru",
        R.drawable.flag_co to "Colômbia",
        R.drawable.flag_mx to "México",
        R.drawable.flag_us to "Estados Unidos",
        R.drawable.flag_fr to "França",
        R.drawable.flag_de to "Alemanha",
        R.drawable.flag_it to "Itália",
        R.drawable.flag_es to "Espanha",
        R.drawable.flag_pt to "Portugal",
        R.drawable.flag_jp to "Japão",
        R.drawable.flag_cn to "China",
        R.drawable.flag_in to "Índia",
        R.drawable.flag_za to "África do Sul",
        R.drawable.flag_au to "Austrália",
        R.drawable.flag_eg to "Egito"
    )

    private lateinit var selectedFlags: IntArray
    private var questionIndex = 0
    private var score = 0
    private var answered = false
    private var correct = false

    private lateinit var questionText: TextView
    private lateinit var flagImage: ImageView
    private lateinit var answerInput: EditText
    private lateinit var checkButton: Button
    private lateinit var nextButton: Button
    private var normalTextColor = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_quiz)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { view, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(bars.left, bars.top, bars.right, bars.bottom)
            insets
        }

        // Usa os componentes da tela XML já criada pelo grupo.
        questionText = findViewById(R.id.textView3)
        flagImage = findViewById(R.id.imageView)
        answerInput = findViewById(R.id.editTextText2)
        checkButton = findViewById(R.id.button2)
        nextButton = findViewById(R.id.button3)
        normalTextColor = questionText.currentTextColor

        selectedFlags = savedInstanceState?.getIntArray("selected_flags")
            ?: flags.indices.shuffled().take(5).toIntArray()
        questionIndex = savedInstanceState?.getInt("question_index") ?: 0
        score = savedInstanceState?.getInt("score") ?: 0
        answered = savedInstanceState?.getBoolean("answered") ?: false
        correct = savedInstanceState?.getBoolean("correct") ?: false
        answerInput.setText(savedInstanceState?.getString("answer") ?: "")

        showQuestion()
        checkButton.setOnClickListener { checkAnswer() }
        nextButton.setOnClickListener { nextQuestion() }
    }

    private fun showQuestion() {
        val flag = flags[selectedFlags[questionIndex]]
        val number = "Pergunta ${questionIndex + 1} de 5"
        questionText.text = if (!answered) number else {
            val message = if (correct) "Correto!" else "Incorreto! Era ${flag.second}."
            "$number\n$message"
        }
        questionText.setTextColor(
            if (!answered) normalTextColor else if (correct) Color.rgb(27, 127, 58) else Color.rgb(198, 40, 40)
        )
        flagImage.setImageResource(flag.first)
        answerInput.isEnabled = !answered
        checkButton.isEnabled = !answered
        nextButton.isEnabled = answered
        nextButton.text = if (questionIndex == 4) "Ver resultado" else "Próximo"
    }

    private fun checkAnswer() {
        if (answered) return
        val answer = answerInput.text.toString().trim()
        if (answer.isEmpty()) {
            answerInput.error = "Digite uma resposta"
            return
        }

        correct = normalize(answer) == normalize(flags[selectedFlags[questionIndex]].second)
        answered = true
        if (correct) score += 20
        (getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager)
            .hideSoftInputFromWindow(answerInput.windowToken, 0)
        showQuestion()
    }

    private fun nextQuestion() {
        if (!answered) return
        if (questionIndex == 4) {
            val result = Intent(this, scoreActivity::class.java)
            result.putExtra("player_name", intent.getStringExtra("player_name"))
            result.putExtra("score", score)
            startActivity(result)
            finish()
        } else {
            questionIndex++
            answered = false
            correct = false
            answerInput.text.clear()
            showQuestion()
        }
    }

    private fun normalize(text: String): String = Normalizer
        .normalize(text.trim().lowercase(Locale.ROOT), Normalizer.Form.NFD)
        .replace(Regex("\\p{M}+"), "")
        .replace(Regex("\\s+"), " ")

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putIntArray("selected_flags", selectedFlags)
        outState.putInt("question_index", questionIndex)
        outState.putInt("score", score)
        outState.putBoolean("answered", answered)
        outState.putBoolean("correct", correct)
        outState.putString("answer", answerInput.text.toString())
        super.onSaveInstanceState(outState)
    }
}
