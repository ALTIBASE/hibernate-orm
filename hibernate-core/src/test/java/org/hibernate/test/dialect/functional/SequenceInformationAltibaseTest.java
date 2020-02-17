/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.test.dialect.functional;

import java.util.Map;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.cfg.AvailableSettings;
import org.hibernate.dialect.AltibaseDialect;
import org.hibernate.jpa.test.BaseEntityManagerFunctionalTestCase;

import org.hibernate.testing.RequiresDialect;
import org.junit.Test;

import static org.hibernate.testing.transaction.TransactionUtil.doInJPA;

/**
 * @author YounJung Park
 */
@RequiresDialect(AltibaseDialect.class)
public class SequenceInformationAltibaseTest extends
                                            BaseEntityManagerFunctionalTestCase {

    @Override
    protected Class<?>[] getAnnotatedClasses() {
        return new Class[] {
                Book.class
        };
    }

    @Override
    protected void addMappings(Map settings) {
        settings.put( AvailableSettings.HBM2DDL_AUTO, "none" );
    }

    @Test
    public void test() {
        doInJPA( this::entityManagerFactory, entityManager -> {
            Book book = new Book();
            book.setTitle("My Book");

            entityManager.persist(book);
        } );
    }

    @Entity
    @Table(name = "TBL_BOOK")
    public static class Book {
        @Id
        @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "book_sequence")
        @SequenceGenerator(
                name = "book_sequence",
                sequenceName = "book_seq",
                initialValue = 10,
                allocationSize = 10
        )
        @Column(name = "ID")

        private Integer id;

        @Column(name = "TITLE")
        private String title;

        public Book() {
        }

        public Book(String title) {
            this.title = title;

        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        @Override
        public String toString() {
            return "Book{" +
                   "id=" + id +
                   ", title='" + title + '\'' +
                   '}';
        }
    }
}
