package org.example.javers;

import org.example.DBExtension;
import org.example.event_field.Amount;
import org.example.inf.PageResult;
import org.example.letter_of_credit.IssueEvent;
import org.example.party.PartyType;
import org.javers.core.Javers;
import org.javers.core.metamodel.type.EntityType;
import org.javers.core.metamodel.type.PrimitiveType;
import org.javers.core.metamodel.type.ValueObjectType;
import org.javers.core.metamodel.type.ValueType;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;

@ExtendWith(DBExtension.class)
public class JaversDomainTest {

    private final Javers javers;

    public JaversDomainTest(Javers javers) {
        this.javers = javers;
    }

    /**
     * <b>Javers has 3 Main Types to categorize Domain Objects</b>
     * <ol>
     * <li><b>Entity</b>
     * <br>- Any Object that has identity filed which annotated with <code>@Id</code> considered as an Entity type
     * <br>- Comparing strategy for Entity state is property-by-property.
     * <br>- For Entity references, comparing is based on InstanceId.
     * </li>
     * <br>
     * <li><b>Value Object</b>
     * <br>- It’s a complex value holder with a list of mutable properties but without a unique identifier.
     * <br>- It’s similar to JPA Embeddable type or Java Record.
     * <br>- Comparing strategy for Value Objects is property-by-property.
     * <br>- in JaVers, Value Object is just an Entity without identity. unlike DDD that ValueObject depends on Entity
     * </li>
     * <br>
     * <li><b>Value</b>
     * <br>- Javers Value is a simple value holder.
     * <br>- A few examples of well-known Value types: <code>BigDecimal, LocalDate, String, URL</code>.
     * <br>- Comparing strategy for Values is (by default) based on Object.equals().
     * <br>- in JaVers, Value Object is just an Entity without identity. unlike DDD that ValueObject depends on Entity
     * </li>
     * <br>
     * </ol>
     */
    @Test
    void mappingTypeTest() {
        assertInstanceOf(EntityType.class, javers.getTypeMapping(IssueEvent.class));
        assertInstanceOf(ValueObjectType.class, javers.getTypeMapping(Amount.class));
        assertInstanceOf(ValueObjectType.class, javers.getTypeMapping(PageResult.class));
        assertInstanceOf(ValueType.class, javers.getTypeMapping(BigDecimal.class));
        assertInstanceOf(PrimitiveType.class, javers.getTypeMapping(PartyType.class));
    }


    /**
     * <b>There are 4 ways to map a class:</b>
     * <ol>
     *     <li><b>Explicitly</b>
     *      <br>using one of the JaversBuilder.register...() methods:
     *      <br>   registerEntity(Class entityClass, String idPropertyName),
     *      <br>   registerValueObject(Class valueObjectClass),
     *      <br>   registerValue(Class valueClass),
     *     </li>
     *     <br>
     *     <li><b>Explicitly with annotations</b>
     *      <br>using annotations, see class level annotations.
     *     </li>
     *     <br>
     *     <li><b>Implicitly</b>
     *      <br>using the type inferring algorithm based on the class inheritance hierarchy.
     *      <br>If a class is not mapped (by method 1 or 2), JaVers maps this class the in same way as its nearest supertype (superclass or interface)
     *     </li>
     *     <br>
     *     <li><b>Use the defaults</b>
     *      <br>By default, JaVers maps a class to Value Object.
     *     </li>
     * </ol>
     */
    @Test
    @Disabled
    void mappingConfiguration() {
    }

    /**
     * <b>Mapping ProTips:</b>
     * <ul>
     *     <li>First, try to map high level abstract classes. For example, if all of your Entities extend some abstract class, you can map only this class using <code>@Entity</code>.</li>
     *     <br>
     *     <li>Use <code>@TypeName</code> annotation for Entities, it gives you freedom of class names refactoring.</li>
     *     <br>
     *     <li>For an Entity, a type of its Id-property is mapped as Value by default.</li>
     *     <br>
     *     <li>If JaVers knows nothing about a class — defaulted mapping is Value Object.</li>
     *     <br>
     *     <li>JaVers compares objects deeply. It can cause performance problems for large object graphs. Use <code>@ShallowReference</code> and <code>@DiffIgnore</code> to ignoring things.</li>
     * </ul>
     */
    @Test
    @Disabled
    void mappingProTips() {
    }
}
