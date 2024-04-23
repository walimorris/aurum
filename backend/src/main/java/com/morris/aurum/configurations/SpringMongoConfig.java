package com.morris.aurum.configurations;

import com.mongodb.MongoClientSettings;
import com.morris.aurum.models.clients.Client;
import com.morris.aurum.models.clients.CorporateClient;
import com.morris.aurum.models.clients.HumanClient;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.codecs.pojo.PropertyCodecProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

@Configuration
public class SpringMongoConfig {

    /**
     * By default, MongoDB saves documents with a _class field in the document. This bean
     * alters the MongoConverter and removes this default feature.
     *
     * @param databaseFactory {@link MongoDatabaseFactory}
     * @param converter {@link MappingMongoConverter}
     *
     * @return {@link MongoTemplate}
     */
    @Bean
    public MongoTemplate mongoTemplate(MongoDatabaseFactory databaseFactory, MappingMongoConverter converter) {
        converter.setTypeMapper(new DefaultMongoTypeMapper(null));
        return new MongoTemplate(databaseFactory, converter);
    }

    /**
     * Used to register custom classes in CodeRegistry.
     *
     *
     * @return {@link CodecRegistry}
     */
    @Bean
    public CodecRegistry codecRegistry() {
        List<Class<?>> classList = List.of(
                Client.class, CorporateClient.class, HumanClient.class
        );

        return fromRegistries(
                MongoClientSettings.getDefaultCodecRegistry(),
                fromProviders(
                        PojoCodecProvider.builder()
                                .register((PropertyCodecProvider) classList)
                                .build()
                )
        );
    }
}
