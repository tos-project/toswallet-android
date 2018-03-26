package com.toscoin.wallet.ui;


import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.toscoin.core.wallet.Wallet;
import com.toscoin.wallet.Constants;
import com.toscoin.wallet.R;
import com.toscoin.wallet.util.Fonts;
import com.toscoin.wallet.util.UiUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Generates and shows a new passphrase
 */
public class SeedFragment extends Fragment {
    private static final Logger log = LoggerFactory.getLogger(SeedFragment.class);

    private WelcomeFragment.Listener mListener;
    private boolean hasExtraEntropy = false;
    private TextView mnemonicView;

    public SeedFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_seed, container, false);

        TextView seedFontIcon = (TextView) view.findViewById(R.id.seed_icon);
        Fonts.setTypeface(seedFontIcon, Fonts.Font.COINOMI_FONT_ICONS);

        final CheckBox backedUpSeed = (CheckBox) view.findViewById(R.id.backed_up_seed);
        backedUpSeed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                buttonNext.setEnabled(isChecked);
            }
        });

        final Button buttonNext = (Button) view.findViewById(R.id.button_next);
        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                log.info("Clicked restore wallet");
                if (backedUpSeed.isChecked()) {
                    if (mListener != null) {
                        mListener.onSeedCreated(mnemonicView.getText().toString());
                    }
                } else {
                    Toast.makeText(getActivity(), R.string.backed_up_seed, Toast.LENGTH_SHORT).show();
                }
            }
        });
//        buttonNext.setEnabled(false);

        mnemonicView = (TextView) view.findViewById(R.id.seed);
        generateNewMnemonic();

        // Touch the seed icon to generate extra long seed
        seedFontIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hasExtraEntropy = !hasExtraEntropy; // toggle
                generateNewMnemonic();
                if (hasExtraEntropy) {
                    Toast.makeText(getActivity(), R.string.extra_entropy, Toast.LENGTH_SHORT).show();
                }
            }
        });


        View.OnClickListener generateNewSeedListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generateNewMnemonic();
            }
        };

        mnemonicView.setOnClickListener(generateNewSeedListener);
        view.findViewById(R.id.seed_regenerate_title).setOnClickListener(generateNewSeedListener);

        view.findViewById(R.id.button_copy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String seed = mnemonicView.getText().toString();
                ClipboardManager clipboard = (ClipboardManager) getActivity()
                        .getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("seed", seed);
                clipboard.setPrimaryClip(clip);
                Toast.makeText(getActivity(),
                        getString(R.string.action_copy) + "!!!",
                        Toast.LENGTH_SHORT)
                        .show();
                UiUtils.share(getActivity() ,seed );
            }
        });

        return view;
    }

    private void generateNewMnemonic() {
        log.info("Clicked generate a new mnemonic");
        String mnemonic;
        if (hasExtraEntropy) {
            mnemonic = Wallet.generateMnemonicString(Constants.SEED_ENTROPY_EXTRA);
        } else {
            mnemonic = Wallet.generateMnemonicString(Constants.SEED_ENTROPY_DEFAULT);
        }
        mnemonicView.setText(mnemonic);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (WelcomeFragment.Listener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement WelcomeFragment.OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
