/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.test.joinedsubclass;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

import org.hibernate.Session;
import org.hibernate.dialect.AltibaseDialect;
import org.hibernate.persister.entity.EntityPersister;
import org.hibernate.persister.entity.JoinedSubclassEntityPersister;
import org.hibernate.persister.entity.Loadable;

import org.hibernate.testing.SkipForDialect;
import org.hibernate.testing.TestForIssue;
import org.hibernate.testing.junit4.BaseCoreFunctionalTestCase;
import org.hibernate.testing.junit4.ExtraAssertions;
import org.junit.Test;

import static org.hibernate.testing.junit4.ExtraAssertions.assertTyping;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author Steve Ebersole
 */
@TestForIssue( jiraKey = "HHH-6911" )
public class JoinedSubclassWithExplicitDiscriminatorTest extends BaseCoreFunctionalTestCase {
	@Entity( name = "Animal" )
	@Table( name = "animal" )
	@Inheritance( strategy = InheritanceType.JOINED )
	@DiscriminatorColumn( name = "type", discriminatorType = DiscriminatorType.STRING )
	@DiscriminatorValue( value = "???animal???" )
	public static abstract class Animal {
		@Id
		public Integer id;

		protected Animal() {
		}

		protected Animal(Integer id) {
			this.id = id;
		}
	}

	@Entity( name = "Cat" )
	@DiscriminatorValue( value = "cat" )
	public static class Cat extends Animal {
		public Cat() {
			super();
		}

		public Cat(Integer id) {
			super( id );
		}
	}

	@Entity( name = "Dog" )
	@DiscriminatorValue( value = "dog" )
	public static class Dog extends Animal {
		public Dog() {
			super();
		}

		public Dog(Integer id) {
			super( id );
		}
	}

	@Override
	protected Class<?>[] getAnnotatedClasses() {
		return new Class[] { Animal.class, Cat.class, Dog.class };
	}

	@Test
	@SkipForDialect( value = AltibaseDialect.class, comment = "'TYPE' is not escaped even though autoQuoteKeywords is enabled")
	public void metadataAssertions() {
		EntityPersister p = sessionFactory().getEntityPersister( Dog.class.getName() );
		assertNotNull( p );
		final JoinedSubclassEntityPersister dogPersister = assertTyping( JoinedSubclassEntityPersister.class, p );
		assertEquals( "string", dogPersister.getDiscriminatorType().getName() );
		assertEquals( "type", dogPersister.getDiscriminatorColumnName() );
		assertEquals( "dog", dogPersister.getDiscriminatorValue() );

		p = sessionFactory().getEntityPersister( Cat.class.getName() );
		assertNotNull( p );
		final JoinedSubclassEntityPersister catPersister = assertTyping( JoinedSubclassEntityPersister.class, p );
		assertEquals( "string", catPersister.getDiscriminatorType().getName() );
		assertEquals( "type", catPersister.getDiscriminatorColumnName() );
		assertEquals( "cat", catPersister.getDiscriminatorValue() );
	}

	@Test
	public void basicUsageTest() {
		Session session = openSession();
		session.beginTransaction();
		session.save( new Cat( 1 ) );
		session.save( new Dog( 2 ) );
		session.getTransaction().commit();
		session.close();

		session = openSession();
		session.beginTransaction();
		session.createQuery( "from Animal" ).list();
		Cat cat = (Cat) session.get( Cat.class, 1 );
		assertNotNull( cat );
		session.delete( cat );
		Dog dog = (Dog) session.get( Dog.class, 2 );
		assertNotNull( dog );
		session.delete( dog );
		session.getTransaction().commit();
		session.close();
	}
}
