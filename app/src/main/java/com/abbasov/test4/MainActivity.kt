package com.abbasov.test4

import android.graphics.Bitmap
import android.os.Bundle
import android.os.Handler
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.abbasov.test4.databinding.ActivityMainBinding
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private val TAG = "RxKotlinActivity"
    var str = "https://kun.uz/"
    var runnable: Runnable? = null
    var mHandler: Handler? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        mHandler = Handler()
        runnable = Runnable {
            onResume()

        }

        val web = webview()
        web.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .debounce (
                4L, TimeUnit.SECONDS
            )
            .subscribe {
                str="https://$it"
                mHandler!!.postDelayed(runnable!!, 1000)
            }




    }

    override fun onResume() {
        super.onResume()
        binding.webView.loadUrl(str)
        Toast.makeText(this, "$str", Toast.LENGTH_SHORT).show()
        binding.webView.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
            }
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
            }
        }
    }

    fun webview(): Observable<String> {
        return Observable.create { emitter ->
            binding.url.addTextChangedListener {
                emitter.onNext(binding.url.text.toString())
            }
        }
    }
}