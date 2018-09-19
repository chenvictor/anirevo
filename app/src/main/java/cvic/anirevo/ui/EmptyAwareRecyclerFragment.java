package cvic.anirevo.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cvic.anirevo.R;

public abstract class EmptyAwareRecyclerFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private TextView mTextView;

    private String emptyText;

    public EmptyAwareRecyclerFragment(String emptyText) {
        this.emptyText = emptyText;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recyclerview_emptyview, container, false);
        mRecyclerView = view.findViewById(R.id.recyclerview);
        mTextView = view.findViewById(R.id.emptyText);
        return view;
    }
}
