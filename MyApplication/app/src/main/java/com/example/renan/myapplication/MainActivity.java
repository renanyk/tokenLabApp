package com.example.renan.myapplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ProgressDialog pd;
    private ArrayList<Game> gameList;
    private ListView gameListView;
    String txtJson;
    Context context;
    private GameAdapter gameAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gameListView = (ListView) findViewById(R.id.listView);
        context=this;
        gameList = new ArrayList<>();
        new JsonTask().execute("https://dl.dropboxusercontent.com/s/1b7jlwii7jfvuh0/games");

        gameListView.setAdapter(gameAdapter= new GameAdapter(this,R.layout.cell_game,gameList));

    }

    private class JsonTask extends AsyncTask<String, String, String> {

        protected void onPreExecute() {
            super.onPreExecute();

            pd = new ProgressDialog(MainActivity.this);
            pd.setMessage("Please wait");
            pd.setCancelable(false);
            pd.show();
        }

        protected String doInBackground(String... params) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();


                InputStream stream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();
                String line = "";

                while ((line = reader.readLine()) != null) {
                    buffer.append(line+"\n");
                    Log.d("Response: ", "> " + line);   //here u ll get whole response...... :-)

                }

                return buffer.toString();


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (pd.isShowing()){
                pd.dismiss();
            }
            txtJson=result;
            if(result!=null){
                jsonParser(txtJson);

            }
        }
    }

    public void jsonParser(String date) {

        try {

            JSONObject jobj = new JSONObject(date);
            JSONArray jArrGame = jobj.getJSONArray("games");
            int id;
            Game game;
            String name;
            String image;
            String release_date;
            String trailer;
            ArrayList<String> platforms = new ArrayList<>();
            JSONArray jArrPlatforms;
            for(int i=0;i<jArrGame.length();i++){

                JSONObject obj = jArrGame.getJSONObject(i);
                id = Integer.parseInt(obj.getString("id"));
                name = obj.getString("name");

                image = obj.getString("image");
                release_date = obj.getString("release_date");
                trailer = obj.getString("trailer");
                jArrPlatforms = obj.getJSONArray("platforms");
                for(int j=0;j<jArrPlatforms.length();j++){
                    platforms.add(jArrGame.getJSONObject(i).toString());
                }


                game = new Game(id,name,image,release_date,trailer,platforms);
                new ImageLoadTask(image,game).execute();
                gameList.add(game);
                Log.d("aaaaaaaaaaaaaa",Integer.toString(gameList.size()));
            }



            //gameAdapter.addAll(gameList);

            gameAdapter.notifyDataSetChanged();
        }catch(JSONException ex){
            ex.printStackTrace();

        }




    }
    public class GameAdapter extends ArrayAdapter<Game> {


        public GameAdapter(Context context, int textViewResourceId, ArrayList<Game> mygameList) {
            super(context, textViewResourceId, mygameList);
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewGroup cell = (ViewGroup) convertView;
            if(cell==null)
                cell = (ViewGroup) getLayoutInflater().inflate(R.layout.cell_game,null);

            Log.d("aaaaaa",Integer.toString(position));
            TextView name = (TextView) cell.findViewById(R.id.game_name);
            TextView game_release_date = (TextView) cell.findViewById(R.id.game_release_date);
            TextView platforms = (TextView) cell.findViewById(R.id.game_platforms);
            ImageView imageView = (ImageView) cell.findViewById(R.id.game_imageView);
            imageView.setImageBitmap(gameList.get(position).getImage());
            name.setText(gameList.get(position).getName());
            game_release_date.setText(gameList.get(position).getRelease_date());
            platforms.setText("teste");

            return cell;

        }
    }

    public class ImageLoadTask extends AsyncTask<Void, Void, Bitmap> {

        private String url;
        private Game game;

        public ImageLoadTask(String url, Game game) {
            this.url = url;
            this.game = game;
        }

        @Override
        protected Bitmap doInBackground(Void... params) {
            try {
                URL urlConnection = new URL(url);
                HttpURLConnection connection = (HttpURLConnection) urlConnection
                        .openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                return myBitmap;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            game.setImage(result);
            gameAdapter.notifyDataSetChanged();
        }

    }
}
