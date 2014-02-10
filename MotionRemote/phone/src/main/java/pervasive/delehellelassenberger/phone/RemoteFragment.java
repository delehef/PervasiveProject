package pervasive.delehellelassenberger.phone;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class RemoteFragment extends Fragment {
    final byte VOLUME_UP = 0;
    final byte VOLUME_DOWN = 1;
    final byte CHAN_UP = 2;
    final byte CHAN_DOWN = 3;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_remote, container, false);
        return rootView;
    }

    public void volUp(View v) {
        new TcpWorker().execute(VOLUME_UP);
    }

    public void volDown(View v) {
        new TcpWorker().execute(VOLUME_DOWN);
    }

    public void chanUp(View v) {
        new TcpWorker().execute(CHAN_UP);
    }

    public void chanDown(View v) {
        new TcpWorker().execute(CHAN_DOWN);
    }
}
