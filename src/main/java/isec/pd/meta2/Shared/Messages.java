package isec.pd.meta2.Shared;

public enum Messages {
    UPDATE{
        @Override
        public String toString(){return "Update data";}
    },
    OK{
        @Override
        public String toString(){return "code 201";}
    },
    CREATE_EVENT{
        @Override
        public String toString(){return "Request create event";}
    },
    DELETE_EVENT{
        @Override
        public String toString(){return "Request delete event";}
    },
    GET_EVENTS{
        @Override
        public String toString(){return "Request get events";}
    },
    GENERATE_PRESENCE_CODE{
        @Override
        public String toString(){return "Request generate presence code";}
    },
    UPDATE_PRESENCE_CODE{
        @Override
        public String toString(){return "Request update presence code";}
    },
    QUERY_EVENTS{
        @Override
        public String toString(){return "Request query events";}
    },
    DELETE_PRESENCES{
        @Override
        public String toString(){return "Request delete presences";}
    },
    INSERT_PRESENCES{
        @Override
        public String toString(){return "Request insert presences";}
    },
    REQUEST_EDIT_PROFILE{
        @Override
        public String toString(){return "Request edit profile";}
    },
    EDIT_PROFILE_SUCCESS{
        @Override
        public String toString(){return "Successfully changed!";}
    },
    EDIT_PROFILE_ERROR{
        @Override
        public String toString(){return "an error occurred";}
    },
    PRESENCE_CODE_REGISTED{
        @Override
        public String toString(){return "the presence code was registed";}
    },
    INVALID_PRESENCE_CODE{
      @Override
      public String toString(){return "the presence code is not valid!";}
    },
    REGISTER_PRESENCE_CODE{
        @Override
        public String toString(){return "Register presence code option";};
    },
    EDIT_PROFILE{
        @Override
        public String toString(){return "edit profile option";}
    },
    GET_PRESENCES{
        @Override
        public String toString(){return "get presences option";}
    },
    GET_CSV_PRESENCES{
        @Override
        public String toString(){return "get csv with presences option";}
    },
    CLOSE{
        @Override
        public String toString(){return "Close";}
    },
    UNKNOWN_COMMAND {
        @Override
        public String toString(){return "Unknown command.";}
    },
    CHECK_PRESENCES {
        @Override
        public String toString(){return "Check if an event have presences registered.";}
    },
    GET_INFO_EVENT{
        @Override
        public String toString(){return "Request get info event.";}
    },
    GET_PRESENCES_EVENT{
        @Override
        public String toString(){return "Request get presences event.";}
    },
    EDIT_EVENT{
        @Override
        public String toString(){return "Request edit event.";}
    }, GET_PRESENCES_FILTER{
        @Override
        public String toString(){return "Request get presences with filters.";}
    };
    @Override
    public abstract String toString();
}
