package isec.pd.meta2.Shared;

import java.io.Serializable;

public class Login implements Serializable {
    private String username, password;

    public Login(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }
    public String getPassword() {
        return password;
    }

}
