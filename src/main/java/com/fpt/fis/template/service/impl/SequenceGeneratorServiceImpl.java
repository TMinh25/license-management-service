package com.fpt.fis.template.service.impl;

import com.fpt.fis.template.repository.entity.DatabaseSequence;
import com.fpt.fis.template.service.SequenceGeneratorService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import static org.springframework.data.mongodb.core.FindAndModifyOptions.options;

@Service
@Log4j2
public class SequenceGeneratorServiceImpl implements SequenceGeneratorService {

    private final ReactiveMongoOperations reactiveMongoOperations;

    @Autowired
    public SequenceGeneratorServiceImpl(ReactiveMongoOperations reactiveMongoOperations) {
        this.reactiveMongoOperations = reactiveMongoOperations;
    }

    public Mono<Long> generateSequence(String seqName) {
        return reactiveMongoOperations.findAndModify(
                        Query.query(Criteria.where("_id").is(seqName)),
                        new Update().inc("seq", 1),
                        options().returnNew(true).upsert(true),
                        DatabaseSequence.class)
                .map(DatabaseSequence::getSeq)
                .defaultIfEmpty(1L);
    }
}
