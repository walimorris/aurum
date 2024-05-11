package com.morris.aurum.configurations;

import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.morris.aurum.models.Address;
import com.morris.aurum.models.Contact;
import com.morris.aurum.models.accounts.Account;
import com.morris.aurum.models.accounts.CheckingAccount;
import com.morris.aurum.models.accounts.SavingAccount;
import com.morris.aurum.models.cards.DebitCard;
import com.morris.aurum.models.clients.Client;
import com.morris.aurum.models.clients.CorporateClient;
import com.morris.aurum.models.clients.IndividualClient;
import com.morris.aurum.models.converters.DateToBsonDateTimeConverter;
import com.morris.aurum.models.transactions.DepositTransaction;
import com.morris.aurum.models.transactions.Transaction;
import com.morris.aurum.models.transactions.WithdrawTransaction;
import org.bson.codecs.BsonDateTimeCodec;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;

import java.util.Arrays;
import java.util.List;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

@Configuration
@PropertySource("classpath:secrets.properties")
public class SpringMongoConfig {

    @Value("${secrets.mongodbUri}")
    private String mongodbUri;

    @Bean
    public MongoClient mongoClient() {
        return MongoClients.create(mongodbUri);
    }

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
        return fromRegistries(
                MongoClientSettings.getDefaultCodecRegistry(),
                fromProviders(
                        PojoCodecProvider.builder()
                                .register(Client.class, CorporateClient.class, IndividualClient.class,
                                        Account.class, Address.class, Contact.class, CheckingAccount.class,
                                        SavingAccount.class, DebitCard.class, Transaction.class,
                                        DepositTransaction.class, WithdrawTransaction.class,
                                        BsonDateTimeCodec.class).build()
                )
        );
    }

    /**
     * Needs to be registered to enable native MDB transactions which are disabled by default.
     * @see <a href="https://docs.spring.io/spring-data/mongodb/reference/mongodb/client-session-transactions.html">Spring Transactions Docs</a>
     *
     * @param databaseFactory {@link MongoDatabaseFactory}
     * @return {@link MongoTransactionManager}
     */
    @Bean
    MongoTransactionManager transactionManager(MongoDatabaseFactory databaseFactory) {
        return new MongoTransactionManager(databaseFactory);
    }

    @Bean
    public MongoCustomConversions MongoCustomConversions() {
        return new MongoCustomConversions(List.of(new DateToBsonDateTimeConverter()));
    }
}
