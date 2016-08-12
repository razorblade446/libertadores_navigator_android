package net.razorblade446.android.libertadoresnavigator.services;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import net.razorblade446.android.libertadoresnavigator.NavigatorApplication;
import net.razorblade446.android.libertadoresnavigator.common.dtos.Schedule;
import okhttp3.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.w3c.dom.NodeList;
import org.xmlpull.v1.XmlPullParser;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UlisesService extends IntentService {

    public static final String ACTION_GET_SCHEDULE_DONE =
            "net.razorblade446.android.libertadoresnavigator.action.get_schedule.done";

    private static String UlisesPath =
            "http://integrado.libertadores.edu.co:7779/ulises/";

    private ClearableCookieJar cookies;

    private OkHttpClient client;

    public UlisesService() {
        super("UlisesService");

    }

    @Override
    protected void onHandleIntent(Intent intent) {

        cookies = new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(this));
        client = new OkHttpClient.Builder().cookieJar(cookies).build();

        String login = intent.getStringExtra("login");
        String password = intent.getStringExtra("password");

        ArrayList<Schedule> scheduleList = null;

        try {
            this.login(login, password);
            scheduleList = this.getSchedule();
        } catch (Exception ex) {
            Log.e(NavigatorApplication.TAG, ex.getLocalizedMessage());
        }

        Intent responseIntent = new Intent();
        responseIntent.setAction(ACTION_GET_SCHEDULE_DONE);
        responseIntent.putParcelableArrayListExtra("schedule", scheduleList);
        sendBroadcast(responseIntent);


    }

    private boolean login(String username, String password) throws IOException {

        RequestBody loginBody = new FormBody.Builder()
                .add("tipo", "3")
                .add("login", username)
                .add("clave", password)
                .add("x", String.valueOf(Math.floor(Math.random() * 400)))
                .build();

        Request loginRequest = new Request.Builder()
                .url(UlisesPath + "login-submit.do")
                .post(loginBody)
                .addHeader("Referer", UlisesPath + "login.do")
                .build();

        Response response = client.newCall(loginRequest).execute();
        if (!response.isSuccessful()) {
            throw new IOException("Unexpected response " + response);
        }

        return true;
    }

    private ArrayList<Schedule> getSchedule() throws Exception {

        ArrayList<Schedule> scheduleList = new ArrayList<>();

        Request scheduleRequest = new Request.Builder()
                .url(UlisesPath + "consultas/consultaHorarios.do")
                .addHeader("Referer", UlisesPath + "consultas/index.do")
                .get()
                .build();

        Response response = client.newCall(scheduleRequest).execute();
        if (!response.isSuccessful()) {
            throw new IOException("Unexpected response" + response);
        }

        Document doc = Jsoup.parse(response.body().byteStream(),
                null,
                response.header("Content-Location"));

        Elements courses = doc.select("table.texto:last-of-type > tbody > tr:not(:first-child)");

        for (Element elm: courses) {
            Schedule sched = new Schedule();
            sched.course = elm.children().get(0).text();
            sched.group = elm.children().get(1).text();
            sched.credits = elm.children().get(2).text();
            sched.classroom = elm.children().get(3).text();
            sched.time = elm.children().get(4).text();
            sched.day = elm.children().get(5).text();
            sched.location = elm.children().get(9).text();

            scheduleList.add(sched);
        }

        return scheduleList;

    }
}
