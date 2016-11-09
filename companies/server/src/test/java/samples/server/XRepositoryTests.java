package samples.server;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;

@RunWith(SpringRunner.class)
@DataJpaTest
public class XRepositoryTests {
    private final Date before = Date.from(LocalDateTime.now().plusDays(1).atZone(ZoneId.systemDefault()).toInstant());
    private final Date after = Date.from(LocalDateTime.now().minusDays(1).atZone(ZoneId.systemDefault()).toInstant());

    private final Company first = new Company(1);
    private final Company second = new Company(2).addKeywords(Arrays.stream(new String[] {"what"}));
    private final Company third = new Company(3).addKeywords(Arrays.stream(new String[] {"what", "where"}));

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private XRepository companies;

    @Before
    public void prepare() {
        entityManager.persist(first);
        entityManager.persist(second);
        entityManager.persist(third);
    }

    @After
    public void cleanup() {
        entityManager.clear();
    }

    @Test
    public void testFindBy() {
        assertThat(companies.findByKey(1), is(equalTo(first)));
        assertThat(companies.findByKey(2), is(equalTo(second)));
        assertThat(companies.findByKey(3), is(equalTo(third)));
    }

    @Test
    public void testFindByCreated() {
        assertThat(
                companies.findDistinctByCreatedBefore(before).stream().collect(Collectors.toSet()),
                is(equalTo(Stream.of(first, second, third).collect(Collectors.toSet()))));
        assertThat(
                companies.findDistinctByCreatedBefore(after).stream().collect(Collectors.toSet()),
                is(equalTo(Stream.of().collect(Collectors.toSet()))));
    }

    @Test
    public void testFindByCreatedAndKeywords() {
        assertThat(
                companies.findDistinctByCreatedAfterAndKeywords_ValueIn(after, Arrays.asList("where")).stream().collect(Collectors.toSet()),
                is(equalTo(Stream.of(third).collect(Collectors.toSet()))));
        assertThat(
                companies.findDistinctByCreatedAfterAndKeywords_ValueIn(after, Arrays.asList("what", "where")).stream().collect(Collectors.toSet()),
                is(equalTo(Stream.of(second, third).collect(Collectors.toSet()))));
    }

}
