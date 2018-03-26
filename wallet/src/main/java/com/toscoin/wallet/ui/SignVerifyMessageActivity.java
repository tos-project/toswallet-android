package com.toscoin.wallet.ui;

import android.os.Bundle;
import android.view.MenuItem;

import com.toscoin.wallet.R;

/**
 * @author John L. Jegutanis
 */
public class SignVerifyMessageActivity extends BaseWalletActivity {
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_wrapper);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(false);

        if (savedInstanceState == null) {
            SignVerifyMessageFragment fragment = new SignVerifyMessageFragment();
            fragment.setArguments(getIntent().getExtras());
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, fragment)
                    .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
