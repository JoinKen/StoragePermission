package it1006.learn.storageandroid;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    public static final int MY_PERMISSION_REQUEST = 123; //dont care

    ArrayList<String> arrayList; //Danh sách bài hát

    ListView listView; //Hiển thị danh sách

    ArrayAdapter<String> adapter; //adapter đổ vào danh sách listView

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //CODE xin quyền cho tất cả API
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                Log.d("it10069", "Permissinoningg2");
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSION_REQUEST);
            } else {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSION_REQUEST);
                Log.d("it10069", "Permissinoningg3");
            }
        } else {
            doStuff();//Nếu xin quyền ở đây trực tiếp được thì thực hiện luôn(hiếm gặp)
        }
    }

    private void loadAudio() {
        ContentResolver contentResolver = getContentResolver(); //This class provides applications access to the content model.

        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI; //Khai báo Uri( Ctrl+Nhấp vào Class Uri để hiểu thêm

        //Coment là lấy trong Mô tả của class Cursor. hehe
        // This interface provides random read-write access to the result set returned by a database query.
        Cursor cursor = contentResolver.query(uri, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            arrayList = new ArrayList<>();
            do {
                String data = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                String title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                arrayList.add(data + "\n" + title + "\n" + artist);
            } while (cursor.moveToNext()); //Dò cho đến khi hết
        }
        cursor.close();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            //Ở đây mình sẽ tiến hành xin quyền lại bằng một  thông báo hiển thị lên cho người dùng,
            //Nếu chọn đồng ý thì được quyền, nếu không thì finish() kết thúc chương trình
            case MY_PERMISSION_REQUEST: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(MainActivity.this,
                            Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        doStuff();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Permission Denied!", Toast.LENGTH_SHORT).show();
                    finish();
                }
                return;
            }
        }
    }

    /**
     * Hàm Load và đổ dữ liệu lên view chính activity_main.xml
     */
    public void doStuff() {
        listView = (ListView) findViewById(R.id.listView);
        arrayList = new ArrayList<>();

        loadAudio();

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrayList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Sự kiện khi nhấn vào bất kỳ cái item nào;
                //Có thể lấy position để biết bài nào được nhấn: The position of the view in the adapter.
                //id The row id of the item that was clicked
            }
        });
    }
}
