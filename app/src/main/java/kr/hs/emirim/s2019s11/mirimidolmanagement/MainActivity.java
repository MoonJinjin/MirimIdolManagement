package kr.hs.emirim.s2019s11.mirimidolmanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    MyDBHelper myHelper;
    EditText editName, editCount, editSelectName, editSelectCount;
    SQLiteDatabase sqlDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("아이돌 그룹 관리 DB");

        editName = findViewById(R.id.edit_name);
        editCount = findViewById(R.id.edit_count);
        editSelectName = findViewById(R.id.edit_select_name);
        editSelectCount = findViewById(R.id.edit_select_count);

        Button btnInit = findViewById(R.id.btn_init);
        Button btnInput = findViewById(R.id.btn_input);
        Button btnSelect = findViewById(R.id.btn_select);

        myHelper = new MyDBHelper(this);

        btnInit.setOnClickListener(btnListener);
        btnInput.setOnClickListener(btnListener);
        btnSelect.setOnClickListener(btnListener);
    }

    View.OnClickListener btnListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_init:
                    sqlDB = myHelper.getWritableDatabase(); // 읽기와 쓰기가 가능
                    myHelper.onUpgrade(sqlDB, 1, 2);
                    sqlDB.close();
                    break;
                case R.id.btn_input:
                    sqlDB = myHelper.getWritableDatabase();
                    sqlDB.execSQL("insert into groupTBL values('"+ editName.getText().toString() + "', " + editCount.getText().toString() + ");");
                    sqlDB.close();
                    Toast.makeText(getApplicationContext(), "저장 완료", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.btn_select:
                    sqlDB = myHelper.getReadableDatabase();
                    Cursor cursor = sqlDB.rawQuery("select * from groupTBL;", null);
                    String strNames = "그룹명\r\n----------\r\n";
                    String strCount = "인원수\r\n----------\r\n";
                    while (cursor.moveToNext()) {
                        strNames += cursor.getString(0) + "\r\n";
                        strCount += cursor.getString(1) + "\r\n";
                    }
                    editSelectName.setText(strNames);
                    editSelectCount.setText(strCount);
                    sqlDB.close();
                    cursor.close();
                    break;
            }
        }
    };
}