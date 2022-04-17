package com.example.broadcast

import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {

    private val receiver = MyReceiver()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // intentFiler потрібен, щоб нащ receiver реагував не на всі події, а тільки на ті, які нам потрібні
        val intentFiler = IntentFilter().apply {
            addAction(Intent.ACTION_BATTERY_LOW)
            addAction(Intent.ACTION_AIRPLANE_MODE_CHANGED)
        }
        registerReceiver(receiver, intentFiler)
    }

    // Якщо ми підпислися на Receiver, то ми маємо і відписатися від нього, щоб не було відтоку пам'яті
    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
    }
}