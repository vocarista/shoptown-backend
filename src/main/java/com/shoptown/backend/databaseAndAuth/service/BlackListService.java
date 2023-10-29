package com.shoptown.backend.databaseAndAuth.service;

import com.shoptown.backend.databaseAndAuth.api.models.BlackListToken;
import com.shoptown.backend.databaseAndAuth.config.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BlackListService {
    private final MongoTemplate mongoTemplate;
    private final JwtService jwtService;

    @Autowired
    public BlackListService(MongoTemplate mongoTemplate, JwtService jwtService) {
        this.mongoTemplate = mongoTemplate;
        this.jwtService = jwtService;
    }

    public void addToBlackList(String token) {
        BlackListToken blackListToken = BlackListToken.builder().token(token).build();
        mongoTemplate.save(blackListToken);
    }

    public boolean isBlackListed(String token) {
        Query query = new Query(Criteria.where("token").is(token));
        return mongoTemplate.exists(query, BlackListToken.class);
    }

    @Scheduled(cron = "0 0 3 * * ?")
    public void cleanupBlackList() {
        List<BlackListToken> blackListTokens = mongoTemplate.findAll(BlackListToken.class);
        blackListTokens.forEach(mongoTemplate::remove);
        blackListTokens.removeIf(token -> jwtService.isTokenExpired(token.getToken()));
        mongoTemplate.insertAll(blackListTokens);
    }

}
