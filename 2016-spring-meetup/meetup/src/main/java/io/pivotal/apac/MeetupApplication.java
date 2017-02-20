package io.pivotal.apac;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Component;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by sergiu on 8/12/16.
 */
@SpringBootApplication
public class MeetupApplication {

    public static void main(String[] args) {
        SpringApplication.run(MeetupApplication.class, args);
    }

}

@Entity @Data @NoArgsConstructor
class Talk {

    @Id
    @GeneratedValue
    Long id;
    String title;
    String presenter;
    String role;
    String company;
}

@RestResource
// Standard JPA staff except this annotation. It saves you a lot of effort to create RESTful API. Thanks Spring Data Rest!
interface TalkRepository extends JpaRepository<Talk, Long> {
    // if you are familiar with JPA, feel free to add more method here, for example findAllByName.
    // it will be appeared in http://your-consumer.cfapps.io/contacts/search
}

@Component
class DataInitializer implements InitializingBean {

    private static Logger LOG = LoggerFactory.getLogger(DataInitializer.class);

    @Autowired
    private TalkRepository talkRepository;

    @Override
    public void afterPropertiesSet() {
        LOG.info("Verifying if data needs to be created");
        if(talkRepository.count() > 0) {
            LOG.info("Sample Data already loaded.");
        } else {

            Talk talk = new Talk();
            talk.setId(5000L);
            talk.setTitle("Welcome Greeting");
            talk.setPresenter("Spring SG");
            talk.setCompany("Meetup");
            talk.setRole("Networking");
            talkRepository.save(talk);

            Talk talk1 = new Talk();
            talk1.setId(5001L);
            talk1.setTitle("From Idea to Production in 20min");
            talk1.setPresenter("Sergiu Bodiu");
            talk1.setCompany("Pivotal");
            talk1.setRole("Platform Architect");
            talkRepository.save(talk1);

            Talk talk2 = new Talk();
            talk2.setId(5002L);
            talk2.setTitle("Leveraging machine Learning with Apache Spark");
            talk2.setPresenter("Anup Kumar");
            talk2.setCompany("IBM");
            talk2.setRole("Executive Data Architect");
            talkRepository.save(talk2);

            Talk talk3 = new Talk();
            talk3.setId(5003L);
            talk3.setTitle("Data Microservices");
            talk3.setPresenter("Vish Phaneendra");
            talk3.setCompany("Pivotal");
            talk3.setRole("Sr. Field Eng. Manager");
            talkRepository.save(talk3);

            Talk talk4 = new Talk();
            talk4.setId(5004L);
            talk4.setTitle("DevOps Analytics");
            talk4.setPresenter("Flavio Monigatti");
            talk4.setCompany("Credit Suisse");
            talk4.setRole("DevOps Head");
            talkRepository.save(talk4);

            Talk talk5 = new Talk();
            talk5.setId(5004L);
            talk5.setTitle("Security at DevOps Speed");
            talk5.setPresenter("Sven Schleier");
            talk5.setCompany("Vantage Point Security");
            talk5.setRole("Senior Security Consultant");
            talkRepository.save(talk5);
            LOG.info("Sample Data loaded...  6 talks");
        }
    }

}

