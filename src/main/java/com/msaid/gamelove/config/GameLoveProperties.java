package com.msaid.gamelove.config;

import com.msaid.gamelove.persistence.entity.UserRole;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
@ConfigurationProperties(prefix = "game-love")
@Getter
@Setter
public class GameLoveProperties {
    private DBThreadPoolProperties dbProperties = new DBThreadPoolProperties();
    private String jwtSecret;
    private long jwtExpireMs = 3600000;
    private List<UserProperties> users;
    private List<GameProperties> games;
    private int pageSize = 15;
    private boolean securityEnabled = false;



    @Getter
    @Setter
    public static class DBThreadPoolProperties {
        private int corePoolSize = 16;
        private int maxPoolSize = 32;
        private String threadNamePrefix = "dbThread";
        private int queCapacity = 500;
    }

    @Getter
    @Setter
    public static class UserProperties {
        private String username;
        private String password;
        private Set<UserRole> roles;
    }
    @Getter
    @Setter
    public static class GameProperties {
        private String name;
    }

}
