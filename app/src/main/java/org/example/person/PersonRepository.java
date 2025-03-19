package org.example.person;

import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;
import java.util.Optional;

@Mapper
interface PersonRepository {

    @Select("select p.* from person p where p.id = #{id} ")
    Optional<Person> findPersonById(Long id);

    @Select("""
            select p.* from person p
            
            OFFSET #{offset} ROWS
            FETCH NEXT #{limit} ROWS ONLY
            """)
    List<Person> findAll(@Param("offset") int offset, @Param("limit") int limit);

    @Select("select count(*) from person")
    long totalItems();


    @SelectProvider(type = PersonSelectProvider.class, method = "selectPerson")
    List<Person> searchPersonBySearchParam(PersonSearchParam param, int offset, int limit);

    @SelectProvider(type = PersonSelectProvider.class, method = "selectPersonTotal")
    long totalPersonBySearchParam(PersonSearchParam param);


    @Insert("insert into person(ID,FULL_NAME,VERSION) values (#{id},#{fullName},#{version})")
    int insert(Person p);

    @Update("update person set FULL_NAME=#{fullName}, VERSION=#{version} where ID=#{id}")
    int update(Person p);

    class PersonSelectProvider {

        public String selectPerson(PersonSearchParam param, int offset, int limit) {
            return applySearchQuery(param, new SQL().SELECT("*"))
                    .toString()
                    .concat("""
                            
                            OFFSET %s ROWS
                            FETCH NEXT %s ROWS ONLY
                            """.formatted(offset, limit));
        }

        public String selectPersonTotal(PersonSearchParam param) {
            return applySearchQuery(param, new SQL().SELECT("count(ID)")).toString();
        }

        private SQL applySearchQuery(PersonSearchParam param, SQL baseSql) {
            final var sql = baseSql
                    .FROM("PERSON");

            if (param.fullName() != null) {
                sql.WHERE("full_name like '%" + param.fullName().trim() + "%' ");
            }

            return sql;
        }
    }
}
