package ro.quadroq.sexdice;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.Wearable;

import ro.quadroq.commonclasses.Utils;
import ro.quadroq.sexdice.coloradd.AddColorActivity;


public class MainActivity extends ActionBarActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {

    // private Cursor cursor;
    private ColorsCursorAdapter adapter;
    private RecyclerView colorList;
    private CardView noColorsSign;

    GoogleApiClient mGoogleApiClient;
    private static final String TAG = "MobileMainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        colorList = (RecyclerView) findViewById(R.id.colorList);
        noColorsSign = (CardView) findViewById(R.id.no_color_sign);
        FloatingActionButton button = (FloatingActionButton) findViewById(R.id.addButton);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddColorActivity.class);
                startActivity(intent);
            }
        });

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(Bundle connectionHint) {
                        Log.d(TAG, "onConnected: " + connectionHint);
                        // Now you can use the Data Layer API
                    }
                    @Override
                    public void onConnectionSuspended(int cause) {
                        Log.d(TAG, "onConnectionSuspended: " + cause);
                    }
                })
                .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(ConnectionResult result) {
                        Log.d(TAG, "onConnectionFailed: " + result);
                    }
                })
                        // Request access only to the Wearable API
                .addApi(Wearable.API)
                .build();
        colorList.setLayoutManager(new LinearLayoutManager(this));
        colorList.addOnItemTouchListener(new ColorListOnItemTouchListener(this, new ColorListOnItemTouchListener.OnItemClickListener() {

            @Override
            public void onItemClick(View childView, int position) {
                Cursor c = adapter.getCursor();
                c.moveToPosition(position);
                        int color = c.getInt(c.getColumnIndex(ColorItem.COLUMN_COLOR));

                        Intent intent = new Intent(android.content.Intent.ACTION_SEND);
                        intent.setType("text/plain");

                        // Add data to the intent, the receiving app will decide what to do with it.
                        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.share_subject));
                        intent.putExtra(Intent.EXTRA_TEXT, "I've discovered this new cool color using " + getString(R.string.app_name) + "! The color code is: " + Utils.getColorString(color));
                        startActivity(Intent.createChooser(intent, getString(R.string.share_dialog_title)));
            }

            @Override
            public void onItemLongPress(View childView, int position) {
                Cursor c = adapter.getCursor();
                c.moveToPosition(position);
                int colorId = c.getInt(c.getColumnIndex(ColorItem.COLUMN_ID));
                final int colorColor = c.getInt(c.getColumnIndex(ColorItem.COLUMN_COLOR));
                Snackbar.make(findViewById(R.id.snackbarPosition), R.string.color_removed, Snackbar.LENGTH_LONG)
                        .setAction(R.string.undo, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ContentValues contentValues = new ContentValues();
                                contentValues.put(ColorItem.COLUMN_COLOR, colorColor);
                                getContentResolver().insert(ColorContentProvider.CONTENT_URI, contentValues);
                            }
                        })
                        .show();

                getContentResolver().delete(ColorContentProvider.CONTENT_URI, ColorItem.COLUMN_ID + "=?", new String[]{Integer.toString(colorId)});
            }
        }));
        adapter = new ColorsCursorAdapter(null);
        colorList.setAdapter(adapter);

        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this,
                ColorContentProvider.CONTENT_URI, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if(data.getCount() > 0) {
            colorList.setVisibility(View.VISIBLE);
            noColorsSign.setVisibility(View.GONE);
        } else {
            colorList.setVisibility(View.GONE);
            noColorsSign.setVisibility(View.VISIBLE);
        }
        adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }

}
