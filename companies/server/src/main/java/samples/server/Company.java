package samples.server;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Entity
@EntityListeners(AuditingEntityListener.class)
class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Integer key;
    @Temporal(TemporalType.DATE)
    @CreatedDate
    private Date created;
    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    final private Set<Keyword> keywords = new HashSet<>();

    private Company() {
    }

    Company(Integer key) {
        this.key = key;
    }

    Company addKeywords(Stream<String> keywords) {
        this.keywords.addAll(keywords.map(Keyword::new).collect(Collectors.toSet()));
        return this;
    }

    Integer getKey() {
        return key;
    }
}
