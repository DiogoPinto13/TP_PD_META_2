package isec.pd.meta2.Shared;

public enum ErrorMessages {
    INVALID_USERNAME{
        @Override
        public String toString(){return "invalid username";}
    },
    FAIL_REGISTER_PRESENCE_CODE{
        @Override
        public String toString(){return "failed register presence code";}
    },
    INVALID_EVENT_NAME{
        @Override
        public String toString(){return "Invalid event name";}
    },
    CREATE_EVENT_FAILED{
        @Override
        public String toString(){return "There was a problem creating an event";}
    },
    INVALID_USER{
        @Override
        public String toString(){return "This user is not valid";}
    },
    INVALID_PASSWORD{
        @Override
        public String toString() {return "The password is not valid";}
    },
    LOGIN_NORMAL_USER{
        @Override
        public String toString(){return "login normal user";}
    },
    LOGIN_ADMIN_USER{
        @Override
        public String toString(){return "login admin user";}
    },
    INVALID_REQUEST{
        @Override
        public String toString(){return "Invalid Request";}
    },
    USERNAME_ALREADY_EXISTS{
      @Override
      public String toString(){return "This username already exists";}
    },
    SQL_ERROR{
        @Override
        public String toString(){return "An error occurred with the Database";}
    };
    @Override
    public abstract String toString();
}
