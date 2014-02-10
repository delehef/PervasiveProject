package pervasive.delehellelassenberger.phone;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


public class SettingsFragment extends Fragment {
    private View view;
    private boolean isEnabled = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.settings_fragment, container, false);

        ((Button)(view.findViewById(R.id.toggle_enable_button))).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Integer r = new TcpWorker().execute((byte)(isEnabled?5:4)).get(1, TimeUnit.SECONDS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    ((Button)v).setText("Error");
                } catch (TimeoutException e) {
                    ((Button)v).setText("Timeout");
                }
                updateEnableDisable();
            }
        });

        return view;
    }

    public void onResume() {
        super.onResume();
        updateEnableDisable();
    }

    public void updateEnableDisable() {
        Button b = (Button) view.findViewById(R.id.toggle_enable_button);
        Integer status = 0;
        try {
            status = new TcpWorker().execute((byte)6).get(1, TimeUnit.SECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        b.setText(status==1?"Disable":"Enable");
        isEnabled = (status==1);
    }

    public void toggleEnabled() {

    }
}
