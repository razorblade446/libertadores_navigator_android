package net.razorblade446.android.libertadoresnavigator.services;

import android.util.Log;
import com.einmalfel.earl.EarlParser;
import com.einmalfel.earl.Feed;
import com.einmalfel.earl.Item;
import net.razorblade446.android.libertadoresnavigator.NavigatorApplication;
import net.razorblade446.android.libertadoresnavigator.common.dtos.GetNewsMessage;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;

public class NewsService implements Serializable{
    private static final long serialVersionUID = 1L;

    private static class NewsServiceLoader {
        private static final NewsService instance = new NewsService();
    }

    private NewsService() {
        EventBus.getDefault().register(this);
    }

    public static NewsService getInstance () {
        return NewsServiceLoader.instance;
    }

    private NewsService readResolve() {
        return NewsServiceLoader.instance;
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
