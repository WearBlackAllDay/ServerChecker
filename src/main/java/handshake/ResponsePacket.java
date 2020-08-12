package handshake;

import java.util.List;

public class ResponsePacket {

    public Description description;
    public Players players;
    public Version version;
    public String favicon;
    public long ping;
    public String rawJson;

    public static class Description {
        public String text;
    }

    public static class Players {
        public int online;
        public int max;
        public List<Player> sample;
    }

    public static class Player {
        public String name;
        public String id;
    }

    public static class Version {
        public String name;
        public int protocol;
    }

}
