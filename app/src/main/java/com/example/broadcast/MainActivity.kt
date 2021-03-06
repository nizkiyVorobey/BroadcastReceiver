package com.example.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ProgressBar
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.broadcast.MyReceiver.Companion.EXTRA_CLICKED_COUNT

class MainActivity : AppCompatActivity() {

//    private val receiver = MyReceiver()

    private lateinit var progressBar: ProgressBar

    private val localBroadcastReceiver by lazy {
        LocalBroadcastManager.getInstance(this)
    }

    /**
     * Коли нам треба в змінювати layout в залежності від даних з BroadcastReceiver, ми
     * створюємо анонімний клас, там де будемо змунювати layout
     *
     *
     * Зараз коли ми стоврюжмо якийсь BroadcastReceiver то будь який додаток зможе на нього підписатися,
     * це не дуже безпечно, якщо ми хочемо передати якісь персональні дані юзера. Якщо ми хочемо передавати лише
     * всередені нашої апки то трба використовувати LocalBroadcastManager
     */
    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == "loaded") {
                val percent = intent.getIntExtra("percent", 0)
                progressBar.progress = percent
            }
        }

    }

    private var clickedCount = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        progressBar = findViewById(R.id.progress_bar)

        // intentFiler потрібен, щоб нащ receiver реагував не на всі події, а тільки на ті, які нам потрібні
        val intentFiler = IntentFilter().apply {
            with(localBroadcastReceiver) {
                addAction(Intent.ACTION_BATTERY_LOW)
                addAction(Intent.ACTION_AIRPLANE_MODE_CHANGED)
                addAction(MyReceiver.ACTION_CLICKED)
                addAction("loaded")
            }
        }
        localBroadcastReceiver.registerReceiver(receiver, intentFiler)

        findViewById<Button>(R.id.button).setOnClickListener {
            val intent = Intent(MyReceiver.ACTION_CLICKED).apply {
                putExtra(EXTRA_CLICKED_COUNT, ++clickedCount)
            }

            sendBroadcast(intent)
        }

        Intent(this, MyService::class.java).apply {
            startService(this)
        }
    }

    // Якщо ми підпислися на Receiver, то ми маємо і відписатися від нього, щоб не було відтоку пам'яті
    override fun onDestroy() {
        super.onDestroy()
        localBroadcastReceiver.unregisterReceiver(receiver)
    }
}