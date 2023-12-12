package isec.pd.meta2.Shared;

import java.io.Serializable;
import java.util.ArrayList;

public class EventResult implements Serializable {
    private String columns;
    public ArrayList<String> events;

    public EventResult(String columns) {
        this.columns = columns;
        events = new ArrayList<String>();
    }

    public String getColumns() {
        return columns;
    }

    public void setColumns(String columns) {
        this.columns = columns;
    }
}
