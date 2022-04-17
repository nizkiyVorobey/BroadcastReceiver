package com.example.broadcast

import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.broadcast.MyReceiver.Companion.EXTRA_CLICKED_COUNT

class MainActivity : AppCompatActivity() {

    private val receiver = MyReceiver()
    private var clickedCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // intentFiler потрібен, щоб нащ receiver реагував не на всі події, а тільки на ті, які нам потрібні
        val intentFiler = IntentFilter().apply {
            addAction(Intent.ACTION_BATTERY_LOW)
            addAction(Intent.ACTION_AIRPLANE_MODE_CHANGED)
            addAction(MyReceiver.ACTION_CLICKED)
        }
        registerReceiver(receiver, intentFiler)

        findViewById<Button>(R.id.button).setOnClickListener {
            val intent = Intent(MyReceiver.ACTION_CLICKED).apply {
                putExtra(EXTRA_CLICKED_COUNT,  ++clickedCount)
            }

            sendBroadcast(intent)
        }
    }

    // Якщо ми підпислися на Receiver, то ми маємо і відписатися від нього, щоб не було відтоку пам'яті
    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
    }
}