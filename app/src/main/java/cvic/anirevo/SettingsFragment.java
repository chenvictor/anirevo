package cvic.anirevo;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.util.Log;
import android.widget.Toast;

import cvic.anirevo.model.anirevo.AgeRestriction;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SettingsFragment.SettingsFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class SettingsFragment extends PreferenceFragmentCompat {

    private SettingsFragmentInteractionListener mListener;

    @Override
    public void onCreatePreferences(Bundle bundle, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);
        PreferenceManager.setDefaultValues(getActivity(), R.xml.preferences, false);

        Preference.OnPreferenceChangeListener preferenceChangeListener = new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                showToastMessage("Hit 'Apply' to apply changes.");
                return true;
            }
        };
        findPreference(getString(R.string.display_13_key)).setOnPreferenceChangeListener(preferenceChangeListener);
        findPreference(getString(R.string.display_18_key)).setOnPreferenceChangeListener(preferenceChangeListener);

        //Set button
        Preference button = findPreference(getString(R.string.apply_age_restriction_key));
        button.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                SharedPreferences prefs = getPreferenceManager().getSharedPreferences();
                AgeRestriction restriction = null;

                if (prefs.getBoolean(getString(R.string.display_13_key), true)) {
                    restriction = AgeRestriction.AGE_RESTRICTION_13;
                    if (prefs.getBoolean(getString(R.string.display_18_key), false)) {
                        restriction = AgeRestriction.AGE_RESTRICTION_18;
                    }
                }
                if (mListener != null) {
                    showToastMessage("Applying changes...");
                    mListener.reloadJSON(restriction);
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof SettingsFragmentInteractionListener) {
            mListener = (SettingsFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private Toast preferenceToast = null;

    private void showToastMessage(String message) {
        if (preferenceToast != null) {
            preferenceToast.cancel();
        }
        preferenceToast = Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT);
        preferenceToast.show();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface SettingsFragmentInteractionListener {
        // TODO: Update argument type and name
        void reloadJSON (AgeRestriction restriction);
    }

}
