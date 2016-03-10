package com.example.lorenzo.donatethedistance;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class CharitySelectionView extends AppCompatActivity {
    public final static String SELECTED_CHARITY = "selected charity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charity_selection_view);

        TableLayout yourRootLayout = (TableLayout) findViewById(R.id.table);
        int count = yourRootLayout.getChildCount();
        for (int i = 0; i < count; i++) {
            View v = yourRootLayout.getChildAt(i);
            if (v instanceof TableRow) {
                final TableRow row = (TableRow) v;
                row.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int rowCount = row.getChildCount();
                        for (int r = 0; r < rowCount; r++) {
                            View v2 = row.getChildAt(r);
                            if (v2 instanceof TextView) {
                                Intent intent = new Intent(CharitySelectionView.this, ActivitySelectionView.class);
                                intent.putExtra(SELECTED_CHARITY, (String) ((TextView) v2).getText());
                                startActivity(intent);
                            }
                        }
                    }
                });
            }
        }
    }
}
