package net.ricerich.wanteat;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.Toast;

import net.ricerich.wanteat.JsonParser;
import net.ricerich.wanteat.MainActivity;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class NetworkJoin extends AsyncTask<String, Void, String> { /*execute는 DoInBackground이다.*/
    private URL url;
    Activity mAct;

    public NetworkJoin(MainActivity mAct) {
        this.mAct = mAct;
    }

    protected void onCreate() {
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... strings) { //이게 execute임
//        String URL_Address = "http://10.100.103.20/JessProject/Wanteat_Join.jsp";
        String URL_Address = "http://192.168.123.192/JessProject/Wanteat_Join.jsp";

        String res = "";

        try {
            url = new URL(URL_Address);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            //전송모드 설정
            conn.setDefaultUseCaches(false);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");

            //content-type 설정: 서버에게 웹에서 <Form>으로 값이 넘어온 것과 같은 방식으로 처리하라는 걸 알려준다
            //서버로 텍스트만 넘길 때는 이런 식으로 사용.
            conn.setRequestProperty("Content-type", "application/x-www-form-urlencoded; charset=utf-8");

            //전송값 설정
            StringBuffer buffer = new StringBuffer();
            buffer.append("id").append("=").append(strings[0]);
            buffer.append("&pwd").append("=").append(strings[1]);
            buffer.append("&name").append("=").append(strings[2]);
            buffer.append("&age").append("=").append(strings[3]);
            buffer.append("&phone").append("=").append(strings[4]);

            //서버로 전송
            OutputStreamWriter outStream = new OutputStreamWriter(conn.getOutputStream(), "utf-8");
            PrintWriter writer = new PrintWriter(outStream);
            writer.write(buffer.toString());
            writer.flush();

            StringBuilder builder = new StringBuilder();
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
            String line;
            while ((line = in.readLine()) != null) {
                builder.append(line + "\n");
            }

            res = builder.toString();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        int res = 0;
        try {
            res = JsonParser.getResultJson(s);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (res == 0) {
            Toast.makeText(mAct, "회원가입 실패", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(mAct, "회원가입 완료", Toast.LENGTH_SHORT).show();
        }
    }

}