package com.example.jakub.osobistytrenerbiegacza;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.TextView;


import com.example.jakub.osobistytrenerbiegacza.model.Training;
import com.example.jakub.osobistytrenerbiegacza.utils.HistoryArrayAdapter;

import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnFragmentInteractionListener}
 * interface.
 */
public class HistoryFragment extends Fragment implements AbsListView.OnItemClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_SECTION_NUMBER = "section_number";
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
//    private String mParam1;
//    private String mParam2;

    private OnFragmentInteractionListener mListener;

    /**
     * The fragment's ListView/GridView.
     */
    private AbsListView mListView;


    private HistoryArrayAdapter historyArrayAdapter;

    // TODO: Rename and change types of parameters
    public static HistoryFragment newInstance(int sectionNumber) {
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        HistoryFragment fragment = new HistoryFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public HistoryFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("Plan fragment on create");

        List<Training> trainingHistory = Singleton.getInstance().getUser().getHistory();
        historyArrayAdapter = new HistoryArrayAdapter(getActivity(), trainingHistory);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        mListView = (AbsListView) view.findViewById(R.id.fragment_history_list_lv_list);
        mListView.setAdapter(historyArrayAdapter);
        mListView.setOnItemClickListener(this);

        int visibility = Singleton.getInstance().getUser().getHistory().size() != 0 ? View.GONE : View.VISIBLE;
        TextView tvNoHistory = (TextView) view.findViewById(R.id.fragment_history_list_tv_no_history);
        tvNoHistory.setVisibility(visibility);

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
//        try {
//            mListener = (OnFragmentInteractionListener) activity;
//        } catch (ClassCastException e) {
//            throw new ClassCastException(activity.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        TextView textView = (TextView) view;
        createDialog(textView.getText().toString(),position);
        if (null != mListener) {
            // Notify the active callbacks interface (the activity, if the
            // fragment is attached to one) that an item has been selected.
            List<Training> trainingHistory = Singleton.getInstance().getUser().getHistory();
            mListener.onFragmentInteraction(String.valueOf(trainingHistory.get(position).getTrainingNumber()));//po chuj ta linia
        }
    }

    public void createDialog(String date, int position) {
        Log.i("menu", "createConfirmationDialog");
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//        Summary summary = new Summary();
        List<Training> trainingHistory = Singleton.getInstance().getUser().getHistory();
//        String text = "Lekki bieg 60 min.\nOdcinki 3x30s, przerwa 60s\nPrzebiegnięty dystans: 12km\nCałkowity czas: 80 min" +
//                "\nŚrednia prędkość: 5'23\"" +
//                "\nNotatki: Przyjemny bieg, pogoda dobra, lekkie podmuchy wiatru" +
//                "\nOcena: 4";
        Training t = trainingHistory.get(position);
        String text = t.getSummary().getTrainingSummary(t.getParts());
        builder.setMessage(text)
                .setTitle(date);

        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Log.i("menu", "Tak");
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }


    /**
     * The default content for this Fragment has a TextView that is shown when
     * the list is empty. If you would like to change the text, call this method
     * to supply the text it should use.
     */
    public void setEmptyText(CharSequence emptyText) {
        View emptyView = mListView.getEmptyView();

        if (emptyView instanceof TextView) {
            ((TextView) emptyView).setText(emptyText);
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(String id);
    }
}
