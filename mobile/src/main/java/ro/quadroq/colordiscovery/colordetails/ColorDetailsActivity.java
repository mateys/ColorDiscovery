package ro.quadroq.colordiscovery.colordetails;

import android.app.Activity;
import android.os.Bundle;

import ro.quadroq.colordiscovery.R;
import ro.quadroq.commonclasses.Constants;

/**
 * Created by mateisuica on 23/06/15.
 */
public class ColorDetailsActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.color_details_activity);
        if (findViewById(R.id.fragment_container) != null) {
            if (savedInstanceState != null) {
                return;
            }
            ColorDetailsFragment colorDetailsFragment = new ColorDetailsFragment();
            if(getIntent() != null) {
                int colorId = getIntent().getIntExtra(Constants.COLOR_ID, 0);
                Bundle args = new Bundle();
                args.putInt(Constants.COLOR_ID, colorId);
                colorDetailsFragment.setArguments(args);
            }
            getFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, colorDetailsFragment).commit();
        }

    }
}
