package com.jacksonisaac.modernartui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.SeekBar;

public class MainActivity extends Activity {

    private RelativeLayout mHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mHolder = (RelativeLayout) findViewById(R.id.palette);
        SeekBar seekBar = (SeekBar) findViewById(R.id.seekBar);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                int baseColor;
                int nextColor;

                for (int i = 0; i < mHolder.getChildCount(); i++) {
                    View child = mHolder.getChildAt(i);

                    baseColor = Color.parseColor((String)child.getTag());
                    nextColor = (0x00FFFFFF - (baseColor | 0xFF000000)) | (baseColor & 0xFF000000);

                    if (ContextCompat.getColor(getApplicationContext(), R.color.white) != baseColor &&
                         ContextCompat.getColor(getApplicationContext(), R.color.gray) != baseColor) {

                        int origR = (baseColor >> 16) & 0x000000FF;
                        int origG = (baseColor >> 8) & 0x000000FF;
                        int origB = baseColor & 0x000000FF;

                        int invR = ( nextColor >> 16 ) & 0x000000FF;
                        int invG = ( nextColor >> 8 ) & 0x000000FF;
                        int invB = nextColor & 0x000000FF;

                        child.setBackgroundColor(Color.rgb(
                                                  (int)(origR + ( invR - origR ) * (progress / 50f)),
                                                  (int)(origG + ( invG - origG ) * (progress / 100f)),
                                                  (int)(origB + ( invB - origB ) * (progress / 150f))));
                        child.invalidate();
                    }
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch ( item.getItemId() ) {
            case R.id.menu_help:
                showDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.dialog_title)
                .setMessage(R.string.dialog_message)
                .setPositiveButton(R.string.dialog_moma, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.moma.org/"));
                        startActivity(intent);
                    }
                })
                .setNegativeButton(R.string.dialog_not_now, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
