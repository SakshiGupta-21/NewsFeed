package com.example.newsfeed

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.browser.customtabs.CustomTabsIntent
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject

class MainActivity : AppCompatActivity(), NewsItemClicked {
// hello comment added
    private lateinit var mAdapter: NewsListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView.layoutManager = LinearLayoutManager(this)
        fetchData()
        mAdapter = NewsListAdapter(this)
        recyclerView.adapter = mAdapter
    }

    private fun fetchData() {
        val url = "https://api.nytimes.com/svc/mostpopular/v2/viewed/7.json?api-key=0xo1InbYoxUT6Ryq6Ml70wyHkUpz0wtD"
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET,
            url,
            null,
            Response.Listener {
                val newsJsonArray = it.getJSONArray("results")
                val newsArray = ArrayList<News>()
                for(i in 0 until newsJsonArray.length()) {
                    val newsJsonObject = newsJsonArray.getJSONObject(i)

                    val newsJsonObject2=newsJsonObject.getJSONArray("media")
                    val newsJsonObject3=newsJsonObject2.getJSONObject(0)
                    val newsJsonObject4=newsJsonObject3.getJSONArray("media-metadata")
                    val newsJsonObject5=newsJsonObject4.getJSONObject(2)


                    Log.d("SEE",newsJsonObject.toString())

                    Log.d("newsJsonObject2",newsJsonObject2.toString())
                    Log.d("newsJsonObject3",newsJsonObject3.toString())
                    Log.d("newsJsonObject4",newsJsonObject4.toString())
                    val news = News(
                        newsJsonObject.getString("title"),
                        newsJsonObject.getString("byline"),
                        newsJsonObject.getString("url"),
                        newsJsonObject5.getString("url")

                    )
                    newsArray.add(news)
                }

                mAdapter.updateNews(newsArray)
            },
            Response.ErrorListener {
                Log.d("error","error")
            }
        )
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }

    override fun onItemClicked(item: News) {
        val builder =  CustomTabsIntent.Builder()
        val customTabsIntent = builder.build()
        customTabsIntent.launchUrl(this, Uri.parse(item.url))
    }
}