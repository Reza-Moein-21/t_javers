package org.example.javers;

import org.example.DBExtension;
import org.example.person.Person;
import org.javers.core.Changes;
import org.javers.core.Javers;
import org.javers.core.diff.Change;
import org.javers.repository.jql.JqlQuery;
import org.javers.repository.jql.QueryBuilder;
import org.javers.shadow.Shadow;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(DBExtension.class)
public class JaversTest {

    private final Javers javers;

    public JaversTest(Javers javers) {
        this.javers = javers;
    }


    @Test
    void delete() {
        javers.commitShallowDelete("ADMIN", Person.builder().id(1L).build());
    }

    @Test
    void javersTest() {

        // load person object
        final var f = Person.builder().id(1L).fullName("fullName").build();
        javers.commit("user1", f);

        final var newVersion = f.toBuilder().fullName("ALI").build();

        if (javers.compare(f, newVersion).hasChanges()) {
            final var nextVersion = f.version() + 1;
            final var f1 = newVersion.toBuilder().version(nextVersion).build();
            javers.commit("user2", f1);

        }

        final var q = QueryBuilder.byInstanceId(1L, Person.class).build();
        shadows(q);
//        snapshots(q);
        changes(q);


    }

    private void changes(JqlQuery q) {
        Changes changes = javers.findChanges(q);
        for (Change change : changes) {
            System.out.println(change.getAffectedGlobalId());
            System.out.println(change.getAffectedLocalId());
            System.out.println(change.getAffectedObject());
        }
    }

    private void snapshots(JqlQuery q) {
        final var snapshots = javers.findSnapshots(q);
        System.out.println("Last version: " + snapshots.getLast().getVersion());

    }

    private void shadows(JqlQuery q) {
        for (Shadow<Person> shadow : javers.<Person>findShadows(q)) {
            System.out.println(shadow.getCommitId());
            System.out.println(shadow.get());
        }
    }
}
