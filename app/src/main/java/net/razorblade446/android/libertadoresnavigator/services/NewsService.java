package net.razorblade446.android.libertadoresnavigator.services;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;
import com.einmalfel.earl.EarlParser;
import com.einmalfel.earl.Feed;
import com.einmalfel.earl.Item;
import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import net.razorblade446.android.libertadoresnavigator.NavigatorApplication;
import net.razorblade446.android.libertadoresnavigator.common.dtos.Blog;
import net.razorblade446.android.libertadoresnavigator.common.dtos.GetNewsMessage;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;

public class NewsService extends IntentService {
    public static final String ACTION_GET_NEWS_DONE =
            "net.razorblade446.android.libertadoresnavigator.action.get_news.done";

    public static final String ACTION_GET_NEWS =
            "net.razorblade446.android.libertadoresnavigator.action.get_news";

    private static String NewsPath =
            "http://www.ulibertadores.edu.co/index.php/mas-noticias?view=latest&format=feed&type=rss";

    private ClearableCookieJar cookies;

    private OkHttpClient client;

    public NewsService() {
        super("NewsService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        cookies = new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(this));
        client = new OkHttpClient.Builder().cookieJar(cookies).build();

        ArrayList<Blog> blogItems = new ArrayList<>();

        try {
            blogItems = this.getNews();
        } catch (Exception ex) {
            Log.e(NavigatorApplication.TAG, "Unexpected exception: " + ex.getMessage());
        }

        Intent responseIntent = new Intent();
        responseIntent.setAction(ACTION_GET_NEWS_DONE);
        responseIntent.putParcelableArrayListExtra("news", blogItems);
        sendBroadcast(responseIntent);

    }

    private ArrayList<Blog> getNews () throws Exception{

        ArrayList<Blog> news = new ArrayList<>();

        Request newsRequest = new Request.Builder()
                .url(NewsPath)
                .build();

        Response newsResponse = client.newCall(newsRequest).execute();

        if(!newsResponse.isSuccessful()) {
            throw new IOException("Unexpected response " + newsResponse);
        }

        Document doc = Jsoup.parse(newsResponse.body().byteStream(),
                null,
                newsResponse.header("Content-Location"));

        Elements items = doc.select("item");

        for(Element elm: items) {
            Blog blog = new Blog();
            blog.setTitle(elm.select("title").first().text());
            blog.setLink(elm.select("link").first().text());
            blog.setPubDate(elm.select("pubDate").first().text());
            blog.setDescription(elm.select("description").first().text());
            news.add(blog);
        }

        return news;

    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void getNews(GetNewsMessage message) {

        try {
            InputStream inputStream =
                    new URL("http://www.ulibertadores.edu.co/index.php/mas-noticias?view=latest&format=feed&type=rss")
                            .openConnection()
                            .getInputStream();
            Feed feed = EarlParser.parseOrThrow(inputStream, 0);

            for (Item item : feed.getItems()) {
                Log.i(NavigatorApplication.TAG, item.getTitle());
            }

        } catch (Exception ex) {

        }

    }
}
